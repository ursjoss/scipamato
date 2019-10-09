package ch.difty.scipamato.core.entity.codeclass

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CodeClassFilterTest {

    private val f = CodeClassFilter()

    @Test
    fun getAndSet() {
        f.nameMask = "nameMask"
        f.descriptionMask = "descriptionMask"

        assertThat(f.nameMask).isEqualTo("nameMask")
        assertThat(f.descriptionMask).isEqualTo("descriptionMask")

        assertThat(f.toString()).isEqualTo("CodeClassFilter(nameMask=nameMask, descriptionMask=descriptionMask)")
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
        assertThat(CodeClassFilter.CodeClassFilterFields.values().map { it.fieldName })
            .containsExactly("nameMask", "descriptionMask")
    }
}
