package io.academic.service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonObject;
import io.academic.dao.OaiDao;
import io.academic.entity.OaiRecord;
import io.academic.entity.OaiRecordRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    public String parseRecordString(JSONObject jsonObject) throws ParseException {
        String dc="";
        JsonObject jDc = new JsonObject();
        //we have 3 layer, outer encapsulates inner and inner encapsulates cell
        if (null != jsonObject.get("any")) {
            JSONObject j = (JSONObject) jsonObject.get("any");
            JSONObject jOuterValue = (JSONObject) j.get("value");
            JSONArray jOuterTitleOrCreatorOrSubject = (JSONArray) jOuterValue.get("titleOrCreatorOrSubject");

            String previousName=" ", previousValue=" ";
            for (int i = 0; i < jOuterTitleOrCreatorOrSubject.size(); i++) {

                JSONObject inner = (JSONObject) jOuterTitleOrCreatorOrSubject.get(i);
                JSONObject innerValue = (JSONObject) inner.get("value");
                String innerName = (String) inner.get("name");
                String cell = (String) innerValue.get("value");

                String[] parts = innerName.split("/");

                parts[6]=parts[6].substring(1);
                if (parts[6].equals(previousName))
                {
                    dc+=", "+cell.trim();
                }
                else
                {
                    if (dc.equals("")) {
                        dc = parts[6] + ":: " + cell.trim();
                    }
                    else {
                        dc += ";; \n" + parts[6] + ":: " + cell.trim();
                    }
                }
                previousName = parts[6];
            }

        }
        return dc;
    }

}
