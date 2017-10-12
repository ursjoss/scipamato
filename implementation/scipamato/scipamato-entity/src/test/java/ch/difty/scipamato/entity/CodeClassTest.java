package ch.difty.scipamato.entity;

import static org.assertj.core.api.Assertions.*;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeClassTest extends Jsr303ValidatedEntityTest<CodeClass> {

    private static final String JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE = "{javax.validation.constraints.NotNull.message}";
    private static final String THIS_IS_CC1 = "this is cc1";

    private void verifySuccessfulValidation(final CodeClass cc) {
        validate(cc);
        assertThat(getViolations()).isEmpty();
    }

    private void validateAndAssertFailure(final CodeClass cc, final String field, final Object invalidValue, final String msg) {
        validate(cc);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<CodeClass> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(field);
    }

    @Test
    public void validatingCodeClass_beingValid_succeeds() {
        verifySuccessfulValidation(new CodeClass(1, "foo", "bar"));
    }

    @Test
    public void validatingCodeClass_withNullName_fails() {
        CodeClass cc = new CodeClass(1, null, "bar");
        validateAndAssertFailure(cc, CodeClass.NAME, null, JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE);
    }

    @Test
    public void validatingCodeClass_withNullDescription_fails() {
        CodeClass cc = new CodeClass(1, "foo", null);
        validateAndAssertFailure(cc, CodeClass.DESCRIPTION, null, JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL_MESSAGE);
    }

    @Test
    public void testingToString() {
        CodeClass cc = new CodeClass(1, "foo", "bar");
        assertThat(cc.toString()).isEqualTo("CodeClass[id=1]");
    }

    @Test
    public void cloning_copiesValues() {
        CodeClass orig = new CodeClass(1, "cc1", THIS_IS_CC1);
        CodeClass copy = new CodeClass(orig);
        assertThat(copy).isEqualToComparingFieldByField(orig);
    }

    @Test
    public void sameValues_makeEquality() {
        CodeClass cc1 = new CodeClass(1, "cc1", THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(cc1);
        assertEquality(cc1, cc2);
    }

    private void assertEquality(CodeClass cc1, CodeClass cc2) {
        assertThat(cc1.equals(cc2)).isTrue();
        assertThat(cc2.equals(cc1)).isTrue();
        assertThat(cc1.hashCode()).isEqualTo(cc2.hashCode());
    }

    @Test
    public void differingValues_makeInequality() {
        CodeClass cc1 = new CodeClass(1, "cc1", THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(2, "cc1", THIS_IS_CC1);
        CodeClass cc3 = new CodeClass(1, "cc2", THIS_IS_CC1);
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

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void equalingToSpecialCases() {
        CodeClass cc1 = new CodeClass(1, "cc1", THIS_IS_CC1);

        assertThat(cc1.equals(cc1)).isTrue();
        assertThat(cc1.equals(null)).isFalse();
        assertThat(cc1.equals("")).isFalse();
    }

    @Test
    public void displayValue() {
        CodeClass cc1 = new CodeClass(1, "cc1", THIS_IS_CC1);
        assertThat(cc1.getDisplayValue()).isEqualTo("cc1");
    }

    @Test
    public void differingValues_withIdNullOnOne() {
        CodeClass cc1 = new CodeClass(1, "cc1", THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(null, "cc1", THIS_IS_CC1);
        assertInequality(cc1, cc2);
    }

    private void assertInequality(CodeClass cc1, CodeClass cc2) {
        assertThat(cc1.equals(cc2)).isFalse();
        assertThat(cc2.equals(cc1)).isFalse();
        assertThat(cc1.hashCode()).isNotEqualTo(cc2.hashCode());
    }

    @Test
    public void differingValues_withIdNullOnBoth() {
        CodeClass cc1 = new CodeClass(null, "cc1", THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(null, "cc1", THIS_IS_CC1);
        assertEquality(cc1, cc2);
    }

    @Test
    public void differingValues_withNameNullOnOne() {
        CodeClass cc1 = new CodeClass(1, null, THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(1, "cc1", THIS_IS_CC1);
        assertInequality(cc1, cc2);
    }

    @Test
    public void differingValues_withNameNullOnBoth() {
        CodeClass cc1 = new CodeClass(1, null, THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(1, null, THIS_IS_CC1);
        assertEquality(cc1, cc2);
    }

    @Test
    public void differingValues_withDescriptionNullOnOne() {
        CodeClass cc1 = new CodeClass(1, "cc1", THIS_IS_CC1);
        CodeClass cc2 = new CodeClass(1, "cc1", null);
        assertInequality(cc1, cc2);
    }

    @Test
    public void differingValues_withDescriptionNullOnBoth() {
        CodeClass cc1 = new CodeClass(1, "cc1", null);
        CodeClass cc2 = new CodeClass(1, "cc1", null);
        assertEquality(cc1, cc2);
    }
}
