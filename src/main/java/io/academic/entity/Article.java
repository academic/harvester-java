package io.academic.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Table(name = "articles")
@Entity
public class Article extends AbstractAuditingEntity {

    @Column
    @Type(type = "text")
    private String title;

    @Column
    @Type(type = "text")
    private String body;

    @Column
    @Type(type = "text")
    private String keywords;

    @Column
    @Type(type = "text")
    private String authors;

    @Column
    @Type(type = "text")
    private String dc;

    @Column
    @Type(type = "text")
    private String publisher;

    @Column
    @Type(type = "text")
    private String date;

    @Column
    @Type(type = "text")
    private String type;

    @Column
    @Type(type = "text")
    private String base64;

    @Column
    @Type(type = "text")
    private String articleIdentifier;

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getArticleIdentifier() {
        return articleIdentifier;
    }

    public void setArticleIdentifier(String articleIdentifier) {
        this.articleIdentifier = articleIdentifier;
    }

}
