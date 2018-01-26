package io.academic.controller;

import io.academic.service.OaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin(origins = "http://localhost:8000", maxAge = 3600)
@RestController
@RequestMapping("/api/elastic")
public class SearchController {
    private final OaiService service;

    @Autowired
    public SearchController(OaiService service) {
        this.service = service;
    }

    @RequestMapping(method = GET, value = "/_search")
    public String search(@RequestParam(value= "q") String term) throws IOException {
        return service.search(term);
    }

    @RequestMapping(method = GET)
    public String allData(@RequestParam(defaultValue = "") String query) throws IOException {
        return service.getAll();
    }


}
