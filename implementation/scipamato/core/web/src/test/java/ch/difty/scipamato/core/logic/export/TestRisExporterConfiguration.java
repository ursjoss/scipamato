package ch.difty.scipamato.core.logic.export;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory;
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy;

@Configuration
public class TestRisExporterConfiguration {

    @Bean
    @Primary
    public RisAdapterFactory risAdapterFactory() {
        return RisAdapterFactory.Companion.create(RisExporterStrategy.DEFAULT);
    }
}
