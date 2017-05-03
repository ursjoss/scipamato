package ch.difty.sipamato.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ApplicationPropertiesTest implements ApplicationProperties {

    @Override
    public String getDefaultLocalization() {
        return "dl";
    }

    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return null;
    }

    @Override
    public String getBrand() {
        return "brand";
    }

    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return 10l;
    }

    @Test
    public void assertDefaultLocalizationProperty() {
        assertThat(LOCALIZATION_DEFAULT).isEqualTo("sipamato.localization.default");
    }

    @Test
    public void assertAuthorParserRactoryProperty() {
        assertThat(AUTHOR_PARSER_FACTORY).isEqualTo("sipamato.author.parser");
    }

    @Test
    public void assertBrandProperty() {
        assertThat(BRAND).isEqualTo("sipamato.brand");
    }

    @Test
    public void assertMinimumPaperNumberToBeRecycled() {
        assertThat(PAPER_NUMBER_MIN_TO_RECYCLE).isEqualTo("sipamato.paper.number.minimum-to-be-recycled");
    }

}
