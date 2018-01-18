package io.academic.controller;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.academic.dao.MessageDao;
import io.academic.dao.OaiDao;
import io.academic.entity.OaiRecord;
import io.academic.entity.OaiRecordRepository;
import io.academic.service.DcParseService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OaiController {

    @Autowired
    private OaiRecordRepository oaiRecordRepository;

    private List<OaiRecord> oaiRecords;

    @Autowired
    private DcParseService dcParseService;

    @PostMapping(value = "/oai")
    @ExceptionMetered
    @Timed
    public MessageDao add(@RequestBody @Valid OaiDao oaiDao) {
        try {
            /**
             * curl -H "Content-Type: application/json" -X POST -d '{"id":"id of the wanted oai"}' http://localhost:3002/oai
             */


            oaiRecords = oaiRecordRepository.findById(oaiDao.getId());

//            String dc = "{\"any\":{\"name\":\"{http://www.openarchives.org/OAI/2.0/oai_dc/}dc\",\"declaredType\":\"org.openarchives.oai._2_0.oai_dc.OaiDcType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"titleOrCreatorOrSubject\":[{\"name\":\"{http://purl.org/dc/elements/1.1/}title\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"Yeni Sayıda...\",\"lang\":\"en-US\"},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}creator\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"\\n                                Balcı, Sarp                            \",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}publisher\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"Mülkiyeliler Birliği Genel Merkezi\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}publisher\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"Mülkiye Alumni Association\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}date\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"2012-12-25T21:50:11Z\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}type\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"info:eu-repo/semantics/article\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}format\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"application/pdf\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}identifier\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"http://dergipark.gov.tr/mulkiye/issue/1/2\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}identifier\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}source\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"\\n                                \\n                                                                    Volume: 36, Issue: 4\\n                                                                                                    1-6\\n                                                                \\n                            \",\"lang\":\"en-US\"},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}source\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"1305-9971\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}language\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"tr\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}relation\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"http://dergipark.gov.tr/download/article-file/1\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false}]},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false}}";
            String dc="";
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(oaiRecords.get(0).getDc());
            dc = dcParseService.parseRecordString(jsonObject);

            System.out.println(dc);



        } catch (Exception ie) {
            throw new RuntimeException("Queue is full", ie);
        }
        return new MessageDao("Queued");
    }
}
