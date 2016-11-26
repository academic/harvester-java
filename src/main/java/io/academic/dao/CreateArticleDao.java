package io.academic.dao;

import org.hibernate.validator.constraints.NotBlank;

public class CreateArticleDao {

    public CreateArticleDao() {
    }

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotBlank
    private String keywords;

    @NotBlank
    private String authors;

    @NotBlank
    private String dc;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getAuthors() {
        return authors;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getDc() {
        return dc;
    }
}
