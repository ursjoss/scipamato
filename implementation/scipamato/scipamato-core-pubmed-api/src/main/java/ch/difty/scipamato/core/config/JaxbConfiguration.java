package ch.difty.scipamato.core.config;

import feign.Feign;
import feign.Logger;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.scipamato.core.pubmed.PubMed;

@Configuration
public class JaxbConfiguration {

    private static final String PACKAGE = "ch.difty.scipamato.core.pubmed";

    static final String PUBMED_URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";

    private final JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
        .withMarshallerJAXBEncoding("UTF-8")
        .build();

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
        return Feign
            .builder()
            .client(new OkHttpClient())
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .decoder(new JAXBDecoder(jaxbFactory))
            .target(PubMed.class, PUBMED_URL);
    }
}
