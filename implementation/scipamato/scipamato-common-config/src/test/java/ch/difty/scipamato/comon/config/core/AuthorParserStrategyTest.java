package ch.difty.scipamato.comon.config.core;

import static ch.difty.scipamato.common.config.core.AuthorParserStrategy.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

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
