package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class CodeTest : PublicDbEntityTest<Code>() {

    override fun newEntity(): Code = Code(
        codeClassId = 1,
        code = "code",
        langCode = "lc",
        name = "name",
        comment = "comment",
        sort = 3,
    )

    override fun assertSpecificGetters() {
        entity.codeClassId shouldBeEqualTo 1
        entity.code shouldBeEqualTo "code"
        entity.langCode shouldBeEqualTo "lc"
        entity.name shouldBeEqualTo "name"
        entity.comment shouldBeEqualTo "comment"
        entity.sort shouldBeEqualTo 3
    }

    override fun verifyEquals() {
        EqualsVerifier.simple().forClass(Code::class.java).verify()
    }

    @Test
    fun displayValue() {
        entity.displayValue shouldBeEqualTo "name"
    }
}
