package io.academic.entity;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DBArticleRepository extends PagingAndSortingRepository<DBArticle, Long> {

    List<DBArticle> findByTitle(String title);

}
