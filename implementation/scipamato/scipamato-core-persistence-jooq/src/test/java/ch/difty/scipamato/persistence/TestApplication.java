package ch.difty.scipamato.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.config.core.AuthorParserStrategy;
import ch.difty.scipamato.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.pubmed.PubmedArticleService;

@SpringBootApplication
@ComponentScan(basePackages = "ch.difty.scipamato")
public class TestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(TestApplication.class).run(args);
    }

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties() {

            @Override
            public String getBuildVersion() {
                return "vxy";
            }

            @Override
            public String getDefaultLocalization() {
                return "de";
            }

            @Override
            public AuthorParserStrategy getAuthorParserStrategy() {
                return AuthorParserStrategy.DEFAULT;
            }

            @Override
            public String getBrand() {
                return "scipamato";
            }

            @Override
            public long getMinimumPaperNumberToBeRecycled() {
                return 10;
            }

            @Override
            public String getPubmedBaseUrl() {
                return "http://pubmed/";
            }

        };
    }

    @Bean
    public PubmedArticleService noopPubmedArticleService() {
        return new PubmedArticleService() {

            @Override
            public Optional<PubmedArticleFacade> getPubmedArticleWithPmid(int pmId) {
                return Optional.empty();
            }

            @Override
            public List<PubmedArticleFacade> extractArticlesFrom(String content) {
                return new ArrayList<>();
            }
        };
    }
}
