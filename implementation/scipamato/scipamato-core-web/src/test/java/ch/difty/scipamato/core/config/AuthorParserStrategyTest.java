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
        assertThat(AuthorParserStrategy.fromProperty("PUBMED", "whatever-key-for-logging-only")).isEqualTo(PUBMED);
    }

    @Test
    public void gettingStrategyByName_withNotExistingName_returnsPubmedStrategy() {
        assertThat(AuthorParserStrategy.fromProperty("ksjdflksjdk", "key")).isEqualTo(PUBMED);
    }

    @Test
    public void gettingStrategyByName_withNullName_returnsPubmedStrategy() {
        assertThat(AuthorParserStrategy.fromProperty(null, "key")).isEqualTo(PUBMED);
    }
}
