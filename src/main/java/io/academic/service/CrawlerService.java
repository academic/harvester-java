package io.academic.service;

import io.academic.dao.CrawlerDao;
import io.academic.entity.OaiRecord;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Crawls xmls and sends to data management services
 */
@Service
public class CrawlerService {

    Logger log = LoggerFactory.getLogger( CrawlerService.class );

    @Autowired
    OaiService oaiService;

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

            Elements records = getRecords( doc );

            // Step 3: Parse DC

            for (Element record : records) {
                log.info( "Record: {}", record.html() );

                OaiRecord oaiRecord = new OaiRecord();
                oaiRecord.setURL( crawlerDao.getHref() );
                oaiRecord.setToken( resumptionToken );
                try {
                    oaiRecord.setResponseDate( this.stringToDate( this.getResponseDate( doc ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                oaiRecord.setIdentifier( metadata.get( "identifier" ) );
                oaiRecord.setDatestamp( metadata.get( "datestamp" ) );
                oaiRecord.setRecord( metadata.get( "metadata" ) );
                oaiRecord.setState( 0 );

                // Step 4: Record OAI Records to database
                oaiService.queue( oaiRecord );
            }


            // Step 5: Get resumptionToken
            resumptionToken = this.getResumptionToken( doc );

            if (resumptionToken.isEmpty()) {
                log.info( "URL crawler wil stop" );
            } else {
                log.info( "URL crawler will try new token: {}", resumptionToken );
            }


            // Step 6: Built new url


            // Step 7: Recall this metod with new link

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getResponseDate(Document doc) throws IOException {
        return doc.select( "responseDate" ).first().ownText();
    }

    private String getResumptionToken(Document doc) throws IOException {
        return doc.select( "resumptionToken" ).first().ownText();
    }


    private Elements getRecords(Document doc) throws IOException {
        Elements records = doc.select( "record" );
        return records;
    }

    // 2012-12-25T21:50:11Z
    private Date stringToDate(String datetimestring) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mmaZ" );
        return sdf.parse( datetimestring );
    }


}
