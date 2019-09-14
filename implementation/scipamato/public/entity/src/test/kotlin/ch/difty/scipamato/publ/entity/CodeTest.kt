package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CodeTest : PublicEntityTest<Code>() {

    override val toString: String
        get() = "Code(codeClassId=1, code=code, langCode=lc, name=name, comment=comment, sort=3)"

    override fun newEntity(): Code = Code.builder()
            .codeClassId(1)
            .code("code")
            .langCode("lc")
            .name("name")
            .comment("comment")
            .sort(3)
            .build()

    override fun assertSpecificGetters() {
        assertThat(entity.codeClassId).isEqualTo(1)
        assertThat(entity.code).isEqualTo("code")
        assertThat(entity.langCode).isEqualTo("lc")
        assertThat(entity.name).isEqualTo("name")
        assertThat(entity.comment).isEqualTo("comment")
        assertThat(entity.sort).isEqualTo(3)
    }

    override fun verifyEquals() {
        EqualsVerifier.forClass(Code::class.java)
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
        assertThat(Code.CodeFields.values().map { it.fieldName }).containsExactly("codeClassId", "code", "langCode", "name", "comment", "sort", "displayValue")
    }

}
