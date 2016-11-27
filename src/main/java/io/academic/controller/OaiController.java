package io.academic.controller;

import io.academic.dao.CreateOaiRecordDao;
import io.academic.dao.MessageDao;
import io.academic.entity.OaiRecord;
import io.academic.service.OaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OaiController {

    @Autowired
    OaiService oaiService;

    @PostMapping(value = "/CrawOai")
    public MessageDao add(@RequestBody @Valid CreateOaiRecordDao createOaiRecordDao) {
        oaiService.queue(new OaiRecord(createOaiRecordDao));
        return new MessageDao("Queued!");
    }


}
