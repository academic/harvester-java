package io.academic.dao;


import org.hibernate.validator.constraints.NotBlank;

public class CrawlerDao {
    public CrawlerDao() {}

    @NotBlank
    private String href;

    public String getHref() {
        return href;
    }

}
