package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeClassTest extends Jsr303ValidatedEntityTest<CodeClass> {

    private CodeClass cc = new CodeClass(1, "foo", "bar");

    private void verifySuccessfulValidation() {
        validate(cc);
        assertThat(getViolations()).isEmpty();
    }

    private void validateAndAssertFailure(final String field, final Object invalidValue, final String msg) {
        validate(cc);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<CodeClass> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(field);
    }

    @Test
    public void validatingCodeClass_beingValid_succeeds() {
        verifySuccessfulValidation();
    }

    @Test
    public void validatingCodeClass_withNullId_fails() {
        cc.setId(null);
        validateAndAssertFailure(CodeClass.ID, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCodeClass_withNullName_fails() {
        cc.setName(null);
        validateAndAssertFailure(CodeClass.NAME, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCodeClass_withNullDescription_fails() {
        cc.setDescription(null);
        validateAndAssertFailure(CodeClass.DESCRIPTION, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void testingToString() {
        assertThat(cc.toString()).isEqualTo("CodeClass[id=1,name=foo,description=bar]");
    }

}
