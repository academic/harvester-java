package io.academic.entity;

import io.academic.dao.OaiRecordDao;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by musaceylan on 11/26/16.
 */
public class OaiRecordEntity {

    public OaiRecordEntity(OaiRecordDao oaiRecordDao) {
            this.URL = oaiRecordDao.getURL();
        this.token = oaiRecordDao.getToken();
        this.responseDate = oaiRecordDao.getResponseDate();
        this.record = oaiRecordDao.getRecord();
        this.state = oaiRecordDao.getState();

    }

    @Column
    private String URL;

    @Column
    private String token;

    @Column
    private Date responseDate;

    @Column
    private String record;

    @Column
    private String state;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
