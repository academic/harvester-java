package io.academic.controller;


import io.academic.service.OaiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
public class SearchformController {

    private final OaiService service;

    public SearchformController(OaiService service) {
        this.service = service;
    }


    @GetMapping("/searchform")
    public String searchForm(Model model)  {
        System.out.println("form called");
        model.addAttribute("searchForm", new Searchform());
        return "searchform";
    }

    @PostMapping("/searchform")
    public String greetingSubmit(@ModelAttribute Searchform searchform) throws IOException {
        searchform.setResult(service.searchForm(searchform.getValue()));
        return "searchresult";
    }


}
