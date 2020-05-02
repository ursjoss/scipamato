package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
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
        entity.codeClassId shouldBeEqualTo 1
        entity.code shouldBeEqualTo "code"
        entity.langCode shouldBeEqualTo "lc"
        entity.name shouldBeEqualTo "name"
        entity.comment shouldBeEqualTo "comment"
        entity.sort shouldBeEqualTo 3
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
        entity.displayValue shouldBeEqualTo "name"
    }

    @Test
    fun assertEnumFields() {
        Code.CodeFields.values().map { it.fieldName } shouldContainAll
            listOf("codeClassId", "code", "langCode", "name", "comment", "sort", "displayValue")
    }
}
