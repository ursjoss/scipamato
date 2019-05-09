package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.publ.ScipamatoPublicApplication;

/**
 * Note, this test class currently derives the configured values from
 * <literal>application.properties</literal>.
 *
 * @author u.joss
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScipamatoPublicApplication.class)
class ScipamatoPublicPropertiesIntegrationTest {

    @Autowired
    ApplicationPublicProperties appProperties;

    @Test
    void gettingBuildVersion() {
        assertThat(appProperties.getBuildVersion()).matches("\\d+\\.\\d+\\.\\d+.*");
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
    void assertPubmedBaseUrl() {
        assertThat(appProperties.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    void assertPresenceOfCommercialFont() {
        assertThat(appProperties.isCommercialFontPresent()).isEqualTo(false);
    }

    @Test
    void assertNavbarVisibleByDefault() {
        assertThat(appProperties.isNavbarVisibleByDefault()).isEqualTo(false);
    }

    @Test
    void assertCmsUrlSearchPage() {
        assertThat(appProperties.getCmsUrlSearchPage()).isEqualTo("http://localhost:8081/");
    }

    @Test
    void assertCmsUrlNewStudyPage() {
        assertThat(appProperties.getCmsUrlNewStudyPage()).isEqualTo("http://localhost:8081/new-studies");
    }

    @Test
    void authorsAbbreviatedMaxLength() {
        assertThat(appProperties.getAuthorsAbbreviatedMaxLength()).isEqualTo(60);
    }

    @Test
    void isResponsiveIframeSupportEnabled() {
        assertThat(appProperties.isResponsiveIframeSupportEnabled()).isFalse();
    }

    @Test
    void numberOfPreviousNewslettersInArchive() {
        assertThat(appProperties.getNumberOfPreviousNewslettersInArchive()).isEqualTo(14);
    }

}
