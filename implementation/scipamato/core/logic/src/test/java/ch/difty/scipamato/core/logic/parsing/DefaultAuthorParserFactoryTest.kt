package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.common.NullArgumentException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultAuthorParserFactoryTest {

    private lateinit var factory: AuthorParserFactory

    @BeforeEach
    fun setUp() {
        factory = DefaultAuthorParserFactory(AuthorParserStrategy.PUBMED)
    }

    @Test
    fun degenerateConstruction() {
        Assertions.assertThrows(NullArgumentException::class.java) { DefaultAuthorParserFactory(null) }
    }

    @Test
    fun creatingParser_withNullAuthorString_throws() {
        Assertions.assertThrows(NullArgumentException::class.java) { factory.createParser(null) }
    }

    @Test
    fun cratingParser_withNoSetting_usesDefaultAuthorParser() {
        val parser = factory.createParser("Turner MC.")
        assertThat(parser).isInstanceOf(PubmedAuthorParser::class.java)
    }

}