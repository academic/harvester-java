package io.academic.service;

import io.academic.entity.OaiRecord;
import io.academic.entity.OaiRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Crawls an OAI endpoint and stores all.
 */
@Service
public class OaiService {

    Logger log = LoggerFactory.getLogger(ArticleService.class);


    @Autowired
    private OaiRecordRepository oaiRecordRepository;

    @Async
    public void queue(OaiRecord oaiRecord) {
        oaiRecordRepository.save(oaiRecord);
        log.info("OAI saved in PostgreSQL with ID: {}", oaiRecord.getId());
    }
}
