package io.academic.service;


import io.academic.dao.DcDao;
import io.academic.entity.DBArticle;
import io.academic.entity.OaiRecord;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.xml.DcXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;

@Service
public class DcService {


    Logger log = LoggerFactory.getLogger( DcService.class );

    @Autowired
    OaiService oaiService;

    @Autowired
    ArticleService articleService;


    public void parse(DcDao dcDao) throws IOException, TikaException, SAXException {


        OaiRecord oaiRecord = oaiService.getOaiRecord(dcDao.getId());

        log.info(" Creating an article from the oaiRecord with id : {} ", oaiRecord.getId() );

        /// Parser init
        Parser parser = new DcXMLParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();




        parser.parse(new URL(oaiRecord.getDc()).openStream(),handler,metadata,parseContext );


        //passing to article
        DBArticle article = null;

        article.setTitle(metadata.get("title"));


        /// NOT COMPLETE nnew more variables
        articleService.queue(article);

        /// setting state 1
        oaiRecord.setState( 1 );


    }


}
