package ch.difty.scipamato.core.logic.exporting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;

@Configuration
@Profile("!wickettest")
public class RisExporterConfiguration {

    @Bean
    public RisAdapterFactory risAdapterFactory(final ApplicationCoreProperties appProperties) {
        return RisAdapterFactory.Companion.create(appProperties.getRisExporterStrategy());
    }
}