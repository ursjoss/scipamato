package ch.difty.scipamato.comon.config.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.config.core.ApplicationProperties;

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
    public void assertAuthorParserRactoryProperty() {
        assertThat(ApplicationProperties.AUTHOR_PARSER_FACTORY).isEqualTo("scipamato.author-parser");
    }

    @Test
    public void assertBrandProperty() {
        assertThat(ApplicationProperties.BRAND).isEqualTo("scipamato.brand");
    }

    @Test
    public void assertMinimumPaperNumberToBeRecycled() {
        assertThat(ApplicationProperties.PAPER_NUMBER_MIN_TO_RECYCLE)
            .isEqualTo("scipamato.paper-number-minimum-to-be-recycled");
    }

    @Test
    public void assertPubmedBaseUrl() {
        assertThat(ApplicationProperties.PUBMED_BASE_URL).isEqualTo("scipamato.pubmed-base-url");
    }

}
