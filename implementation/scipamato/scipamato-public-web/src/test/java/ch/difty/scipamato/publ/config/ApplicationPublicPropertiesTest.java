package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ApplicationPublicPropertiesTest {

    @Test
    public void assertAuthorParserRactoryProperty() {
        assertThat(ApplicationPublicProperties.COMMERCIAL_FONT_PRESENT).isEqualTo("scipamato.commercial-font-present");
    }
}
