package io.academic.controller;


import io.academic.dao.SearchDao;
import io.academic.service.AcademicSearchService;
import io.academic.service.OaiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;


@Controller
public class SearchformController {

    private final AcademicSearchService service;

    public SearchformController(AcademicSearchService service) {
        this.service = service;
    }


    @GetMapping("/searchform")
    public String searchForm(Model model)  {
        model.addAttribute("searchForm", new SearchDao());
        return "searchform";
    }

    @PostMapping("/searchform")
    public String greetingSubmit(@ModelAttribute SearchDao searchDao) throws IOException {
        System.out.println("inside post");
        System.out.println(searchDao.getValue());
        System.out.println(service.searchForm(searchDao.getValue()));
        searchDao.setResult(service.searchForm(searchDao.getValue()));
        return "searchresult";
    }


}
