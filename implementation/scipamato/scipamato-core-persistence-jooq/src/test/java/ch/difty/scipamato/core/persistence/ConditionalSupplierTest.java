package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

class ConditionalSupplierTest {

    private final ConditionalSupplier cs = new ConditionalSupplier();

    private Condition c;

    @Test
    void combiningWithAnd_withNoConditions_createsDummySelectAllCondition() {
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("1 = 1");
    }

    @Test
    void combiningWithAnd_withSingleConditionConsideredPresent_returnsSingleElementCondition() {
        boolean present = true;
        cs.add(present, () -> DSL
            .field("baz")
            .eq(DSL.value("boo")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("baz = 'boo'");
    }

    @Test
    void combiningWithAnd_withSingleConditionNotConsideredPresent_returnsDummyTrueCondition() {
        boolean present = false;
        cs.add(present, () -> DSL
            .field("foo")
            .eq(DSL.value("bar")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("1 = 1");
    }

    @Test
    void combiningWithAnd_withSingleCondition_returnsSingleElementCondition() {
        cs.add(() -> DSL
            .field("foo")
            .eq(DSL.value("bar")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("foo = 'bar'");
    }

    @Test
    void combiningWithAnd_withTwoConditions_appliesDummyTermWithConditions() {
        cs.add(() -> DSL
            .field("foo")
            .eq(DSL.value("bar")));
        cs.add(() -> DSL
            .field("baz")
            .eq(DSL.value("boo")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  foo = 'bar'\n" +
            "  and baz = 'boo'\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    void combiningWithOr_withNoConditions_selectsAllRecords() {
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo("1 = 0");
    }

    @Test
    void combiningWithOr_withNoConditions_createsDummySelectNothingCondition() {
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo("1 = 0");
    }

    @Test
    void combiningWithOr_withSingleConditions_appliesDummyTermWith() {
        cs.add(() -> DSL
            .field("foo")
            .eq(DSL.value("bar")));
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo("foo = 'bar'");
    }

    @Test
    void combiningWithOr_withDoubleConditions() {
        cs.add(() -> DSL
            .field("foo")
            .eq(DSL.value("bar")));
        cs.add(() -> DSL
            .field("baz")
            .eq(DSL.value("boo")));
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  foo = 'bar'\n" +
            "  or baz = 'boo'\n" +
            ")"
            // @formatter:on
        );
    }
}
