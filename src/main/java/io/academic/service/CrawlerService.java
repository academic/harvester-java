package io.academic.service;

import io.academic.dao.CrawlerDao;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.xml.DcXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import org.xml.sax.ContentHandler;

import java.io.IOException;

/**
 * Crawls xmls and sends to data management services
 */
@Service
public class CrawlerService {

    Logger log = LoggerFactory.getLogger( CrawlerService.class );

    public void parse(CrawlerDao crawlerDao) {

        log.info( "URL crawler started href: {}", crawlerDao.getHref() );

        String resumptionToken = null;
        Parser parser = new DcXMLParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();


        try {

            // Step 1: Get url

            Document doc = Jsoup.connect( crawlerDao.getHref() ).get();

            // Step 2: Get records of this page

            Elements records = getRecords(doc);

            // Step 3: Parse DC

            for (Element record : records) {
                log.info( "Record: {}", record.html() );
            }

            // Step 4: Record OAI Records to database

            // Step 5: Get resumptionToken

            // Step 6: Built new url

            resumptionToken = this.getResumptionToken(doc);
            log.info( "URL crawler will try new token: {}", resumptionToken );

            // Step 7: Recall this metod with new link

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getResumptionToken(Document doc) throws IOException {
        return doc.select( "resumptionToken" ).first().ownText();
    }


    private Elements getRecords(Document doc) throws IOException {
        Elements records = doc.select( "record" );
        return  records;
    }


}
