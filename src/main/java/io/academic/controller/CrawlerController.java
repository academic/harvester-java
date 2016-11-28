package io.academic.controller;

import io.academic.dao.MessageDao;
import io.academic.dao.CrawlerDao;
import io.academic.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CrawlerController {

    @Autowired
    CrawlerService crawlerService;

    @PostMapping(value = "/crawl")
    public MessageDao add(@RequestBody @Valid CrawlerDao crawlerDao) {

        crawlerService.parse(crawlerDao);

        return new MessageDao("Queued!");
    }
}
