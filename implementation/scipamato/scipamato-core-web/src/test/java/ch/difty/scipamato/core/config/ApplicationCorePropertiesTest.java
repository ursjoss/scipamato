package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ApplicationCorePropertiesTest {

    @Test
    public void assertAuthorParserRactoryProperty() {
        assertThat(ApplicationCoreProperties.AUTHOR_PARSER_FACTORY).isEqualTo("scipamato.author-parser");
    }

    @Test
    public void assertMinimumPaperNumberToBeRecycled() {
        assertThat(ApplicationCoreProperties.PAPER_NUMBER_MIN_TO_RECYCLE)
            .isEqualTo("scipamato.paper-number-minimum-to-be-recycled");
    }

}
