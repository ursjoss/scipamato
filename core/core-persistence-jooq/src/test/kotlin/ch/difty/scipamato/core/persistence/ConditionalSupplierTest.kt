package ch.difty.scipamato.core.persistence

import org.amshove.kluent.shouldBeEqualTo
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test

internal class ConditionalSupplierTest {

    private val cs = ConditionalSupplier()

    @Test
    fun combiningWithAnd_withNoConditions_createsDummySelectAllCondition() {
        val c = cs.combineWithAnd()
        c.toString() shouldBeEqualTo "1 = 1"
    }

    @Test
    fun combiningWithAnd_withSingleConditionConsideredPresent_returnsSingleElementCondition() {
        val present = true
        cs.add(present) { DSL.field("baz").eq(DSL.value("boo")) }
        val c = cs.combineWithAnd()
        c.toString() shouldBeEqualTo "baz = 'boo'"
    }

    @Test
    fun combiningWithAnd_withSingleConditionNotConsideredPresent_returnsDummyTrueCondition() {
        val present = false
        cs.add(present) { DSL.field("foo").eq(DSL.value("bar")) }
        val c = cs.combineWithAnd()
        c.toString() shouldBeEqualTo "1 = 1"
    }

    @Test
    fun combiningWithAnd_withSingleCondition_returnsSingleElementCondition() {
        cs.add { DSL.field("foo").eq(DSL.value("bar")) }
        val c = cs.combineWithAnd()
        c.toString() shouldBeEqualTo "foo = 'bar'"
    }

    @Test
    fun combiningWithAnd_withTwoConditions_appliesDummyTermWithConditions() {
        cs.add { DSL.field("foo").eq(DSL.value("bar")) }
        cs.add { DSL.field("baz").eq(DSL.value("boo")) }
        val c = cs.combineWithAnd()
        c.toString() shouldBeEqualTo
            """(
               |  foo = 'bar'
               |  and baz = 'boo'
               |)""".trimMargin()
    }

    @Test
    fun combiningWithOr_withNoConditions_selectsAllRecords() {
        val c = cs.combineWithOr()
        c.toString() shouldBeEqualTo "1 = 0"
    }

    @Test
    fun combiningWithOr_withNoConditions_createsDummySelectNothingCondition() {
        val c = cs.combineWithOr()
        c.toString() shouldBeEqualTo "1 = 0"
    }

    @Test
    fun combiningWithOr_withSingleConditions_appliesDummyTermWith() {
        cs.add { DSL.field("foo").eq(DSL.value("bar")) }
        val c = cs.combineWithOr()
        c.toString() shouldBeEqualTo "foo = 'bar'"
    }

    @Test
    fun combiningWithOr_withDoubleConditions() {
        cs.add { DSL.field("foo").eq(DSL.value("bar")) }
        cs.add { DSL.field("baz").eq(DSL.value("boo")) }
        val c = cs.combineWithOr()
        c.toString() shouldBeEqualTo
            """(
               |  foo = 'bar'
               |  or baz = 'boo'
               |)""".trimMargin()
    }
}
