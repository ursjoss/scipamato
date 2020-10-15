package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class KeywordTest : PublicDbEntityTest<Keyword>() {

    override fun newEntity(): Keyword = Keyword(
        id = 1,
        keywordId = 2,
        langCode = "lc",
        name = "name",
        searchOverride = "n",
    )

    override fun assertSpecificGetters() {
        entity.id shouldBeEqualTo 1
        entity.keywordId shouldBeEqualTo 2
        entity.langCode shouldBeEqualTo "lc"
        entity.name shouldBeEqualTo "name"
        entity.searchOverride shouldBeEqualTo "n"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple().forClass(Keyword::class.java).verify()
    }

    @Test
    fun displayValue() {
        entity.displayValue shouldBeEqualTo "name"
    }
}
