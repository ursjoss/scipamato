package ch.difty.scipamato.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.ScipamatoPublicApplication;
import ch.difty.scipamato.config.core.ApplicationProperties;

/**
 * Note,  this test class currently derives the configured values from application.properties.
 *
 * @author u.joss
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScipamatoPublicApplication.class)
public class ScipamatoPropertiesIntegrationTest {

    @Autowired
    public ApplicationProperties appProperties;

    @Test
    public void gettingBuildVersion() {
        assertThat(appProperties.getBuildVersion()).matches("\\d+\\.\\d+\\.\\d+.*");
    }

    @Test
    public void gettingDefaultStrategy() {
        assertThat(appProperties.getAuthorParserStrategy()).isNull();
    }

    @Test
    public void assertDefaultlocalization() {
        assertThat(appProperties.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void assertBrand() {
        assertThat(appProperties.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    public void assertMinimumPaperNumberToBeRecycled() {
        assertThat(appProperties.getMinimumPaperNumberToBeRecycled()).isEqualTo(0);
    }

    @Test
    public void assertPubmedBaseUrl() {
        assertThat(appProperties.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }
}
