package io.academic.controller;


import io.academic.dao.OaiDataProviderDao;
import io.academic.dao.SearchDao;
import io.academic.entity.OaiDataProvider;
import io.academic.entity.OaiDataProviderRepository;
import io.academic.service.OaiDataProviderService;
import io.academic.service.OaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;


@Controller
public class DataproviderformController {

    private OaiDataProviderService oaiDataProviderService;
    @Autowired
    private OaiDataProviderRepository oaiDataProviderRepository;

    public DataproviderformController(OaiDataProviderService oaiDataProviderService) {
        this.oaiDataProviderService=oaiDataProviderService;
    }


    @GetMapping("/dataproviderform")
    public String addForm(ModelMap modelMap)  {
        modelMap.addAttribute("dataproviderForm", new OaiDataProviderDao());
        return "dataproviderform";
    }

    @PostMapping("/dataproviderform")
    public String greetingSubmit(Model model, @ModelAttribute("dataproviderForm")OaiDataProviderDao oaiDataProviderDao) throws IOException {

        System.out.println(oaiDataProviderDao.getName());
        System.out.println(oaiDataProviderDao.getUrl());
        System.out.println(oaiDataProviderDao.getIdentifier());
        oaiDataProviderService.queue(oaiDataProviderDao);
        model.addAttribute("oaiDataProviderDao",oaiDataProviderDao);
        Page<OaiDataProvider>providers= oaiDataProviderRepository.findAll(new PageRequest(0,10));
        model.addAttribute("providers",providers);
        return "dataproviderresult";
    }



}
