package io.academic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@Service
public class ProcessorService {

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private RecordTypeProcessor recordTypeProcessor;

    @Autowired
    private UrlProcessor urlProcessor;

    @PostConstruct
    public void startProcessors() {
        taskExecutor.execute(recordTypeProcessor);
        taskExecutor.execute(urlProcessor);
    }

    public boolean submitUrl(String url) throws InterruptedException {
        return urlProcessor.submit(url);
    }
}
