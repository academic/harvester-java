package io.academic.dao;

public class OaiDataProviderDao {

    private String name;
    private String url;
    private String identifier;

    public OaiDataProviderDao(String name,String url,String identifier) {
        this.name = name;
        this.url = url;
        this.identifier = identifier;
    }
    public OaiDataProviderDao(String url) {
        this.url = url;
        this.name = "";
        this.identifier="";
    }

    public OaiDataProviderDao() {
    }

    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }


    public String getIdentifier() {
        return identifier;
    }


}
