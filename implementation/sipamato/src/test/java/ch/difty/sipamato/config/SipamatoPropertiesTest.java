package ch.difty.sipamato.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SipamatoPropertiesTest {

    private final SipamatoProperties sp = new SipamatoProperties("de", "DEFAULT", "SiPaMaTo");

    @Test
    public void canResolveDefaultLocalization() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void canResolveAuthorParserStrategy() {
        assertThat(sp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.DEFAULT);
    }

    @Test
    public void canResolveBrand() {
        assertThat(sp.getBrand()).isEqualTo("SiPaMaTo");
    }
}
