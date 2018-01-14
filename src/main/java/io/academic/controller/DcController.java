package io.academic.controller;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.academic.dao.IdentifierDao;
import io.academic.dao.MessageDao;
import io.academic.service.DcProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DcController {

    @Autowired
    DcProcessor dcProcessor;

    @GetMapping(value = "/dc")
    @ExceptionMetered
    @Timed
    public MessageDao add(@RequestBody @Valid IdentifierDao identifierDao) {
        try {
            /**
             * curl -H "Content-Type: application/json" -X POST -d '{"url":"http://domain/?verb=ListRecords&metadataPrefix=oai_dc"}' http://localhost:3002/crawl
             */
            dcProcessor.submit(identifierDao.getOai());
        } catch (InterruptedException ie) {
            throw new RuntimeException("Queue is full", ie);
        }
        return new MessageDao("Queued");
    }
}
