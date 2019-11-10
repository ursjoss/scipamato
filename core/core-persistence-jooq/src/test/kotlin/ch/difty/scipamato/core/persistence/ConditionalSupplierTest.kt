package ch.difty.scipamato.core.persistence

import org.assertj.core.api.Assertions.assertThat

import org.jooq.Condition
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test

internal class ConditionalSupplierTest {

    private val cs = ConditionalSupplier()

    private lateinit var c: Condition

    @Test
    fun combiningWithAnd_withNoConditions_createsDummySelectAllCondition() {
        c = cs.combineWithAnd()
        assertThat(c.toString()).isEqualTo("1 = 1")
    }

    @Test
    fun combiningWithAnd_withSingleConditionConsideredPresent_returnsSingleElementCondition() {
        val present = true
        cs.add(present) {
            DSL.field("baz").eq(DSL.value("boo"))
        }
        c = cs.combineWithAnd()
        assertThat(c.toString()).isEqualTo("baz = 'boo'")
    }

    @Test
    fun combiningWithAnd_withSingleConditionNotConsideredPresent_returnsDummyTrueCondition() {
        val present = false
        cs.add(present) {
            DSL.field("foo").eq(DSL.value("bar"))
        }
        c = cs.combineWithAnd()
        assertThat(c.toString()).isEqualTo("1 = 1")
    }

    @Test
    fun combiningWithAnd_withSingleCondition_returnsSingleElementCondition() {
        cs.add {
            DSL.field("foo").eq(DSL.value("bar"))
        }
        c = cs.combineWithAnd()
        assertThat(c.toString()).isEqualTo("foo = 'bar'")
    }

    @Test
    fun combiningWithAnd_withTwoConditions_appliesDummyTermWithConditions() {
        cs.add {
            DSL.field("foo").eq(DSL.value("bar"))
        }
        cs.add {
            DSL.field("baz").eq(DSL.value("boo"))
        }
        c = cs.combineWithAnd()
        assertThat(c.toString()).isEqualTo(
            """(
                  |  foo = 'bar'
                  |  and baz = 'boo'
                  |)""".trimMargin()
        )
    }

    @Test
    fun combiningWithOr_withNoConditions_selectsAllRecords() {
        c = cs.combineWithOr()
        assertThat(c.toString()).isEqualTo("1 = 0")
    }

    @Test
    fun combiningWithOr_withNoConditions_createsDummySelectNothingCondition() {
        c = cs.combineWithOr()
        assertThat(c.toString()).isEqualTo("1 = 0")
    }

    @Test
    fun combiningWithOr_withSingleConditions_appliesDummyTermWith() {
        cs.add {
            DSL.field("foo").eq(DSL.value("bar"))
        }
        c = cs.combineWithOr()
        assertThat(c.toString()).isEqualTo("foo = 'bar'")
    }

    @Test
    fun combiningWithOr_withDoubleConditions() {
        cs.add {
            DSL.field("foo").eq(DSL.value("bar"))
        }
        cs.add {
            DSL.field("baz").eq(DSL.value("boo"))
        }
        c = cs.combineWithOr()
        assertThat(c.toString()).isEqualTo(
            """(
                   |  foo = 'bar'
                   |  or baz = 'boo'
                   |)""".trimMargin()
        )
    }
}
