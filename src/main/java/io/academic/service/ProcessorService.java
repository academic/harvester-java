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
        System.out.println("try oncesi");
//        try {
//            submitDataProvider(new OaiDataProviderDao("Acta Medica Anatolia","http://dergipark.gov.tr/api/public/oai/","dergipark.ulakbim.gov.tr"  ));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        oaiDataProviderService.queue(new OaiDataProvider("Acta Medica Anatolia","http://dergipark.gov.tr/api/public/oai/","dergipark.ulakbim.gov.tr"  ));
//        oaiDataProviderService.queue(new OaiDataProvider("http://export.arxiv.org/oai2"));
//          oaiDataProviderService.queue(new OaiDataProvider("http://www.acikarsiv.gazi.edu.tr/oai/oai2.php?metadataPrefix=oai_dc&verb=ListRecords"));
//        try {
//            urlProcessor.submit("http://dergipark.gov.tr/api/public/oai/?metadataPrefix=oai_dc&verb=ListRecords");
//        } catch (InterruptedException e) {
//            throw new RuntimeException("la noliii", e);
//        }
//        try {
//            oaiService.delete();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    public boolean submitUrl(String url) throws InterruptedException {
        return urlProcessor.submit(url);
    }

//    public boolean submitDataProvider(OaiDataProviderDao oai) throws InterruptedException {
//       return oaiDataProviderProcessor.submit(oai);
//
//    }



}
