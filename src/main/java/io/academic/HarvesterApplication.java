package io.academic;

import eu.luminis.elastic.RestClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Import(RestClientConfig.class)
public class HarvesterApplication {

    public static void main(String[] args) {



        ConfigurableApplicationContext context = SpringApplication.run(HarvesterApplication.class, args);

    }


}
