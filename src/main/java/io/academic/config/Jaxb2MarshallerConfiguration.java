package io.academic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Jaxb2MarshallerConfiguration {

    @Bean
    Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setSchemas(new ClassPathResource("xsd/OAI-PMH.xsd"), new ClassPathResource("xsd/oai_dc.xsd"));
        jaxb2Marshaller.setPackagesToScan("org.openarchives.oai._2", "org.openarchives.oai._2_0.oai_dc","org.purl.dc.elements._1");

        return jaxb2Marshaller;
    }
}
