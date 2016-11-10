package io.academic.entity;

import io.academic.dao.CreateArticleDao;

import javax.persistence.*;

@Table(name = "articles")
@Entity
public class DBArticle {

    public DBArticle(CreateArticleDao createArticleDao) {
        this.title = createArticleDao.getTitle();
        this.body = createArticleDao.getBody();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    private String title;

    @Column
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
