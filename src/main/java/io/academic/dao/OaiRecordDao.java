package io.academic.dao;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * Created by musaceylan on 11/26/16.
 */
public class OaiRecordDao {

    @NotBlank
    private String URL;

    @NotBlank
    private String token;

    @NotBlank
    private Date responseDate;

    @NotBlank
    private String record;

    @NotBlank
    private String state;

    public String getURL() {
        return URL;
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
