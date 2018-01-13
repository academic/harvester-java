package io.academic.controller;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.academic.dao.CrawlerDao;
import io.academic.dao.MessageDao;
import io.academic.entity.OaiRecordRepository;
import io.academic.service.OaiService;
import io.academic.service.UrlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CrawlerController {

    @Autowired
    UrlProcessor urlProcessor;

    @Autowired
    OaiRecordRepository oaiRecordRepository;

    OaiService oaiService;


    @PostMapping(value = "/crawl/list-records")
    @ExceptionMetered
    @Timed
    public MessageDao add(@RequestBody @Valid CrawlerDao crawlerDao) {
        try {
            urlProcessor.submit(crawlerDao.getUrl());
        } catch (InterruptedException ie) {
            throw new RuntimeException("Queue is full", ie);
        }
        return new MessageDao("Queued");
    }







}
