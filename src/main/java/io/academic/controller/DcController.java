package io.academic.controller;


import io.academic.dao.DcDao;
import io.academic.dao.MessageDao;
import io.academic.service.ArticleService;
import io.academic.service.DcService;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class DcController {

    @Autowired
    ArticleService articleService;

    @Autowired
    DcService dcService;


//    @PostMapping(value = "/dc")
    @GetMapping(value = "/dc")
    public MessageDao add(@RequestBody @Valid DcDao dcDao) throws TikaException, IOException, SAXException {


        dcService.parse(dcDao);
        return new MessageDao("taken!");
    }


}
