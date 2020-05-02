package ch.difty.scipamato.core.entity

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeEqualTo
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
    fun cloning_copiesValues() {
        val orig = CodeClass(1, "cc1", DESC)
        val copy = CodeClass(orig)
        copy shouldBeEqualTo orig
    }

    @Test
    fun sameValues_makeEquality() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(cc1)
        assertEquality(cc1, cc2)
    }

    private fun assertEquality(cc1: CodeClass, cc2: CodeClass) {
        (cc1 == cc2).shouldBeTrue()
        (cc2 == cc1).shouldBeTrue()
        cc1.hashCode() shouldBeEqualTo cc2.hashCode()
    }

    @Test
    fun differingValues_makeInequality() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(2, "cc1", DESC)
        val cc3 = CodeClass(1, "cc2", DESC)
        val cc4 = CodeClass(1, "cc1", "this is cc2")

        (cc1 == cc2).shouldBeFalse()
        (cc1 == cc3).shouldBeFalse()
        (cc1 == cc4).shouldBeFalse()
        (cc2 == cc3).shouldBeFalse()
        (cc2 == cc4).shouldBeFalse()
        (cc3 == cc4).shouldBeFalse()

        cc1.hashCode() shouldNotBeEqualTo cc2.hashCode()
        cc1.hashCode() shouldNotBeEqualTo cc3.hashCode()
        cc1.hashCode() shouldNotBeEqualTo cc4.hashCode()
        cc2.hashCode() shouldNotBeEqualTo cc3.hashCode()
        cc2.hashCode() shouldNotBeEqualTo cc4.hashCode()
        cc3.hashCode() shouldNotBeEqualTo cc4.hashCode()
    }

    @Test
    fun equalingToSpecialCases() {
        val cc1 = CodeClass(1, "cc1", DESC)

        (cc1 == cc1).shouldBeTrue()
    }

    @Test
    fun differingValues_withIdNullOnOne() {
        val cc1 = CodeClass(1, "cc1", DESC)
        val cc2 = CodeClass(null, "cc1", DESC)
        assertInequality(cc1, cc2)
    }

    private fun assertInequality(cc1: CodeClass, cc2: CodeClass) {
        (cc1 == cc2).shouldBeFalse()
        (cc2 == cc1).shouldBeFalse()
        cc1.hashCode() shouldNotBeEqualTo cc2.hashCode()
    }

    @Test
    fun differingValues_withIdNullOnBoth() {
        val cc1 = CodeClass(null, "cc1", DESC)
        val cc2 = CodeClass(null, "cc1", DESC)
        assertEquality(cc1, cc2)
    }

    companion object {
        private const val DESC = "this is cc1"
    }
}
