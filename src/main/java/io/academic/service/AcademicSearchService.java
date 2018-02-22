package io.academic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.elasticsearch.action.search.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AcademicSearchService {

    @Autowired
    private OaiService oaiService;

    public AcademicSearchService() {
    }

    public String search(String q) throws IOException {

        SearchRequest searchRequest = new SearchRequest("harvester");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("dc",q));
        searchSourceBuilder.sort(new FieldSortBuilder("title.keyword").order(SortOrder.DESC));
        searchSourceBuilder.fetchSource("title","");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = oaiService.getRestClient().search(searchRequest);
        String result = searchResponse.toString();
        String jsonString = toJson(result);


        System.out.println(jsonString);

        return jsonString; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchBy(String q, String criteria) throws IOException {

        SearchRequest searchRequest = new SearchRequest("harvester");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(criteria,q));
//        searchSourceBuilder.sort(new FieldSortBuilder("title.keyword").order(SortOrder.DESC));
//        searchSourceBuilder.fetchSource("title","");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = oaiService.getRestClient().search(searchRequest);
        String result = searchResponse.toString();
        String jsonString = toJson(result);


        System.out.println(jsonString);

        return jsonString; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchByType(String q) throws IOException
    {
        return searchBy(q,"type");
    }

    public String searchByJournal(String q) throws IOException
    {
        return searchBy(q,"publisher");
    }

    public String searchByDate(String q) throws IOException
    {
        return searchBy(q,"date");
    }

    public String searchByAuthor(String q) throws IOException
    {
        return searchBy(q,"author");
    }

    public String searchByTitle(String q) throws IOException
    {
        return searchBy(q,"title");
    }

    public String searchByKeyword(String q) throws IOException
    {
        return searchBy(q,"keyword");
    }

    public String searchByAbstract(String q) throws IOException
    {
        return searchBy(q,"body");
    }

    //converts normal string to pretty Json String
    public String toJson(String nonJsonString){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(nonJsonString);
        String jsonString = gson.toJson(je);
        return jsonString;
    }

    public String searchPretty(String q) throws IOException {


        return "<pre>"+search(q)+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchPrettyByCriteria(String q, String criteria) throws IOException {

        System.out.println(q);
        System.out.println(criteria);
        return "<pre>"+searchBy(q,criteria)+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchForm(String q) throws IOException {

        return search(q); //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchFormByCriteria(String q, String criteria) throws IOException {

        return searchBy(q,criteria); //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String getAll() throws IOException {


        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

        SearchRequest searchRequest = new SearchRequest("harvester");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = oaiService.getRestClient().search(searchRequest);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String result="";

        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = oaiService.getRestClient().searchScroll(scrollRequest);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();

            result+=toJson(searchResponse.toString());
        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = oaiService.getRestClient().clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isSucceeded();


        return "<pre>"+result+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser
    }
}
