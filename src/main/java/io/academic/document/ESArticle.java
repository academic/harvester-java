package io.academic.document;

import io.academic.dao.CreateArticleDao;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "articles", type = "article", shards = 1, replicas = 0)
public class ESArticle implements Serializable {

    public ESArticle(CreateArticleDao createArticleDao) {
        this.title = createArticleDao.getTitle();
        this.body = createArticleDao.getBody();
        this.keywords = createArticleDao.getKeywords();
        this.authors = createArticleDao.getAuthors();
        this.dc = createArticleDao.getDc();
    }

    @Id
    private String id;

    private String title;

    private String body;

    private String keywords;

    private String authors;

    private String dc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
