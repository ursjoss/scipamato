package ch.difty.scipamato.core.entity.code

import ch.difty.scipamato.core.entity.CodeClass
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(f.codeClass).isEqualTo(cc)
        assertThat(f.nameMask).isEqualTo("nameMask")
        assertThat(f.commentMask).isEqualTo("commentMask")
        assertThat(f.internal).isTrue()

        assertThat(f.toString()).isEqualTo("CodeFilter(codeClass=CodeClass[id=1], nameMask=nameMask, commentMask=commentMask, internal=true)")
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
        assertThat(CodeFilter.CodeFilterFields.values())
                .extracting("name")
                .containsExactly("codeClass", "nameMask", "commentMask", "internal")
    }

}