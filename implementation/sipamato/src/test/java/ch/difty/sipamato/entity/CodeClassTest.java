package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeClassTest extends Jsr303ValidatedEntityTest<CodeClass> {

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
    public void validatingCodeClass_withNullId_fails() {
        CodeClass cc = new CodeClass(null, "foo", "bar");
        validateAndAssertFailure(cc, CodeClass.ID, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCodeClass_withNullName_fails() {
        CodeClass cc = new CodeClass(1, null, "bar");
        validateAndAssertFailure(cc, CodeClass.NAME, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCodeClass_withNullDescription_fails() {
        CodeClass cc = new CodeClass(1, "foo", null);
        validateAndAssertFailure(cc, CodeClass.DESCRIPTION, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void testingToString() {
        CodeClass cc = new CodeClass(1, "foo", "bar");
        assertThat(cc.toString()).isEqualTo("CodeClass[id=1]");
    }

    @Test
    public void cloning_copiesValues() {
        CodeClass orig = new CodeClass(1, "cc1", "this is cc1");
        CodeClass copy = new CodeClass(orig);
        assertThat(copy).isEqualsToByComparingFields(orig);
    }

    @Test
    public void sameValues_makeEquality() {
        CodeClass cc1 = new CodeClass(1, "cc1", "this is cc1");
        CodeClass cc2 = new CodeClass(cc1);
        assertThat(cc1.equals(cc2)).isTrue();
        assertThat(cc2.equals(cc1)).isTrue();
        assertThat(cc1.hashCode()).isEqualTo(cc2.hashCode());
    }

    @Test
    public void differingValues_makeInequality() {
        CodeClass cc1 = new CodeClass(1, "cc1", "this is cc1");
        CodeClass cc2 = new CodeClass(2, "cc1", "this is cc1");
        CodeClass cc3 = new CodeClass(1, "cc2", "this is cc1");
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

    @Test
    public void equalingToSpecialCases() {
        CodeClass cc1 = new CodeClass(1, "cc1", "this is cc1");

        assertThat(cc1.equals(cc1)).isTrue();
        assertThat(cc1.equals(null)).isFalse();
        assertThat(cc1.equals(new String())).isFalse();
    }

}
