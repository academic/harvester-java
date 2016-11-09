package com.article.repository;


import com.article.entities.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * Created by musaceylan on 11/8/16.
 */
public interface ArticleRepository extends ElasticsearchCrudRepository<Article,String>{

    public List < Article> findByContent(String content);

}
