package io.academic.dao;


import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class CrawlerDao {

    @NotBlank
    private String url;

    @NotNull
    private Boolean followResumptionToken;

    public String getUrl() {
        return url;
    }

    public Boolean getFollowResumptionToken() {
        return followResumptionToken;
    }
}
