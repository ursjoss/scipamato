package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
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
        entity.codeClassId shouldBeEqualTo 1
        entity.langCode shouldBeEqualTo "lc"
        entity.name shouldBeEqualTo "name"
        entity.description shouldBeEqualTo "description"
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
        CodeClass.CodeClassFields.values().map { it.fieldName } shouldContainAll
            listOf("codeClassId", "langCode", "name", "description")
    }
}
