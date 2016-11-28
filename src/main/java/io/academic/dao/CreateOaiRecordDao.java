package io.academic.dao;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class CreateOaiRecordDao {

    public CreateOaiRecordDao() {
    }

    @NotBlank
    private String spec;

    @NotBlank
    private String token;

    private Date responseDate;

    @NotBlank
    private String identifier;

    private String datestamp;

    @NotBlank
    private String dc;

    @NotBlank
    private Integer state;

    public String getSpec() {
        return spec;
    }

    public String getToken() {
        return token;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public String getDc() {
        return dc;
    }

    public Integer getState() {
        return state;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDatestamp() {return datestamp;}
}
