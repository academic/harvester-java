package io.academic.service;

import io.academic.dao.OaiDataProviderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class OaiDataProviderProcessor implements Runnable {

    private Logger log = LoggerFactory.getLogger(OaiDataProviderProcessor.class);

    @Autowired
    private OaiDataProviderService oaiDataProviderService;


    @Resource
    @Qualifier("oaiDataProviderQueue")
    private ArrayBlockingQueue<OaiDataProviderDao> oaiQueue;

    public boolean submit(OaiDataProviderDao oai) throws InterruptedException {
        return oaiQueue.offer(oai, 10, TimeUnit.SECONDS);
    }

    public void run() {
        log.info("Starting to run URL Processor");
        while (true) {
            try {
//                String url = oaiQueue.take().getUrl();
                OaiDataProviderDao url = oaiQueue.take();
                oaiDataProviderService.queue(url);
            } catch (InterruptedException e) {
                log.error("Operation interrupted: {}", e.getMessage());
            }
        }
    }

}
