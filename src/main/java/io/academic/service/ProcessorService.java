package io.academic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.Executor;

@Service
public class ProcessorService {

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private RecordTypeProcessor recordTypeProcessor;

    @Autowired
    private UrlProcessor urlProcessor;

    @Autowired
    private OaiService oaiService;


    @PostConstruct
    public void startProcessors() {
        taskExecutor.execute(recordTypeProcessor);
        taskExecutor.execute(urlProcessor);
        try {
            urlProcessor.submit("http://dergipark.gov.tr/api/public/oai/?from=2018-01-07&until=2018-01-08&metadataPrefix=oai_dc&verb=ListRecords");
        } catch (InterruptedException e) {
            throw new RuntimeException("la noliii", e);
        }
        try {
            oaiService.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean submitUrl(String url) throws InterruptedException {
        return urlProcessor.submit(url);
    }

}
