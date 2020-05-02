package ch.difty.scipamato.core.entity.codeclass

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class CodeClassFilterTest {

    private val f = CodeClassFilter()

    @Test
    fun getAndSet() {
        f.nameMask = "nameMask"
        f.descriptionMask = "descriptionMask"

        f.nameMask shouldBeEqualTo "nameMask"
        f.descriptionMask shouldBeEqualTo "descriptionMask"

        f.toString() shouldBeEqualTo "CodeClassFilter(nameMask=nameMask, descriptionMask=descriptionMask)"
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(CodeClassFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        CodeClassFilter.CodeClassFilterFields.values().map { it.fieldName } shouldContainSame
            listOf("nameMask", "descriptionMask")
    }
}
