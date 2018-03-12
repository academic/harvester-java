package io.academic.service;

import io.academic.dao.OaiDataProviderDao;
import io.academic.entity.Article;
import io.academic.entity.ArticleRepository;
import io.academic.entity.OaiDataProvider;
import io.academic.entity.OaiDataProviderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OaiDataProviderService {

    private Logger log = LoggerFactory.getLogger(OaiDataProviderService.class);

    @Autowired
    private OaiDataProviderRepository oaiDataProviderRepository;

    @Autowired
    private UrlProcessor urlProcessor;

    @Autowired
    private OaiService oaiService;

    @Async
    public void queue(OaiDataProvider oaiDataProvider) {
        oaiDataProviderRepository.save(oaiDataProvider);
        try {
//            urlProcessor.submit("http://dergipark.gov.tr/api/public/oai/?from=2018-01-07&until=2018-01-08&metadataPrefix=oai_dc&verb=ListRecords");
                submitUrl(oaiDataProvider.getUrl());
        } catch (InterruptedException e) {
            throw new RuntimeException("la noliii", e);
        }

        log.info("Oai Data Provider saved in PostgreSQL with ID: {}", oaiDataProvider.getId());
    }

    @Async
    public void queue(OaiDataProviderDao oaiDataProviderDao) {
        OaiDataProvider oaiDataProvider = new OaiDataProvider(oaiDataProviderDao);
        oaiDataProviderRepository.save(oaiDataProvider);
        try {
//            urlProcessor.submit("http://dergipark.gov.tr/api/public/oai/?from=2018-01-07&until=2018-01-08&metadataPrefix=oai_dc&verb=ListRecords");
            submitUrl(oaiDataProvider.getUrl());
        } catch (InterruptedException e) {
            throw new RuntimeException("la noliii", e);
        }

        log.info("Oai Data Provider saved in PostgreSQL with ID: {}", oaiDataProvider.getId());
    }

    @Async
    public void queue(String oaiDataProviderUrl) {
        OaiDataProvider oaiDataProvider = new OaiDataProvider(oaiDataProviderUrl);
        oaiDataProviderRepository.save(oaiDataProvider);
        try {
//            urlProcessor.submit("http://dergipark.gov.tr/api/public/oai/?from=2018-01-07&until=2018-01-08&metadataPrefix=oai_dc&verb=ListRecords");
            submitUrl(oaiDataProvider.getUrl());
        } catch (InterruptedException e) {
            throw new RuntimeException("la noliii", e);
        }

        log.info("Oai Data Provider saved in PostgreSQL with ID: {}", oaiDataProvider.getId());
    }

    public boolean submitUrl(String url) throws InterruptedException {
        return urlProcessor.submit(addRule(url));
    }

    //this method add some small rule to the url and by the help of it we did not crawl all data, we  crawl only limited data between these dates
    //do not forget delete before deployment date on the rules will be deleted
    public String addRule(String url)
    {
//        String rule = "?metadataPrefix=oai_dc&verb=ListRecords";
        String rule = "?from=2017-01-01&until=2017-01-02&metadataPrefix=oai_dc&verb=ListRecords";
        return url+rule;
    }

}