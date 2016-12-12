package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.Test;

public class ConditionalSupplierTest {

    private final ConditionalSupplier cs = new ConditionalSupplier();

    private Condition c;

    @Test
    public void combiningWithAnd_withNoConditions_selectsAllRecords() {
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo("1 = 1");
    }

    @Test
    public void combiningWithAnd_withSingleCondition_appliesDummyTermWithCondition() {
        cs.add(() -> DSL.field("foo").eq(DSL.value("bar")));
        c = cs.combineWithAnd();
        assertThat(c.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  1 = 1\n" +
            "  and foo = 'bar'\n" +
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
    public void combiningWithOr_withSingleConditions_appliesDummyTermWith() {
        cs.add(() -> DSL.field("foo").eq(DSL.value("bar")));
        c = cs.combineWithOr();
        assertThat(c.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  1 = 0\n" +
            "  or foo = 'bar'\n" +
            ")"
        // @formatter:on
        );
    }
}
