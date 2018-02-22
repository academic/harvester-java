package io.academic.entity;

import org.elasticsearch.action.search.*;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ArticleRepository extends PagingAndSortingRepository<Article, UUID> {

    List<Article> findByTitle(String title);


}
