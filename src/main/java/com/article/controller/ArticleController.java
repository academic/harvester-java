package com.article.controller;

import com.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by musaceylan on 11/8/16.
 */
@RestController
@RequestMapping("/api/oai")
public class ArticleController {

    @Autowired
    public ArticleService articleService;

    @RequestMapping(value = "/articlesAll", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity query(@RequestParam(value = "articleValue", required = false) String articleValue) {
        ResponseEntity responseEntity = null;
        try {
            articleService.getArticle();
            responseEntity = new ResponseEntity<Void>(HttpStatus.OK);

        }

        catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }


}
