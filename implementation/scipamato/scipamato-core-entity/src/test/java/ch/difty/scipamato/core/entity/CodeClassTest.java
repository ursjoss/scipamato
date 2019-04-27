package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.core.entity.CodeClass.CodeClassFields.DESCRIPTION;
import static ch.difty.scipamato.core.entity.CodeClass.CodeClassFields.NAME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeClassTest extends Jsr303ValidatedEntityTest<CodeClass> {

    private static final String JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE = "{javax.validation.constraints.NotNull.message}";
    private static final String DESC                                          = "this is cc1";

    CodeClassTest() {
        super(CodeClass.class);
    }

    @Override
    protected CodeClass newValidEntity() {
        return new CodeClass(1, "foo", "bar");
    }

    @Override
    protected String getToString() {
        return "CodeClass[id=1]";
    }

    @Override
    protected String getDisplayValue() {
        return newValidEntity().getId() + " - " + newValidEntity().getName();
    }

    @Test
    void validatingCodeClass_beingValid_succeeds() {
        verifySuccessfulValidation(new CodeClass(1, "foo", "bar"));
    }

    @Test
    void validatingCodeClass_withNullName_fails() {
        CodeClass cc = new CodeClass(1, null, "bar");
        validateAndAssertFailure(cc, NAME, null, JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE);
    }

    @Test
    void validatingCodeClass_withNullDescription_fails() {
        CodeClass cc = new CodeClass(1, "foo", null);
        validateAndAssertFailure(cc, DESCRIPTION, null, JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE);
    }

    @Test
    void cloning_copiesValues() {
        CodeClass orig = new CodeClass(1, "cc1", DESC);
        CodeClass copy = new CodeClass(orig);
        assertThat(copy).isEqualToComparingFieldByField(orig);
    }

    @Test
    void sameValues_makeEquality() {
        CodeClass cc1 = new CodeClass(1, "cc1", DESC);
        CodeClass cc2 = new CodeClass(cc1);
        assertEquality(cc1, cc2);
    }

    private void assertEquality(CodeClass cc1, CodeClass cc2) {
        assertThat(cc1.equals(cc2)).isTrue();
        assertThat(cc2.equals(cc1)).isTrue();
        assertThat(cc1.hashCode()).isEqualTo(cc2.hashCode());
    }

    @Test
    void differingValues_makeInequality() {
        CodeClass cc1 = new CodeClass(1, "cc1", DESC);
        CodeClass cc2 = new CodeClass(2, "cc1", DESC);
        CodeClass cc3 = new CodeClass(1, "cc2", DESC);
        CodeClass cc4 = new CodeClass(1, "cc1", "this is cc2");

        assertThat(cc1.equals(cc2)).isFalse();
        assertThat(cc1.equals(cc3)).isFalse();
        assertThat(cc1.equals(cc4)).isFalse();
        assertThat(cc2.equals(cc3)).isFalse();
        assertThat(cc2.equals(cc4)).isFalse();
        assertThat(cc3.equals(cc4)).isFalse();

        assertThat(cc1.hashCode()).isNotEqualTo(cc2.hashCode());
        assertThat(cc1.hashCode()).isNotEqualTo(cc3.hashCode());
        assertThat(cc1.hashCode()).isNotEqualTo(cc4.hashCode());
        assertThat(cc2.hashCode()).isNotEqualTo(cc3.hashCode());
        assertThat(cc2.hashCode()).isNotEqualTo(cc4.hashCode());
        assertThat(cc3.hashCode()).isNotEqualTo(cc4.hashCode());
    }

    @SuppressWarnings({ "unlikely-arg-type", "EqualsWithItself", "ConstantConditions",
        "EqualsBetweenInconvertibleTypes" })
    @Test
    void equalingToSpecialCases() {
        CodeClass cc1 = new CodeClass(1, "cc1", DESC);

        assertThat(cc1.equals(cc1)).isTrue();
        assertThat(cc1.equals(null)).isFalse();
        assertThat(cc1.equals("")).isFalse();
    }

    @Test
    void differingValues_withIdNullOnOne() {
        CodeClass cc1 = new CodeClass(1, "cc1", DESC);
        CodeClass cc2 = new CodeClass(null, "cc1", DESC);
        assertInequality(cc1, cc2);
    }

    private void assertInequality(CodeClass cc1, CodeClass cc2) {
        assertThat(cc1.equals(cc2)).isFalse();
        assertThat(cc2.equals(cc1)).isFalse();
        assertThat(cc1.hashCode()).isNotEqualTo(cc2.hashCode());
    }

    @Test
    void differingValues_withIdNullOnBoth() {
        CodeClass cc1 = new CodeClass(null, "cc1", DESC);
        CodeClass cc2 = new CodeClass(null, "cc1", DESC);
        assertEquality(cc1, cc2);
    }

    @Test
    void differingValues_withNameNullOnOne() {
        CodeClass cc1 = new CodeClass(1, null, DESC);
        CodeClass cc2 = new CodeClass(1, "cc1", DESC);
        assertInequality(cc1, cc2);
    }

    @Test
    void differingValues_withNameNullOnBoth() {
        CodeClass cc1 = new CodeClass(1, null, DESC);
        CodeClass cc2 = new CodeClass(1, null, DESC);
        assertEquality(cc1, cc2);
    }

    @Test
    void differingValues_withDescriptionNullOnOne() {
        CodeClass cc1 = new CodeClass(1, "cc1", DESC);
        CodeClass cc2 = new CodeClass(1, "cc1", null);
        assertInequality(cc1, cc2);
    }

    @Test
    void differingValues_withDescriptionNullOnBoth() {
        CodeClass cc1 = new CodeClass(1, "cc1", null);
        CodeClass cc2 = new CodeClass(1, "cc1", null);
        assertEquality(cc1, cc2);
    }
}
