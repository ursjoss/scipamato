package ch.difty.scipamato.core.logic.parsing;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;

@Configuration
@Profile("!wickettest")
public class AuthorParserConfiguration {

    @NotNull
    @Bean
    public AuthorParserFactory authorParserFactory(final ApplicationCoreProperties appProperties) {
        return AuthorParserFactory.Companion.create(appProperties.getAuthorParserStrategy());
    }
}
