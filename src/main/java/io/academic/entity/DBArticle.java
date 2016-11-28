package io.academic.entity;

import io.academic.dao.CreateArticleDao;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "articles")
@Entity
public class DBArticle {

    public DBArticle(CreateArticleDao createArticleDao) {
        this.title = createArticleDao.getTitle();
        this.body = createArticleDao.getBody();
        this.keywords = createArticleDao.getKeywords();
        this.authors = createArticleDao.getAuthors();
        this.dc = createArticleDao.getDc();
    }

    @Type(type = "pg-uuid")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    @Id
    private UUID id;

    @Column
    private String title;

    @Column
    private String body;

    @Column
    private String keywords;

    @Column
    private String authors;

    @Column
    private String dc;

    public DBArticle() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
