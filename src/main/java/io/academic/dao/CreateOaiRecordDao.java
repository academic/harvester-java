package io.academic.dao;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class CreateOaiRecordDao {

    public CreateOaiRecordDao() {
    }

    @NotBlank
    private String url;

    @NotBlank
    private String token;

    private Date responseDate;

    private String identifier;

    private String datestamp;

    @NotBlank
    private String record;

    @NotBlank
    private Integer state;

    public String getURL() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public String getRecord() {
        return record;
    }

    public Integer getState() {
        return state;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDatestamp() {
        return datestamp;
    }
}
