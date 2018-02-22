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
        String value = searchDao.getValue();
        String result = "";
        String criteria = searchDao.getCriteria();
        if (criteria.equals("all"))
        {
            result = service.searchForm(searchDao.getValue());
        }
        else
        {
            result = service.searchFormByCriteria(searchDao.getValue(),criteria);
        }
        System.out.println(value);
        System.out.println(result);
        searchDao.setResult(result);
        return "searchresult";
    }


}
