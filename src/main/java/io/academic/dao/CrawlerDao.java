package io.academic.dao;


import org.hibernate.validator.constraints.NotBlank;

public class CrawlerDao {

    @NotBlank
    private String url;

    public String getUrl() {
        return url;
    }
}
