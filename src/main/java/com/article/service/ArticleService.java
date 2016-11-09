package com.article.service;

import com.article.entities.Article;
import com.article.repository.ArticleRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by musaceylan on 11/8/16.
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository repository;

    public Iterable <Article> findAll(){
        return repository.findAll();
    }

    public void deleteAll(){
        this.repository.deleteAll();
    }

    public void getArticle(){

        Date publishDate = new Date();

        List<Article>  articlesList = new ArrayList<>();

        for (int i = 0 ; i<1 ; i++){
            List articlesListTemp = getArticlesList(publishDate.toString());
            articlesList.addAll(articlesListTemp);
            /// startDate = startDate.plusDays(1L); uses not DATE LOCALDATE

        }

//        for (Article article:articlesList){
//            getArticleContent(article);
//            //System.out.print(article.getContent);
//            this.repository.save(article);
//        }

     }



    private List<Article> getArticlesList(String date){


        Article article = null;
        List<Article> articleList = new ArrayList<>();

        try{
            Jsoup.connect("http://dergipark.gov.tr/api/public/oai/?verb=ListRecords&metadataPrefix=oai_dc").get();

        }
        catch (IOException e ){
            e.printStackTrace();
        }

        return articleList;
     }


    private void getArticleContent(Article article) {
    }

}
