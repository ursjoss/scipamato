package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo

internal class CodeClassTest : PublicDbEntityTest<CodeClass>() {

    override fun newEntity(): CodeClass = CodeClass(
        codeClassId = 1,
        langCode = "lc",
        name = "name",
        description = "description",
    )

    override fun assertSpecificGetters() {
        entity.codeClassId shouldBeEqualTo 1
        entity.langCode shouldBeEqualTo "lc"
        entity.name shouldBeEqualTo "name"
        entity.description shouldBeEqualTo "description"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple().forClass(CodeClass::class.java).verify()
    }
}
