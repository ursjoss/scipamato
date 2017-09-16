package ch.difty.scipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.*;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.Test;

public class ConditionalSupplierTest {

    private final ConditionalSupplier cs = new ConditionalSupplier();

    private Condition c;

    @Test
    public void combiningWithAnd_withNoConditions_createsDummySelectAllCondition() {
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("1 = 1");
    }

    @Test
    public void combiningWithAnd_withSingleConditionConsideredPresent_returnsSingleElementCondition() {
        boolean present = true;
        cs.add(present, () -> DSL.field("baz").eq(DSL.value("boo")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("baz = 'boo'");
    }

    @Test
    public void combiningWithAnd_withSingleConditionNotConsideredPresent_returnsDummyTrueCondition() {
        boolean present = false;
        cs.add(present, () -> DSL.field("foo").eq(DSL.value("bar")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("1 = 1");
    }

    @Test
    public void combiningWithAnd_withSingleCondition_returnsSingleElementCondition() {
        cs.add(() -> DSL.field("foo").eq(DSL.value("bar")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("foo = 'bar'");
    }

    @Test
    public void combiningWithAnd_withTwoConditions_appliesDummyTermWithConditions() {
        cs.add(() -> DSL.field("foo").eq(DSL.value("bar")));
        cs.add(() -> DSL.field("baz").eq(DSL.value("boo")));
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
    public void combiningWithOr_withNoConditions_selectsAllRecords() {
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo("1 = 0");
    }

    @Test
    public void combiningWithOr_withNoConditions_createsDummySelectNothingCondition() {
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo("1 = 0");
    }

    @Test
    public void combiningWithOr_withSingleConditions_appliesDummyTermWith() {
        cs.add(() -> DSL.field("foo").eq(DSL.value("bar")));
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo("foo = 'bar'");
    }

    @Test
    public void combiningWithOr_withDoubleConditions() {
        cs.add(() -> DSL.field("foo").eq(DSL.value("bar")));
        cs.add(() -> DSL.field("baz").eq(DSL.value("boo")));
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
