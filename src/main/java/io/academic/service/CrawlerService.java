package io.academic.service;

import io.academic.dao.CrawlerDao;
import io.academic.entity.OaiRecord;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


        try {

            // Step 1: Get url

            Document doc = Jsoup.connect( crawlerDao.getHref() ).get();

            // Step 2: Get records of this page

            Elements records = getRecords( doc );

            // Step 3: Get resumptionToken
            resumptionToken = doc.select( "resumptionToken" ).html();

            // Step 4: Parse DC

            for (Element record : records) {
                log.info( "Record: {}", crawlerDao.getHref() );

                OaiRecord oaiRecord = new OaiRecord();
                oaiRecord.setSpec( record.select( "setSpec" ).html() );
                oaiRecord.setToken( resumptionToken );
                try {
                    oaiRecord.setResponseDate( this.stringToDate( doc.select( "responseDate" ).html() ) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                oaiRecord.setIdentifier( record.select( "identifier" ).html() );
                oaiRecord.setDc( record.select( "metadata" ).html() );
                oaiRecord.setDatestamp( record.select( "datestamp" ).html() );
                oaiRecord.setState( 0 );

                // Step 5: Record OAI Records to database
                oaiService.queue( oaiRecord );
            }

            if (resumptionToken.isEmpty()) {
                log.info( "URL crawler wil stop" );
            } else {
                log.info( "URL crawler will try new token: {}", resumptionToken );

                // Step 6: Built new url

                String newoai = crawlerDao.getHref() + "&resumptionToken=" + resumptionToken;

                log.info( "New Token with URL: {}", newoai );


                // Step 7: Recall this metod with new link


                CrawlerService crawlerService = new CrawlerService();
                CrawlerDao newCrawlerDao = new CrawlerDao();
                newCrawlerDao.setHref( newoai );
                crawlerService.parse( newCrawlerDao );

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private Elements getRecords(Document doc) throws IOException {
        return doc.select( "record" );
    }

    private Date stringToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
        return sdf.parse( str );
    }


}
