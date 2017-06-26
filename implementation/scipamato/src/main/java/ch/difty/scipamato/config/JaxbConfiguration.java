package ch.difty.scipamato.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.scipamato.pubmed.PubMed;
import feign.Feign;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.slf4j.Slf4jLogger;

@Configuration
public class JaxbConfiguration {

    private static final String PACKAGE = "ch.difty.scipamato.pubmed";

    private final JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();

    @Bean
    public Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(PACKAGE);
        return marshaller;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setSupportDtd(true);
        unmarshaller.setPackagesToScan(PACKAGE);
        return unmarshaller;
    }

    @Bean
    public PubMed pubMed() {
        return Feign.builder().logger(new Slf4jLogger()).decoder(new JAXBDecoder(jaxbFactory)).target(PubMed.class, PubMed.URL);
    }
}
