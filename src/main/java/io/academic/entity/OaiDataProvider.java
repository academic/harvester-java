package io.academic.entity;

import io.academic.dao.OaiDataProviderDao;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "oai_data_provider")
@Entity
public class OaiDataProvider extends AbstractAuditingEntity {

    public OaiDataProvider(){

    }

    public OaiDataProvider(String name, String url,String downloadUrl, String identifier) {
        this.name = name;
        this.url = url;
        this.downloadUrl = downloadUrl;
        this.identifier = identifier;
    }

    public OaiDataProvider(String url,String downloadUrl) {
        this.url = url;
        this.downloadUrl = downloadUrl;
    }

    public OaiDataProvider(String url) {
        this.url = url;
    }

    @Column
    private String name;

    @Column
    private String url;

    @Column
    private String downloadUrl;

    @Column
    private String identifier;

    public OaiDataProvider(OaiDataProviderDao oaiDataProviderDao) {
        this.name = oaiDataProviderDao.getName();
        this.url = oaiDataProviderDao.getUrl();
        this.identifier = oaiDataProviderDao.getIdentifier();
    }


    public String getName() {
        return name;
    }

    public OaiDataProvider setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public OaiDataProvider setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public OaiDataProvider setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}