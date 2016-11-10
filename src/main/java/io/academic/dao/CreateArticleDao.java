package io.academic.dao;

import org.hibernate.validator.constraints.NotBlank;

public class CreateArticleDao {

    public CreateArticleDao() {
    }

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
