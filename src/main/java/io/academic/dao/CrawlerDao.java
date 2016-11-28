package io.academic.dao;


import org.hibernate.validator.constraints.NotBlank;

public class CrawlerDao {
    public CrawlerDao() {
        this.href = href;
    }

    @NotBlank
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href){
        this.href = href;
    }


}
