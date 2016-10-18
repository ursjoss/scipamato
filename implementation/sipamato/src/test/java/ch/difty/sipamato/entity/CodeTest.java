package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeTest extends Jsr303ValidatedEntityTest<Code> {

    private final Code c = new Code("1A", new CodeClass(1, "c1", ""), "code1");

    private void verifySuccessfulValidation() {
        validate(c);
        assertThat(getViolations()).isEmpty();
    }

    private void validateAndAssertFailure(final String field, final Object invalidValue, final String msg) {
        validate(c);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<Code> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(field);
    }

    @Test
    public void validatingCode_beginValid_succeeds() {
        verifySuccessfulValidation();
    }

    @Test
    public void validatingCode_withNullCode_fails() {
        c.setCode(null);
        validateAndAssertFailure(Code.CODE, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withNullCodeClass_fails() {
        c.setCodeClass(null);
        validateAndAssertFailure(Code.CODE_CLASS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withNullName_fails() {
        c.setName(null);
        validateAndAssertFailure(Code.NAME, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withWrongCodeFormat_fails() {
        c.setCode("xyz");
        validateAndAssertFailure(Code.CODE, "xyz", "{code.invalidCode}");
    }

    @Test
    public void testingToString() {
        assertThat(c.toString()).isEqualTo("Code[code=1A,codeClass=CodeClass[id=1,name=c1,description=],name=code1]");
    }
}
