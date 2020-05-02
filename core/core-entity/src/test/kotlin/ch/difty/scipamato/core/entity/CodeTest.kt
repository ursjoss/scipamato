package ch.difty.scipamato.core.entity

import ch.difty.scipamato.core.entity.Code.CodeFields.CODE
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class CodeTest : Jsr303ValidatedEntityTest<Code>(Code::class.java) {

    override fun newValidEntity(): Code {
        return Code("1A", CODE1, null, false, 1, "c1", "", 1, CREATED, 10, LAST_MOD, 20, 3)
    }

    override val toString =
        """Code[code=1A,name=code1,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,
            |createdBy=10,lastModifiedBy=20,created=2017-01-01T08:01:33.821,
            |lastModified=2017-02-02T08:01:33.821,version=3]""".trimMargin()
    override val displayValue = "code1 (1A)"

    @Test
    fun constructing_withAllValues_populatesCodeClass() {
        val c1 = Code("C1", "c1", null, false, 10, "cc10", CODECLASS10, 2)
        c1.codeClass.shouldNotBeNull()
        c1.codeClass.id shouldBeEqualTo 10
        c1.codeClass.name shouldBeEqualTo "cc10"
        c1.codeClass.description shouldBeEqualTo CODECLASS10
    }

    @Test
    fun validatingCode_withWrongCodeFormat_fails() {
        val c1 = Code("xyz", CODE1, null, false, 1, "c1", "", 1)
        validateAndAssertFailure(c1, CODE, "xyz", "{code.invalidCode}")
    }

    @Test
    fun cloning_copiesValues() {
        val c1 = Code("1A", CODE1, "foo", true, 1, "c1", "", 2)
        val c2 = Code(c1)
        c2.code shouldBeEqualTo "1A"
        c2.name shouldBeEqualTo CODE1
        c2.comment shouldBeEqualTo "foo"
        c2.isInternal.shouldBeTrue()
        c2.codeClass shouldBeEqualTo c1.codeClass
        c2.sort shouldBeEqualTo 2
    }

    @Test
    fun sameValues_makeEquality() {
        val c = newValidEntity()
        val c2 = Code(c)
        (c == c2).shouldBeTrue()
        (c2 == c).shouldBeTrue()
        c.hashCode() shouldBeEqualTo c2.hashCode()
    }

    @Test
    fun differingValues_makeInequality() {
        val c1 = Code("1A", CODE1, null, false, 1, "c1", "", 1)
        val c2 = Code("1B", CODE1, null, false, 1, "c1", "", 1)
        val c3 = Code("1A", "code2", null, false, 1, "c1", "", 1)
        val c4 = Code("1A", CODE1, null, false, 2, "c2", "", 1)
        val c5 = Code("1A", CODE1, null, false, 1, "c2", "", 2)
        val c6 = Code("1A", CODE1, "foo", false, 1, "c1", "", 1)
        val c7 = Code("1A", CODE1, null, true, 1, "c1", "", 1)

        (c1 == c2).shouldBeFalse()
        (c1 == c3).shouldBeFalse()
        (c1 == c4).shouldBeFalse()
        (c1 == c5).shouldBeFalse()
        (c1 == c6).shouldBeFalse()
        (c1 == c7).shouldBeFalse()
        (c2 == c3).shouldBeFalse()
        (c2 == c4).shouldBeFalse()
        (c2 == c5).shouldBeFalse()
        (c2 == c6).shouldBeFalse()
        (c2 == c7).shouldBeFalse()
        (c3 == c4).shouldBeFalse()
        (c3 == c5).shouldBeFalse()
        (c3 == c6).shouldBeFalse()
        (c3 == c7).shouldBeFalse()
        (c4 == c5).shouldBeFalse()
        (c4 == c6).shouldBeFalse()
        (c4 == c7).shouldBeFalse()
        (c5 == c6).shouldBeFalse()
        (c5 == c7).shouldBeFalse()
        (c6 == c7).shouldBeFalse()

        c1.hashCode() shouldNotBeEqualTo c2.hashCode()
        c1.hashCode() shouldNotBeEqualTo c3.hashCode()
        c1.hashCode() shouldNotBeEqualTo c4.hashCode()
        c1.hashCode() shouldNotBeEqualTo c5.hashCode()
        c1.hashCode() shouldNotBeEqualTo c6.hashCode()
        c1.hashCode() shouldNotBeEqualTo c7.hashCode()
        c2.hashCode() shouldNotBeEqualTo c3.hashCode()
        c2.hashCode() shouldNotBeEqualTo c4.hashCode()
        c2.hashCode() shouldNotBeEqualTo c5.hashCode()
        c2.hashCode() shouldNotBeEqualTo c6.hashCode()
        c2.hashCode() shouldNotBeEqualTo c7.hashCode()
        c3.hashCode() shouldNotBeEqualTo c4.hashCode()
        c3.hashCode() shouldNotBeEqualTo c5.hashCode()
        c3.hashCode() shouldNotBeEqualTo c6.hashCode()
        c3.hashCode() shouldNotBeEqualTo c7.hashCode()
        c4.hashCode() shouldNotBeEqualTo c5.hashCode()
        c4.hashCode() shouldNotBeEqualTo c6.hashCode()
        c4.hashCode() shouldNotBeEqualTo c7.hashCode()
        c5.hashCode() shouldNotBeEqualTo c6.hashCode()
        c5.hashCode() shouldNotBeEqualTo c7.hashCode()
        c6.hashCode() shouldNotBeEqualTo c7.hashCode()
    }

    @Test
    fun equalingToSpecialCases() {
        val c = newValidEntity()
        (c == c).shouldBeTrue()
    }

    private fun assertInequality(c1: Code, c2: Code) {
        (c1 == c2).shouldBeFalse()
        (c2 == c1).shouldBeFalse()
        c1.hashCode() shouldNotBeEqualTo c2.hashCode()
    }

    private fun assertEquality(c1: Code, c2: Code) {
        (c1 == c2).shouldBeTrue()
        (c2 == c1).shouldBeTrue()
        c1.hashCode() shouldBeEqualTo c2.hashCode()
    }

    @Test
    fun differingValues_withSameComment() {
        val c1 = Code("1A", CODE1, "foo", false, 1, "c1", "", 1)
        val c2 = Code("1A", CODE1, "foo", false, 1, "c1", "", 1)
        assertEquality(c1, c2)
    }

    @Test
    fun differingValues_withDifferingComment() {
        val c1 = Code("1A", CODE1, "foo", false, 1, "c1", "", 1)
        val c2 = Code("1A", CODE1, "bar", false, 1, "c1", "", 1)
        assertInequality(c1, c2)
    }

    @Test
    fun differingValues_withDifferingSort() {
        val c1 = Code("1A", CODE1, "", false, 1, "c1", "", 1)
        val c2 = Code("1A", CODE1, "", false, 1, "c1", "", 2)
        assertInequality(c1, c2)
    }

    companion object {
        private const val CODE1 = "code1"
        private const val CODECLASS10 = "codeclass10"

        private val CREATED = LocalDateTime.parse("2017-01-01T08:01:33.821")
        private val LAST_MOD = LocalDateTime.parse("2017-02-02T08:01:33.821")
    }
}
