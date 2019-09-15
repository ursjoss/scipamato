package ch.difty.scipamato.core.entity

import ch.difty.scipamato.core.entity.CodeClass.CodeClassFields.DESCRIPTION
import ch.difty.scipamato.core.entity.CodeClass.CodeClassFields.NAME
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class CodeClassTest : Jsr303ValidatedEntityTest<CodeClass>(CodeClass::class.java) {

    override fun newValidEntity(): CodeClass {
        return CodeClass(1, "foo", "bar")
    }

    override val toString = "CodeClass[id=1]"
    override val displayValue = "${newValidEntity().id} - ${newValidEntity().name}"

    @Test
    fun validatingCodeClass_beingValid_succeeds() {
        verifySuccessfulValidation(CodeClass(1, "foo", "bar"))
    }

    @Test
    fun validatingCodeClass_withNullName_fails() {
        val cc = CodeClass(1, null, "bar")
        validateAndAssertFailure(cc, NAME, null, JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE)
    }

    @Test
    fun validatingCodeClass_withNullDescription_fails() {
        val cc = CodeClass(1, "foo", null)
        validateAndAssertFailure(cc, DESCRIPTION, null, JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE)
    }

    @Test
    fun cloning_copiesValues() {
        val orig = CodeClass(1, "cc1", DESC)
        val copy = CodeClass(orig)
        assertThat(copy).isEqualToComparingFieldByField(orig)
    }

    @Test
    fun sameValues_makeEquality() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(cc1)
        assertEquality(cc1, cc2)
    }

    private fun assertEquality(cc1: CodeClass, cc2: CodeClass) {
        assertThat(cc1 == cc2).isTrue()
        assertThat(cc2 == cc1).isTrue()
        assertThat(cc1.hashCode()).isEqualTo(cc2.hashCode())
    }

    @Test
    fun differingValues_makeInequality() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(2, "cc1", DESC)
        val cc3 = CodeClass(1, "cc2", DESC)
        val cc4 = CodeClass(1, "cc1", "this is cc2")

        assertThat(cc1 == cc2).isFalse()
        assertThat(cc1 == cc3).isFalse()
        assertThat(cc1 == cc4).isFalse()
        assertThat(cc2 == cc3).isFalse()
        assertThat(cc2 == cc4).isFalse()
        assertThat(cc3 == cc4).isFalse()

        assertThat(cc1.hashCode()).isNotEqualTo(cc2.hashCode())
        assertThat(cc1.hashCode()).isNotEqualTo(cc3.hashCode())
        assertThat(cc1.hashCode()).isNotEqualTo(cc4.hashCode())
        assertThat(cc2.hashCode()).isNotEqualTo(cc3.hashCode())
        assertThat(cc2.hashCode()).isNotEqualTo(cc4.hashCode())
        assertThat(cc3.hashCode()).isNotEqualTo(cc4.hashCode())
    }

    @Test
    fun equalingToSpecialCases() {
        val cc1 = CodeClass(1, "cc1", DESC)

        assertThat(cc1 == cc1).isTrue()
        assertThat(cc1 == null).isFalse()
    }

    @Test
    fun differingValues_withIdNullOnOne() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(null, "cc1", DESC)
        assertInequality(cc1, cc2)
    }

    private fun assertInequality(cc1: CodeClass, cc2: CodeClass) {
        assertThat(cc1 == cc2).isFalse()
        assertThat(cc2 == cc1).isFalse()
        assertThat(cc1.hashCode()).isNotEqualTo(cc2.hashCode())
    }

    @Test
    fun differingValues_withIdNullOnBoth() {
        val cc1 = CodeClass(null, "cc1", DESC)
        val cc2 = CodeClass(null, "cc1", DESC)
        assertEquality(cc1, cc2)
    }

    @Test
    fun differingValues_withNameNullOnOne() {
        val cc1 = CodeClass(1, null, DESC)
        val cc2 = CodeClass(1, "cc1", DESC)
        assertInequality(cc1, cc2)
    }

    @Test
    fun differingValues_withNameNullOnBoth() {
        val cc1 = CodeClass(1, null, DESC)
        val cc2 = CodeClass(1, null, DESC)
        assertEquality(cc1, cc2)
    }

    @Test
    fun differingValues_withDescriptionNullOnOne() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(1, "cc1", null)
        assertInequality(cc1, cc2)
    }

    @Test
    fun differingValues_withDescriptionNullOnBoth() {
        val cc1 = CodeClass(1, "cc1", null)
        val cc2 = CodeClass(1, "cc1", null)
        assertEquality(cc1, cc2)
    }

    companion object {
        private val JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE = "{javax.validation.constraints.NotNull.message}"
        private val DESC = "this is cc1"
    }
}
