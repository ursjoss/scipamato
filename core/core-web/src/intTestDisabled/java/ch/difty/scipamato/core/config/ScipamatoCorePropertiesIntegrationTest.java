package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.difty.scipamato.core.ScipamatoCoreApplication;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

/**
 * Note, this test class currently derives the configured values from
 * application.properties.
 *
 * @author u.joss
 */
@SpringBootTest(classes = ScipamatoCoreApplication.class)
class ScipamatoCorePropertiesIntegrationTest {

    @Autowired
    private ApplicationCoreProperties appProperties;

    @Test
    void gettingBuildVersion() {
        assertThat(appProperties.getBuildVersion()).matches("\\d+\\.\\d+\\.\\d+.*");
    }

    @Test
    void gettingDefaultStrategy() {
        assertThat(appProperties.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.PUBMED);
    }

    @Test
    void assertDefaultLocalization() {
        assertThat(appProperties.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    void assertBrand() {
        assertThat(appProperties.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    void assertMinimumPaperNumberToBeRecycled() {
        assertThat(appProperties.getMinimumPaperNumberToBeRecycled()).isEqualTo(8);
    }

    @Test
    void assertPubmedBaseUrl() {
        assertThat(appProperties.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }
}
