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

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class AcademicSearchService {

    @Autowired
    private OaiService oaiService;

    public AcademicSearchService() {
    }

    public String search(String q) throws IOException {

        SearchRequest searchRequest = new SearchRequest("harvester");
        searchRequest.source(buildSource("term","dc",q,false));

        //this values are necessary if we need scrollable results (in other words if our result have more than 10 hits)
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1));
        searchRequest.scroll(scroll);

        SearchResponse searchResponse = oaiService.getRestClient().search(searchRequest);
        String result = scrollableSearch(searchResponse,scroll);

        return result;

    }


    public String searchBy(String q, String criteria) throws IOException {

        SearchRequest searchRequest = new SearchRequest("harvester");
        searchRequest.source(buildSource("match",criteria,q,false));

        //this values are necessary if we need scrollable results (in other words if our result have more than 10 hits)
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1));
        searchRequest.scroll(scroll);

        SearchResponse searchResponse = oaiService.getRestClient().search(searchRequest);
        String result = scrollableSearch(searchResponse,scroll);

//        System.out.println(result);

        return result;

    }


    public String getAll() throws IOException {

        SearchRequest searchRequest = new SearchRequest("harvester");
        searchRequest.source(buildSource("matchAll","","",true));

        //this values are necessary if we need scrollable results (in other words if our result have more than 10 hits)
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1));
        searchRequest.scroll(scroll);

        SearchResponse searchResponse = oaiService.getRestClient().search(searchRequest);
        String result = scrollableSearch(searchResponse,scroll);

//        System.out.println(result);

        return result;
    }



    public String searchPretty(String q) throws IOException {


        return "<pre>"+search(q)+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String getAllPretty() throws IOException {


        return "<pre>"+getAll()+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchPrettyByCriteria(String q, String criteria) throws IOException {

        return "<pre>"+searchBy(q,criteria)+"</pre>"; //pre tag for json, otherwise it didnt show pretty in browser

    }

    public String searchForm(String q) throws IOException {

        return search(q);

    }

    public String searchFormByCriteria(String q, String criteria) throws IOException {

        return searchBy(q,criteria);

    }

    //Helper methods

    //converts normal string to pretty Json String
    public String toJson(String nonJsonString){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(nonJsonString);
        String jsonString = gson.toJson(je);
        return jsonString;
    }



    public SearchSourceBuilder buildSource(String queryType, String criteria, String q, Boolean showAllFields){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (queryType.equals("match"))
        {
            searchSourceBuilder.query(matchQuery(criteria,q));
        }
        else if (queryType.equals("term"))
        {
            searchSourceBuilder.query(QueryBuilders.termQuery(criteria,q));
        }
        else
        {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        //we are allways sorting according to title, if it is needed this can be changed to related criteria
        searchSourceBuilder.sort(new FieldSortBuilder("title.keyword").order(SortOrder.DESC));
        if (!showAllFields)
        {
            String[] includeFields = new String[] {"title",criteria};
            String[] excludeFields = new String[] {""};
            searchSourceBuilder.fetchSource(includeFields,excludeFields);
            searchSourceBuilder.fetchSource(true);
        }

        return searchSourceBuilder;
    }

    public String scrollableSearch(SearchResponse searchResponse, Scroll scroll) throws IOException {
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String result="";

        //right now we are returning all results by concatenating results so it is not an elegant solution
        while (searchHits != null && searchHits.length > 0) {
            result+=toJson(searchResponse.toString());

            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = oaiService.getRestClient().searchScroll(scrollRequest);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();

        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = oaiService.getRestClient().clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isSucceeded();
        return result;
    }


}
