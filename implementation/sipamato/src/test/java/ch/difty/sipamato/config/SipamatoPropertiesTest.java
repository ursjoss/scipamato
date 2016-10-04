package ch.difty.sipamato.config;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class SipamatoPropertiesTest {

    SipamatoProperties sp = new SipamatoProperties("DEFAULT", "15");

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
        SipamatoProperties sp = new SipamatoProperties("DEFAULT", "0");
        assertThat(sp.getAutoSaveIntervalInSeconds()).isEqualTo(0);
        assertThat(sp.isAutoSavingEnabled()).isFalse();
    }
}
