package io.academic.controller;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.academic.dao.MessageDao;
import io.academic.dao.OaiDao;
import io.academic.entity.OaiRecord;
import io.academic.entity.OaiRecordRepository;
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

    @PostMapping(value = "/oai")
    @ExceptionMetered
    @Timed
    public MessageDao add(@RequestBody @Valid OaiDao oaiDao) {
        try {
            /**
             * curl -H "Content-Type: application/json" -X POST -d '{"url":"http://domain/?verb=ListRecords&metadataPrefix=oai_dc"}' http://localhost:3002/crawl
             */


            oaiRecords = oaiRecordRepository.findById(oaiDao.getId());

            String dc = "{\"any\":{\"name\":\"{http://www.openarchives.org/OAI/2.0/oai_dc/}dc\",\"declaredType\":\"org.openarchives.oai._2_0.oai_dc.OaiDcType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"titleOrCreatorOrSubject\":[{\"name\":\"{http://purl.org/dc/elements/1.1/}title\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"Yeni Sayıda...\",\"lang\":\"en-US\"},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}creator\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"\\n                                Balcı, Sarp                            \",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}publisher\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"Mülkiyeliler Birliği Genel Merkezi\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}publisher\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"Mülkiye Alumni Association\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}date\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"2012-12-25T21:50:11Z\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}type\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"info:eu-repo/semantics/article\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}format\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"application/pdf\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}identifier\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"http://dergipark.gov.tr/mulkiye/issue/1/2\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}identifier\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}source\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"\\n                                \\n                                                                    Volume: 36, Issue: 4\\n                                                                                                    1-6\\n                                                                \\n                            \",\"lang\":\"en-US\"},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}source\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"1305-9971\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}language\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"tr\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false},{\"name\":\"{http://purl.org/dc/elements/1.1/}relation\",\"declaredType\":\"org.purl.dc.elements._1.ElementType\",\"scope\":\"javax.xml.bind.JAXBElement$GlobalScope\",\"value\":{\"value\":\"http://dergipark.gov.tr/download/article-file/1\",\"lang\":null},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false}]},\"nil\":false,\"globalScope\":true,\"typeSubstituted\":false}}";
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(dc);
            if (null != jsonObject.get("any")) {
                JSONObject j = (JSONObject) jsonObject.get("any");
                JSONObject jx = (JSONObject) j.get("value");
                JSONArray jy = (JSONArray) jx.get("titleOrCreatorOrSubject");


                for (int i = 0; i < jy.size(); i++) {

                    JSONObject jyx = (JSONObject) jy.get(i);
                    JSONObject jyz = (JSONObject) jyx.get("value");
                    String jyk = (String) jyz.get("value");

                    System.out.println("The " + i + " element of the array: " + jyk.trim());
                }


            }


            System.out.println("a");

        } catch (Exception ie) {
            throw new RuntimeException("Queue is full", ie);
        }
        return new MessageDao("Queued");
    }
}
