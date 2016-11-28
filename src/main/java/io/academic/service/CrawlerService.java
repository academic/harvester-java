package io.academic.service;

import io.academic.dao.CrawlerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Crawls xmls and sends to data management services
 */
@Service
public class CrawlerService {

    Logger log = LoggerFactory.getLogger( CrawlerService.class );


    public void parse(CrawlerDao crawlerDao) {

        log.info( "URL crawler started", crawlerDao.getHref() );


        /**
         * Parse url
         */

    }


}
