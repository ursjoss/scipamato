package ch.difty.scipamato.core.entity

import ch.difty.scipamato.core.entity.Code.CodeFields.CODE
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(c1.codeClass).isNotNull
        assertThat(c1
            .codeClass
            .id).isEqualTo(10)
        assertThat(c1
            .codeClass
            .name).isEqualTo("cc10")
        assertThat(c1
            .codeClass
            .description).isEqualTo(CODECLASS10)
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
        assertThat(c2.code).isEqualTo("1A")
        assertThat(c2.name).isEqualTo(CODE1)
        assertThat(c2.comment).isEqualTo("foo")
        assertThat(c2.isInternal).isTrue()
        assertThat(c2.codeClass).isEqualToComparingFieldByField(c1.codeClass)
        assertThat(c2.sort).isEqualTo(2)
    }

    @Test
    fun sameValues_makeEquality() {
        val c = newValidEntity()
        val c2 = Code(c)
        assertThat(c == c2).isTrue()
        assertThat(c2 == c).isTrue()
        assertThat(c.hashCode()).isEqualTo(c2.hashCode())
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

        assertThat(c1 == c2).isFalse()
        assertThat(c1 == c3).isFalse()
        assertThat(c1 == c4).isFalse()
        assertThat(c1 == c5).isFalse()
        assertThat(c1 == c6).isFalse()
        assertThat(c1 == c7).isFalse()
        assertThat(c2 == c3).isFalse()
        assertThat(c2 == c4).isFalse()
        assertThat(c2 == c5).isFalse()
        assertThat(c2 == c6).isFalse()
        assertThat(c2 == c7).isFalse()
        assertThat(c3 == c4).isFalse()
        assertThat(c3 == c5).isFalse()
        assertThat(c3 == c6).isFalse()
        assertThat(c3 == c7).isFalse()
        assertThat(c4 == c5).isFalse()
        assertThat(c4 == c6).isFalse()
        assertThat(c4 == c7).isFalse()
        assertThat(c5 == c6).isFalse()
        assertThat(c5 == c7).isFalse()
        assertThat(c6 == c7).isFalse()

        assertThat(c1.hashCode()).isNotEqualTo(c2.hashCode())
        assertThat(c1.hashCode()).isNotEqualTo(c3.hashCode())
        assertThat(c1.hashCode()).isNotEqualTo(c4.hashCode())
        assertThat(c1.hashCode()).isNotEqualTo(c5.hashCode())
        assertThat(c1.hashCode()).isNotEqualTo(c6.hashCode())
        assertThat(c1.hashCode()).isNotEqualTo(c7.hashCode())
        assertThat(c2.hashCode()).isNotEqualTo(c3.hashCode())
        assertThat(c2.hashCode()).isNotEqualTo(c4.hashCode())
        assertThat(c2.hashCode()).isNotEqualTo(c5.hashCode())
        assertThat(c2.hashCode()).isNotEqualTo(c6.hashCode())
        assertThat(c2.hashCode()).isNotEqualTo(c7.hashCode())
        assertThat(c3.hashCode()).isNotEqualTo(c4.hashCode())
        assertThat(c3.hashCode()).isNotEqualTo(c5.hashCode())
        assertThat(c3.hashCode()).isNotEqualTo(c6.hashCode())
        assertThat(c3.hashCode()).isNotEqualTo(c7.hashCode())
        assertThat(c4.hashCode()).isNotEqualTo(c5.hashCode())
        assertThat(c4.hashCode()).isNotEqualTo(c6.hashCode())
        assertThat(c4.hashCode()).isNotEqualTo(c7.hashCode())
        assertThat(c5.hashCode()).isNotEqualTo(c6.hashCode())
        assertThat(c5.hashCode()).isNotEqualTo(c7.hashCode())
        assertThat(c6.hashCode()).isNotEqualTo(c7.hashCode())
    }

    @Test
    fun equalingToSpecialCases() {
        val c = newValidEntity()
        assertThat(c == c).isTrue()
    }

    private fun assertInequality(c1: Code, c2: Code) {
        assertThat(c1 == c2).isFalse()
        assertThat(c2 == c1).isFalse()
        assertThat(c1.hashCode()).isNotEqualTo(c2.hashCode())
    }

    private fun assertEquality(c1: Code, c2: Code) {
        assertThat(c1 == c2).isTrue()
        assertThat(c2 == c1).isTrue()
        assertThat(c1.hashCode()).isEqualTo(c2.hashCode())
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
