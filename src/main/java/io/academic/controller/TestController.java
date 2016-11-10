package io.academic.controller;

import io.academic.dao.CreateArticleDao;
import io.academic.dao.MessageDao;
import io.academic.document.ESArticle;
import io.academic.document.ESArticleRepository;
import io.academic.entity.DBArticle;
import io.academic.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TestController {

    @Autowired
    ArticleService articleService;

    @PostMapping(value = "/add")
    public MessageDao add(@RequestBody @Valid CreateArticleDao createArticleDao) {
        articleService.queue(new ESArticle(createArticleDao));
        articleService.queue(new DBArticle(createArticleDao));
        return new MessageDao("Queued!");
    }


}
