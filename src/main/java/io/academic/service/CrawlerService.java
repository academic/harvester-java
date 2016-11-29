package io.academic.service;

import io.academic.entity.OaiRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openarchives.oai._2.ListRecordsType;
import org.openarchives.oai._2.OAIPMHtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Crawls xmls and sends to data management services
 * This class needs refactoring for future repeating tasks
 * Only support ListRecordsType fetch / parse operations
 * Also some of the parts must written in defensive (null aware) perception
 */
@Service
public class CrawlerService {

    Logger log = LoggerFactory.getLogger(CrawlerService.class);

    @Autowired
    private Unmarshaller unmarshaller;

    @Autowired
    private OaiService oaiService;

    public ListRecordsType fetchListRecords(String url) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(url);
        log.info("Sending fetch list record request to {}", url);
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            JAXBElement<OAIPMHtype> records = (JAXBElement<OAIPMHtype>) unmarshaller.unmarshal(new StreamSource(closeableHttpResponse.getEntity().getContent()));
            ListRecordsType list = records.getValue().getListRecords();
            log.info("Found {} record", list.getRecord().size());
            list.getRecord().forEach(recordType -> {
                OaiRecord oaiRecord = new OaiRecord();
                oaiRecord.setSpec(recordType.getHeader().getSetSpec().get(0));
                oaiRecord.setIdentifier(recordType.getHeader().getIdentifier());
                oaiRecord.setDatestamp(parseDateTime(recordType.getHeader().getDatestamp()));
                oaiRecord.setDc(recordType.getMetadata().toString());
                oaiRecord.setState(0);
                oaiService.queue(oaiRecord);
            });

            return list;
        }
    }

    public void fetchAndFollowListRecords(String url) throws IOException, URISyntaxException {
        boolean follow = true;
        while (follow) {
            ListRecordsType list = fetchListRecords(url);
            if (list.getResumptionToken() != null && list.getResumptionToken().getValue().length() > 0) {
                URIBuilder uriBuilder = new URIBuilder(url);
                uriBuilder.addParameter("resumptionToken", list.getResumptionToken().getValue());
                url = uriBuilder.build().toString();
                log.info("Following with resumptionToken: {}", list.getResumptionToken().getValue());
            } else {
                follow = false;
            }
        }
    }

    private LocalDateTime parseDateTime(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return LocalDateTime.parse(string, formatter);
    }

}
