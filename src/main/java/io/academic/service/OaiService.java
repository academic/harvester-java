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
import io.academic.dao.DcDao;
import io.academic.entity.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
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


    public RestHighLevelClient getRestClient() {
        return restClient;
    }

    RestHighLevelClient restClient = new RestHighLevelClient( RestClient.builder(
            new HttpHost("localhost", 9200, "http"),
            new HttpHost("localhost", 9201, "http")));


    public static final String INDEX = "harvester";
    private static final String TYPE = "oai";


    @Autowired
    public OaiService()
    {

    }

    public void elasticSave(Article article) throws IOException {
        IndexRequest request = new IndexRequest(INDEX,TYPE);
        request.setPipeline("academic-pdf");
        // before using this pipeline we have to add pipeline to the elasticsearch by following command
//        PUT _ingest/pipeline/academic-pdf
//        {
//            "description": "parse pdfs and index into ES",
//                "processors" :
//                  [
//                      { "attachment" : { "field": "pdf" } },
//                      { "remove" : { "field": "pdf" } }
//                  ]
//        }

            request.source(new Gson().toJson(article), XContentType.JSON);
            IndexResponse indexResponse = restClient.index(request);

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
            DcDao parsedDc = null;
            try {
                parsedDc = dcParseService.parseRecordString((JSONObject) parser.parse( marshallDc(recordType.getMetadata())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            oaiRecord.setDc(parsedDc.getDc());

            oaiRecord.setState(0);
            oaiRecords.add(oaiRecord);

            //TODO: we ave to check all the parts name and assigned according to related name not order
            String[] parts = parsedDc.getDc().split(";;");
            Article article = new Article();
            article.setTitle(parts[0].split("::")[1]);
            article.setAuthors(parts[1].split("::")[1]);
            article.setKeywords(parts[2].split("::")[1]);
            article.setBody(parts[3].split("::")[1]);
            article.setPublisher(parts[4].split("::")[1]);
            article.setDate(parts[5].split("::")[1]);
            article.setType(parts[6].split("::")[1]);
            if (parts.length>10)
            {
                String downlaodUrl = parts[10].split("::")[1];
                article.setRelation(downlaodUrl);
                article.setBase64(UrlPdftoBase64(downlaodUrl));
            }
            else
            {
                article.setRelation("not available");
                article.setBase64("bm90IGF2YWlsYWJsZQ=="); //it means not available in base 64
            }
            article.setDc(parsedDc.getDc());
            article.setArticleIdentifier(parseIdentifier(oaiRecord.getIdentifier()));

            articles.add(article);

            try {
                elasticSave(article);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public String UrlPdftoBase64(String url) {
        URL oracle = null;
        String base64 = "bm90IGF2YWlsYWJsZQ=="; //means not available
        System.out.println(url);
        try {
            oracle = new URL(url);
            URLConnection yc = oracle.openConnection();

            BufferedInputStream bis = new BufferedInputStream(yc.getInputStream());

            byte bytes[] = IOUtils.toByteArray(bis);
            bis.close();
             base64 = Base64.getEncoder().encodeToString(bytes);
            System.out.println(url);
            System.out.println(base64);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64;

    }

    private LocalDateTime parseDateTime(String string) {
        LocalDateTime ldt;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss'Z']");
        if (string.length()<=10)
        {
            ldt = LocalDate.parse(string,formatter).atStartOfDay(); // we can support yyyy-MM-dd format
        }
        else
        {
            ldt = LocalDateTime.parse(string, formatter);
        }
        return ldt;
//        return LocalDateTime.parse(string, formatter);
    }

    private String parseIdentifier(String oaiId){
        String Id="";
        Id = oaiId.substring(oaiId.lastIndexOf(':') + 1); // split identifier with ":" and take last part
        Id = Id.substring(Id.lastIndexOf('/') + 1); // split identifier with "/" and take last part
        return Id;
    }


    public void delete() {

        try {
            DeleteIndexRequest request = new DeleteIndexRequest("harvester");
            restClient.indices().deleteIndex(request);
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
                System.out.println("Index not found and not deleted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
