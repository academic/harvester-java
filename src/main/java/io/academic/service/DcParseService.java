package io.academic.service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Stopwatch;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openarchives.oai._2.ListRecordsType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class DcParseService {

    private Logger log = LoggerFactory.getLogger(UrlProcessor.class);

    @Autowired
    private Unmarshaller unmarshaller;

    @Resource
    @Qualifier("recordListQueue")
    private ArrayBlockingQueue<List<RecordType>> recordListQueue;

    @Resource
    @Qualifier("dcQueue")
    private ArrayBlockingQueue<String> urlQueue;

    @Timed
    @ExceptionMetered
    public void dcParseRecord(String url) throws IOException, URISyntaxException, InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            log.info("Send fetch list request {}", url);
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            JAXBElement<OAIPMHtype> records = (JAXBElement<OAIPMHtype>) unmarshaller.unmarshal(new StreamSource(closeableHttpResponse.getEntity().getContent()));
            ListRecordsType list = records.getValue().getListRecords();
            log.info("Submit {} record", list.getRecord().size());
            recordListQueue.offer(list.getRecord());

            if (list.getResumptionToken() != null && list.getResumptionToken().getValue().length() > 0) {
                URIBuilder uriBuilder = new URIBuilder(url);
                uriBuilder.setParameter("resumptionToken", list.getResumptionToken().getValue());
                url = uriBuilder.build().toString();

                log.info("Queue new url: {}, {}", list.getResumptionToken().getValue(), url);
                urlQueue.put(url);
            }
        }
        log.info("fetchListRecords() took {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

}
