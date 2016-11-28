package io.academic.service;

import io.academic.document.ESArticle;
import io.academic.document.ESArticleRepository;
import io.academic.entity.DBArticle;
import io.academic.entity.DBArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ESArticleRepository esArticleRepository;

    @Autowired
    private DBArticleRepository dbArticleRepository;

    @Async
    public void queue(ESArticle esArticle) {
        esArticleRepository.save(esArticle);
        log.info("Article saved in Elasticsearch with ID: {}", esArticle.getId());

    }

    @Async
    public void queue(DBArticle dbArticle) {
        dbArticleRepository.save(dbArticle);
        log.info("Article saved in PostgreSQL with ID: {}", dbArticle.getId());
    }
}
