package io.academic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "articles")
@Entity
public class Article extends AbstractAuditingEntity {

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

    public String getTitle() {
        return title;
    }

    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Article setBody(String body) {
        this.body = body;
        return this;
    }

    public String getKeywords() {
        return keywords;
    }

    public Article setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public String getAuthors() {
        return authors;
    }

    public Article setAuthors(String authors) {
        this.authors = authors;
        return this;
    }

    public String getDc() {
        return dc;
    }

    public Article setDc(String dc) {
        this.dc = dc;
        return this;
    }
}
