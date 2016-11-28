package io.academic.entity;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface DBArticleRepository extends PagingAndSortingRepository<DBArticle, UUID> {

    List<DBArticle> findByTitle(String title);

}
