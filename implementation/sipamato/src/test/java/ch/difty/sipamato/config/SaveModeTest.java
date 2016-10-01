package ch.difty.sipamato.config;

import static ch.difty.sipamato.config.SaveMode.AUTO;
import static ch.difty.sipamato.config.SaveMode.SUBMIT;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class SaveModeTest {

    @Test
    public void values() {
        assertThat(SaveMode.values()).containsExactly(AUTO, SUBMIT);
    }

    @Test
    public void canParseAuto() {
        assertThat(SaveMode.fromProperty("AUTO")).isEqualTo(AUTO);
    }

    @Test
    public void canParseAutoCaseInsensitive() {
        assertThat(SaveMode.fromProperty("auTo")).isEqualTo(AUTO);
    }

    @Test
    public void gettingStrategyByName_withNotExistingName_returnsDefaultStrategy() {
        assertThat(SaveMode.fromProperty("ksjdflksjdk")).isEqualTo(SUBMIT);
    }

    @Test
    public void gettingStrategyByName_withNullName_returnsDefaultStrategy() {
        assertThat(SaveMode.fromProperty(null)).isEqualTo(SUBMIT);
    }
}
