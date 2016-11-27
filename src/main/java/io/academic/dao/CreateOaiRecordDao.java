package io.academic.dao;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class CreateOaiRecordDao {

    public CreateOaiRecordDao(){
    }

    @NotBlank
    private String url;

    @NotBlank
    private String token;

    private Date responseDate;

    @NotBlank
    private String record;

    @NotBlank
    private String state;

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

    public String getState() {
        return state;
    }
}
