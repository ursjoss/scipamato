package ch.difty.sipamato.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SipamatoPropertiesTest {

    private final SipamatoProperties sp = new SipamatoProperties("de", "DEFAULT", "15", "SiPaMaTo");

    @Test
    public void canResolveDefaultLocalization() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void canResolveAuthorParserStrategy() {
        assertThat(sp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.DEFAULT);
    }

    @Test
    public void withAutoSaveIntervalGreaterThan0() {
        assertThat(sp.getAutoSaveIntervalInSeconds()).isEqualTo(15);
        assertThat(sp.isAutoSavingEnabled()).isTrue();
    }

    @Test
    public void withAutoSaveIntervalEqual0() {
        SipamatoProperties sp = new SipamatoProperties("xy", "DEFAULT", "0", "SiPaMaTo");
        assertThat(sp.getAutoSaveIntervalInSeconds()).isEqualTo(0);
        assertThat(sp.isAutoSavingEnabled()).isFalse();
    }

    @Test
    public void canResolveBrand() {
        assertThat(sp.getBrand()).isEqualTo("SiPaMaTo");
    }
}
