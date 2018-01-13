package io.academic.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "oai_record")
@Entity
public class OaiRecord extends AbstractAuditingEntity {

    public OaiRecord(){

    }

    @Column
    private String spec;

    @Column
    private String identifier;

    @Column
    private LocalDateTime datestamp;

    @Column
    @Type(type = "text")
    private String dc;

    @Column
    private Integer state;

    public String getSpec() {
        return spec;
    }

    public OaiRecord setSpec(String spec) {
        this.spec = spec;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public OaiRecord setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public LocalDateTime getDatestamp() {
        return datestamp;
    }

    public OaiRecord setDatestamp(LocalDateTime datestamp) {
        this.datestamp = datestamp;
        return this;
    }

    public String getDc() {
        return dc;
    }

    public OaiRecord setDc(String dc) {
        this.dc = dc;
        return this;
    }

    public Integer getState() {
        return state;
    }

    public OaiRecord setState(Integer state) {
        this.state = state;
        return this;
    }
}