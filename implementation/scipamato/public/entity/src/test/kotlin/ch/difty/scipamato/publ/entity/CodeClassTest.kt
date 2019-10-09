package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CodeClassTest : PublicEntityTest<CodeClass>() {

    override val toString: String
        get() = "CodeClass(codeClassId=1, langCode=lc, name=name, description=description)"

    override fun newEntity(): CodeClass = CodeClass.builder()
        .codeClassId(1)
        .langCode("lc")
        .name("name")
        .description("description")
        .build()

    override fun assertSpecificGetters() {
        assertThat(entity.codeClassId).isEqualTo(1)
        assertThat(entity.langCode).isEqualTo("lc")
        assertThat(entity.name).isEqualTo("name")
        assertThat(entity.description).isEqualTo("description")
    }

    override fun verifyEquals() {
        EqualsVerifier
            .forClass(CodeClass::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(CodeClass.CodeClassFields.values().map { it.fieldName })
            .containsExactly("codeClassId", "langCode", "name", "description")
    }
}
