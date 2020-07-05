package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy.PUBMED
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class AuthorParserStrategyTest {

    @Test
    fun values() {
        AuthorParserStrategy.values() shouldContainAll listOf(PUBMED)
    }

    @Test
    fun canParsePubmed() {
        AuthorParserStrategy.fromProperty("PUBMED", "whatever-key-for-logging-only") shouldBeEqualTo PUBMED
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun gettingStrategyByName_withNotExistingName_returnsPubmedStrategy() {
        AuthorParserStrategy.fromProperty("ksjdflksjdk", "key") shouldBeEqualTo PUBMED
    }
}
