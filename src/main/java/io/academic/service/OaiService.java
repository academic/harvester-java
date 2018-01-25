package io.academic.service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.index.IndexService;
import eu.luminis.elastic.search.SearchService;
import io.academic.entity.Article;
import io.academic.entity.ArticleRepository;
import io.academic.entity.OaiRecord;
import io.academic.entity.OaiRecordRepository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openarchives.oai._2.MetadataType;
import org.openarchives.oai._2.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Crawls an OAI endpoint and stores all.
 */
@Service
public class OaiService {

    private Logger log = LoggerFactory.getLogger(OaiService.class);



    @Autowired
    private OaiRecordRepository oaiRecordRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DcParseService dcParseService;


    RestHighLevelClient restClient = new RestHighLevelClient( RestClient.builder(
            new HttpHost("localhost", 9200, "http"),
            new HttpHost("localhost", 9201, "http")));


    public static final String INDEX = "harvester";
    private static final String TYPE = "oai";

    private  DocumentService documentService = null;
    private  IndexService indexService = null;
    private  SearchService searchService = null;
    private IndexRequest request;

    @Autowired
    public OaiService(DocumentService documentService, IndexService indexService, SearchService searchService) {
        this.documentService = documentService;
        this.indexService = indexService;
//        indexService.createIndex();
        this.searchService = searchService;
    }

    public String elasticSave(Article article) {
        IndexRequest request = new IndexRequest(INDEX, TYPE).setEntity(article);


        if (article.getId() != null) {
            request.setId(String.valueOf(article.getId()));
        }

        return documentService.index(request);
    }


    @Timed
    @ExceptionMetered
    @Transactional
    public void saveRecords(List<RecordType> recordTypes) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<OaiRecord> oaiRecords = new ArrayList<>();
        List<Article> articles = new ArrayList<>();
        JSONParser parser = new JSONParser();
        recordTypes.forEach((recordType) -> {
            OaiRecord oaiRecord = new OaiRecord();
            oaiRecord.setSpec(recordType.getHeader().getSetSpec().get(0));
            oaiRecord.setIdentifier(recordType.getHeader().getIdentifier());
            oaiRecord.setDatestamp(parseDateTime(recordType.getHeader().getDatestamp()));
            String parsedDc="";
            try {
                 parsedDc = dcParseService.parseRecordString((JSONObject) parser.parse( marshallDc(recordType.getMetadata())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            oaiRecord.setDc(parsedDc);

            oaiRecord.setState(0);
            oaiRecords.add(oaiRecord);

            String[] parts = parsedDc.split(";;");
            Article article = new Article();
            article.setTitle(parts[0].split("::")[1]);
            article.setAuthors(parts[1].split("::")[1]);
            article.setKeywords(parts[2].split("::")[1]);
            article.setBody(parts[3].split("::")[1]);
            article.setDc(parsedDc);
            articles.add(article);

            elasticSave(article);
        });

        oaiRecordRepository.save(oaiRecords);
        log.info("Saved {} OAI record, time {}ms", oaiRecords.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));

        articleRepository.save(articles);
        log.info("Saved {} Articles, time {}ms", articles.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Timed
    @ExceptionMetered
    public OaiRecord getOaiRecord(UUID id) {
        return oaiRecordRepository.findOne(id);
    }

    private String marshallDc(MetadataType metadataType) {
        try {
            return objectMapper.writeValueAsString(metadataType);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    private LocalDateTime parseDateTime(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return LocalDateTime.parse(string, formatter);
    }



    public String search(String q) throws IOException {

        SearchRequest searchRequest = new SearchRequest("harvester");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("dc",q));
        searchSourceBuilder.sort(new FieldSortBuilder("title.keyword").order(SortOrder.DESC));
        searchSourceBuilder.fetchSource("title","");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restClient.search(searchRequest);
        String result = searchResponse.toString();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(result);
        String prettyJsonString = gson.toJson(je);

        System.out.println(prettyJsonString);

        return "<pre>"+prettyJsonString+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String getAll() throws IOException {


        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

        SearchRequest searchRequest = new SearchRequest("harvester");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String result="";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je ;
        String prettyJsonString ;
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = restClient.searchScroll(scrollRequest);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();


            je = jp.parse(searchResponse.toString());
            prettyJsonString = gson.toJson(je);
            result+=prettyJsonString;



        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = restClient.clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isSucceeded();


        return "<pre>"+result+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser
    }

    public void delete() throws IOException {


        DeleteIndexRequest request = new DeleteIndexRequest("harvester");
        restClient.indices().deleteIndex(request);

    }



}
