package ch.difty.scipamato.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ApplicationPropertiesTest {

    @Test
    public void assertBuildVersionProperty() {
        assertThat(ApplicationProperties.BUILD_VERSION).isEqualTo("build.version");
    }

    @Test
    public void assertDefaultLocalizationProperty() {
        assertThat(ApplicationProperties.LOCALIZATION_DEFAULT).isEqualTo("scipamato.default-localization");
    }

    @Test
    public void assertBrandProperty() {
        assertThat(ApplicationProperties.BRAND).isEqualTo("scipamato.brand");
    }

    @Test
    public void assertPubmedBaseUrl() {
        assertThat(ApplicationProperties.PUBMED_BASE_URL).isEqualTo("scipamato.pubmed-base-url");
    }

}
