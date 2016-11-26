package io.academic.service;

import io.academic.document.ESArticleRepository;
import io.academic.entity.DBArticleRepository;
import io.academic.entity.OaiRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Crawls an OAI endpoint and stores all.
 */
@Service
public class OaiService {


    @Autowired
    ESArticleRepository esArticleRepository;

    @Autowired
    DBArticleRepository dbArticleRepository;

    @Autowired
    OaiRecordRepository oaiRecordRepository;





}
