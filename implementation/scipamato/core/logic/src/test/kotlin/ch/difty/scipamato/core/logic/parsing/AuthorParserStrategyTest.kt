package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy.PUBMED
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class AuthorParserStrategyTest {

    @Test
    fun values() {
        assertThat(AuthorParserStrategy.values()).containsExactly(PUBMED)
    }

    @Test
    fun canParsePubmed() {
        assertThat(AuthorParserStrategy.fromProperty("PUBMED", "whatever-key-for-logging-only")).isEqualTo(PUBMED)
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun gettingStrategyByName_withNotExistingName_returnsPubmedStrategy() {
        assertThat(AuthorParserStrategy.fromProperty("ksjdflksjdk", "key")).isEqualTo(PUBMED)
    }
}
