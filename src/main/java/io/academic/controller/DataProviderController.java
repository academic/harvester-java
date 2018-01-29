package io.academic.controller;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.academic.dao.IdentifierDao;
import io.academic.dao.MessageDao;
import io.academic.dao.OaiDataProviderDao;
import io.academic.service.DcProcessor;
import io.academic.service.OaiDataProviderProcessor;
import io.academic.service.OaiDataProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DataProviderController {

    @Autowired
    OaiDataProviderService oaiDataProviderService;

    @PostMapping(value = "/dataprovider")
    @ExceptionMetered
    @Timed
    public MessageDao add(@RequestBody @Valid OaiDataProviderDao oaiDataProviderDao) {
        System.out.println(oaiDataProviderDao.getUrl());
        /**
         * curl -H "Content-Type: application/json" -X POST -d '{"url":"http://oaidomain"}' http://localhost:3002/dataprovider
         * eg : curl -H "Content-Type: application/json" -X POST -d '{"url":"http://dergipark.gov.tr/api/public/oai/"}' http://localhost:3002/dataprovider
         */
        oaiDataProviderService.queue(oaiDataProviderDao);
        return new MessageDao("Queued");
    }
}
