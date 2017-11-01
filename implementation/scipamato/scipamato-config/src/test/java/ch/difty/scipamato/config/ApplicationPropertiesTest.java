package ch.difty.scipamato.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ApplicationPropertiesTest {

    @Test
    public void assertBuildVersionProperty() {
        assertThat(ApplicationProperties.BUILD_VERSION).isEqualTo("build.version");
    }

    @Test
    public void assertDefaultLocalizationProperty() {
        assertThat(ApplicationProperties.LOCALIZATION_DEFAULT).isEqualTo("scipamato.localization.default");
    }

    @Test
    public void assertAuthorParserRactoryProperty() {
        assertThat(ApplicationProperties.AUTHOR_PARSER_FACTORY).isEqualTo("scipamato.author.parser");
    }

    @Test
    public void assertBrandProperty() {
        assertThat(ApplicationProperties.BRAND).isEqualTo("scipamato.brand");
    }

    @Test
    public void assertMinimumPaperNumberToBeRecycled() {
        assertThat(ApplicationProperties.PAPER_NUMBER_MIN_TO_RECYCLE).isEqualTo("scipamato.paper.number.minimum-to-be-recycled");
    }

}
