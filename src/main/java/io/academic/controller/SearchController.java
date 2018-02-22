package io.academic.controller;

import io.academic.service.AcademicSearchService;
import io.academic.service.OaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin(origins = "http://localhost:8000", maxAge = 3600)
@RestController
@RequestMapping("/api/elastic")
public class SearchController {
    private final AcademicSearchService service;

    @Autowired
    public SearchController(AcademicSearchService service) {
        this.service = service;
    }

    @RequestMapping(method = GET, value = "/_search")
    public String search(@RequestParam(value= "q") String term) throws IOException {
        return service.searchPretty(term);
    }

    @RequestMapping(method = GET)
    public String allData(@RequestParam(defaultValue = "") String query) throws IOException {
        return service.getAllPretty();
    }

    //search doing by article criterias such as authors,body,date, keywords, publisher, title
    @RequestMapping(method = GET, value = "/_search/{criteria}")
    public @ResponseBody String searchByCriteria(@RequestParam(value= "q") String term, @PathVariable(value = "criteria") String criteria) throws IOException {
        return service.searchPrettyByCriteria(term,criteria);
    }


}
