package ch.difty.sipamato.config;

import static ch.difty.sipamato.config.AuthorParserStrategies.DEFAULT;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class AuthorParserStrategiesTest {

    @Test
    public void values() {
        assertThat(AuthorParserStrategies.values()).containsExactly(DEFAULT);
    }

    @Test
    public void canParseDefault() {
        assertThat(AuthorParserStrategies.fromProperty("DEFAULT")).isEqualTo(DEFAULT);
    }

    @Test
    public void gettingStrategyByName_withNotExistingName_returnsDefaultStrategy() {
        assertThat(AuthorParserStrategies.fromProperty("ksjdflksjdk")).isEqualTo(DEFAULT);
    }

    @Test
    public void gettingStrategyByName_withNullName_returnsDefaultStrategy() {
        assertThat(AuthorParserStrategies.fromProperty(null)).isEqualTo(DEFAULT);
    }
}
