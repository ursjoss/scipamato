package ch.difty.scipamato.config;

import static ch.difty.scipamato.config.AuthorParserStrategy.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AuthorParserStrategyTest {

    @Test
    public void values() {
        assertThat(AuthorParserStrategy.values()).containsExactly(DEFAULT);
    }

    @Test
    public void canParseDefault() {
        assertThat(AuthorParserStrategy.fromProperty("DEFAULT")).isEqualTo(DEFAULT);
    }

    @Test
    public void gettingStrategyByName_withNotExistingName_returnsDefaultStrategy() {
        assertThat(AuthorParserStrategy.fromProperty("ksjdflksjdk")).isEqualTo(DEFAULT);
    }

    @Test
    public void gettingStrategyByName_withNullName_returnsDefaultStrategy() {
        assertThat(AuthorParserStrategy.fromProperty(null)).isEqualTo(DEFAULT);
    }
}
