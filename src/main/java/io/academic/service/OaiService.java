package io.academic.service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.index.IndexService;
import eu.luminis.elastic.search.SearchService;
import io.academic.entity.OaiRecord;
import io.academic.entity.OaiRecordRepository;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.openarchives.oai._2.MetadataType;
import org.openarchives.oai._2.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ObjectMapper objectMapper;

    @Autowired
    private CounterService counterService;

    RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200, "http"),
            new HttpHost("localhost", 9201, "http")).build();


    public static final String INDEX = "harvester";
    private static final String TYPE = "oai";

    private  DocumentService documentService = null;
    private  IndexService indexService = null;
    private  SearchService searchService = null;

    @Autowired
    public OaiService(DocumentService documentService, IndexService indexService, SearchService searchService) {
        this.documentService = documentService;
        this.indexService = indexService;
//        indexService.createIndex();
        this.searchService = searchService;
    }

    public String elasticSave(OaiRecord oaiRecord) {
        IndexRequest request = new IndexRequest(INDEX, TYPE).setEntity(oaiRecord);

        if (oaiRecord.getId() != null) {
            request.setId(String.valueOf(oaiRecord.getId()));
        }

        return documentService.index(request);
    }


    @Timed
    @ExceptionMetered
    @Transactional
    public void saveRecords(List<RecordType> recordTypes) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<OaiRecord> oaiRecords = new ArrayList<>();
        recordTypes.forEach((recordType) -> {
            OaiRecord oaiRecord = new OaiRecord();
            oaiRecord.setSpec(recordType.getHeader().getSetSpec().get(0));
            oaiRecord.setIdentifier(recordType.getHeader().getIdentifier());
            oaiRecord.setDatestamp(parseDateTime(recordType.getHeader().getDatestamp()));
            oaiRecord.setDc(marshallDc(recordType.getMetadata()));
            oaiRecord.setState(0);
            oaiRecords.add(oaiRecord);

            elasticSave(oaiRecord);
        });

        oaiRecordRepository.save(oaiRecords);
        log.info("Saved {} OAI record, time {}ms", oaiRecords.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
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
        Map<String, String> paramMap = new HashMap<String, String>();
        String query = "dc:"+q;
        paramMap.put("q", query);
        paramMap.put("pretty", "true");

        Response response = restClient.performRequest("GET", "/harvester/_search",
                paramMap);
        String result =  ( EntityUtils.toString(response.getEntity()));

        System.out.println(result);
        System.out.println("Host -" + response.getHost() );
        System.out.println("RequestLine -"+ response.getRequestLine() );
        return "<pre>"+result+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String getAll() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pretty", "true");
        Response response = restClient.performRequest("GET", "/harvester/_search", paramMap);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        System.out.println("Host -" + response.getHost() );
        System.out.println("RequestLine -"+ response.getRequestLine() );
        return "<pre>"+result+"</pre>";
    }

    public void delete() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pretty", "true");
        Response response = restClient.performRequest("DELETE", "/harvester/", paramMap);

    }



}
