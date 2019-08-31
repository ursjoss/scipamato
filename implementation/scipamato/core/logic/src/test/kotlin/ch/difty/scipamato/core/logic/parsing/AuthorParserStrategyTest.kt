package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy.PUBMED
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

private const val PROPERTY_KEY = "propertyKey"

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

    private val values = arrayOf(PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT)

    private enum class PropertyTestEnum { VAL1, VAL2, DEFAULT }

    @Test
    fun fromProperty_witValues_returnsValue() {
        assertThat("VAL2".asProperty(values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
                PropertyTestEnum.VAL2)
    }

    @Test
    fun fromProperty_withoutValues_returnsDefault() {
        assertThat("VAL2".asProperty(arrayOf(), PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(PropertyTestEnum.DEFAULT)
    }
}
