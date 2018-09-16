package ch.difty.scipamato.core.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.FrozenDateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.core.pubmed.PubmedArticleService;

@SpringBootApplication
@ComponentScan(basePackages = "ch.difty.scipamato")
public class TestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(TestApplication.class)
            .run(args);
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
            public String getBrand() {
                return "scipamato";
            }

            @Override
            public String getTitleOrBrand() {
                return getBrand();
            }

            @Override
            public String getPubmedBaseUrl() {
                return "http://pubmed/";
            }

            @Override
            public Integer getRedirectFromPort() {
                return 8080;
            }

            @Override
            public int getMultiSelectBoxActionBoxWithMoreEntriesThan() {
                return 4;
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
            public Optional<PubmedArticleFacade> getPubmedArticleWithPmidAndApiKey(final int pmId,
                final String apiKey) {
                return Optional.empty();
            }

            @Override
            public List<PubmedArticleFacade> extractArticlesFrom(String content) {
                return new ArrayList<>();
            }
        };
    }

    @Bean
    @Primary
    public DateTimeService dateTimeService() {
        return new FrozenDateTimeService();
    }
}
