package io.academic.entity;

import io.academic.dao.CreateOaiRecordDao;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oai_record")
@Entity
public class OaiRecord {

    public OaiRecord(CreateOaiRecordDao createOaiRecordDao) {
        this.url = createOaiRecordDao.getURL();
        this.token = createOaiRecordDao.getToken();
        this.responseDate = createOaiRecordDao.getResponseDate();
        this.record = createOaiRecordDao.getRecord();
        this.state = createOaiRecordDao.getState();

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Column
    private String token;

    @Column
    private Date responseDate;

    @Column
    private String record;

    @Column
    private String state;


    public OaiRecord() {

    }
    public String getURL() {
        return url;
    }

    public void setURL(String URL) {
        this.url = url;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
