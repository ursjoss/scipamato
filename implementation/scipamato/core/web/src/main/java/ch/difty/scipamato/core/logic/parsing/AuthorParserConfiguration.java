package ch.difty.scipamato.core.logic.parsing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;

@Configuration
public class AuthorParserConfiguration {

    @Bean
    public AuthorParserFactory authorParserFactory(final ApplicationCoreProperties appProperties) {
        return new DefaultAuthorParserFactory(appProperties.getAuthorParserStrategy());
    }
}
