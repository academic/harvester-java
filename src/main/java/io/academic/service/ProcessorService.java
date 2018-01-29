package io.academic.service;

import io.academic.dao.OaiDataProviderDao;
import io.academic.entity.OaiDataProvider;
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

    @Autowired
    private OaiDataProviderService oaiDataProviderService;




    @PostConstruct
    public void startProcessors() {
        taskExecutor.execute(recordTypeProcessor);
        taskExecutor.execute(urlProcessor);

//        oaiDataProviderService.queue(new OaiDataProvider("Acta Medica Anatolia","http://dergipark.gov.tr/api/public/oai/","dergipark.ulakbim.gov.tr"  ));
//        oaiDataProviderService.queue(new OaiDataProvider("http://export.arxiv.org/oai2"));

    }

    public boolean submitUrl(String url) throws InterruptedException {
        return urlProcessor.submit(url);
    }

//    public boolean submitDataProvider(OaiDataProviderDao oai) throws InterruptedException {
//       return oaiDataProviderProcessor.submit(oai);
//
//    }



}
