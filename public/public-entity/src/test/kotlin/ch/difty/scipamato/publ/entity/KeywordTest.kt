package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class KeywordTest : PublicEntityTest<Keyword>() {

    override val toString: String
        get() = "Keyword(id=1, keywordId=2, langCode=lc, name=name, searchOverride=n)"

    override fun newEntity(): Keyword = Keyword.builder()
        .id(1)
        .keywordId(2)
        .langCode("lc")
        .name("name")
        .searchOverride("n")
        .build()

    override fun assertSpecificGetters() {
        entity.id shouldBeEqualTo 1
        entity.keywordId shouldBeEqualTo 2
        entity.langCode shouldBeEqualTo "lc"
        entity.name shouldBeEqualTo "name"
        entity.searchOverride shouldBeEqualTo "n"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple()
            .forClass(Keyword::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .verify()
    }

    @Test
    fun displayValue() {
        entity.displayValue shouldBeEqualTo "name"
    }

    @Test
    fun assertEnumFields() {
        Keyword.KeywordFields.values().map { it.fieldName } shouldContainAll
            listOf("id", "keywordId", "langCode", "name", "searchOverride", "displayValue")
    }
}
