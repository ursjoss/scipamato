package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(entity.id).isEqualTo(1)
        assertThat(entity.keywordId).isEqualTo(2)
        assertThat(entity.langCode).isEqualTo("lc")
        assertThat(entity.name).isEqualTo("name")
        assertThat(entity.searchOverride).isEqualTo("n")
    }

    override fun verifyEquals() {
        EqualsVerifier.forClass(Keyword::class.java)
                .withRedefinedSuperclass()
                .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    @Test
    fun displayValue() {
        assertThat(entity.displayValue).isEqualTo("name")
    }

    @Test
    fun assertEnumFields() {
        assertThat(Keyword.KeywordFields.values().map { it.fieldName }).containsExactly("id", "keywordId", "langCode", "name", "searchOverride", "displayValue")
    }
}
