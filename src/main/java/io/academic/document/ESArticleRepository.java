package io.academic.document;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface ESArticleRepository extends ElasticsearchCrudRepository<ESArticle, String> {

    List<ESArticle> findByTitle(String title);

}
