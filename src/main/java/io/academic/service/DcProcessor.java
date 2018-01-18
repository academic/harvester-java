package io.academic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class DcProcessor implements Runnable {

    private Logger log = LoggerFactory.getLogger(DcProcessor.class);

    @Autowired
    private UrlFetchService urlFetchService;

    @Resource
    @Qualifier("urlQueue")
    private ArrayBlockingQueue<String> urlQueue;

    public boolean submit(String url) throws InterruptedException {
        return urlQueue.offer(url, 10, TimeUnit.SECONDS);
    }

    public void run() {
        log.info("Starting to run URL Processor");
        while (true) {
            try {
                String url = urlQueue.take();
                urlFetchService.fetchListRecords(url);
            } catch (InterruptedException e) {
                log.error("Operation interrupted: {}", e.getMessage());
            } catch (URISyntaxException e) {
                log.error("URL error: {}", e.getMessage());
            } catch (IOException e) {
                log.error("An exception occured while fetching url: {}", e.getMessage());
            }
        }
    }

}
