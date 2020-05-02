package ch.difty.scipamato.core.entity.code

import ch.difty.scipamato.core.entity.CodeClass
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class CodeFilterTest {

    private val f = CodeFilter()
    private val cc = CodeClass(1, "cc1", "")

    @Test
    fun getAndSet() {
        f.codeClass = cc
        f.nameMask = "nameMask"
        f.commentMask = "commentMask"
        f.internal = true

        f.codeClass shouldBeEqualTo cc
        f.nameMask shouldBeEqualTo "nameMask"
        f.commentMask shouldBeEqualTo "commentMask"
        f.internal.shouldBeTrue()

        f.toString() shouldBeEqualTo
            "CodeFilter(codeClass=CodeClass[id=1], nameMask=nameMask, commentMask=commentMask, internal=true)"
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(CodeFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        CodeFilter.CodeFilterFields.values().map { it.fieldName } shouldContainSame
            listOf("codeClass", "nameMask", "commentMask", "internal")
    }
}
