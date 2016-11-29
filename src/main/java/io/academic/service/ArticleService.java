package io.academic.service;

import io.academic.entity.Article;
import io.academic.entity.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Async
    public void queue(Article article) {
        articleRepository.save(article);
        log.info("Article saved in PostgreSQL with ID: {}", article.getId());
    }
}
