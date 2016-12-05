package io.academic.service;

import org.openarchives.oai._2.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Component
@Scope("prototype")
public class RecordTypeProcessor implements Runnable {

    private Logger log = LoggerFactory.getLogger(RecordTypeProcessor.class);

    @Autowired
    private OaiService oaiService;

    @Resource
    @Qualifier("recordListQueue")
    private ArrayBlockingQueue<List<RecordType>> recordTypeListQueue;

    public void run() {
        log.info("Starting to run Record Type Processor");
        while (true) {
            try {
                List<RecordType> recordTypes = recordTypeListQueue.take();
                oaiService.saveRecords(recordTypes);
            } catch (InterruptedException e) {
                log.error("Operation interrupted: {}", e.getMessage());
            }
        }
    }

}
