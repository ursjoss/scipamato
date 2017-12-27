package ch.difty.scipamato.core.config;

import static ch.difty.scipamato.core.config.AuthorParserStrategy.PUBMED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AuthorParserStrategyTest {

    @Test
    public void values() {
        assertThat(AuthorParserStrategy.values()).containsExactly(PUBMED);
    }

    @Test
    public void canParsePubmed() {
        assertThat(AuthorParserStrategy.fromProperty("PUBMED")).isEqualTo(PUBMED);
    }

    @Test
    public void gettingStrategyByName_withNotExistingName_returnsPubmedStrategy() {
        assertThat(AuthorParserStrategy.fromProperty("ksjdflksjdk")).isEqualTo(PUBMED);
    }

    @Test
    public void gettingStrategyByName_withNullName_returnsPubmedStrategy() {
        assertThat(AuthorParserStrategy.fromProperty(null)).isEqualTo(PUBMED);
    }
}
