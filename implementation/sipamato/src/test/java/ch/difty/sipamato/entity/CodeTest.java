package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeTest extends Jsr303ValidatedEntityTest<Code> {

    private final Code c = new Code("1A", "code1", 1, "c1", "");

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
    public void constructing_withAllValues_populatesCodeClass() {
        Code c = new Code("C1", "c1", 10, "cc10", "codeclass10");
        assertThat(c.getCodeClass()).isNotNull();
        assertThat(c.getCodeClass().getId()).isEqualTo(10);
        assertThat(c.getCodeClass().getName()).isEqualTo("cc10");
        assertThat(c.getCodeClass().getDescription()).isEqualTo("codeclass10");
    }

    @Test
    public void constructing_withNullCodeClassId_leaveCodeClassNull() {
        Code c = new Code("C1", "c1", null, "cc10", "codeclass10");
        assertThat(c.getCodeClass()).isNull();
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
        assertThat(c.toString()).isEqualTo("Code[code=1A,name=code1,codeClass=CodeClass[id=1,name=c1,description=]]");
    }

    @Test
    public void cloning_copiesValuesDecoupled() {
        Code orig = new Code("C1", "c1", 10, "cc10", "cc10");
        Code copy = new Code(orig);
        assertThat(copy.getCode()).isEqualTo("C1");
        assertThat(copy.getCodeClass()).isEqualsToByComparingFields(orig.getCodeClass());
        assertThat(copy.getName()).isEqualTo("c1");

        orig.setCode("C2");
        assertThat(orig.getCode()).isEqualTo("C2");
        assertThat(copy.getCode()).isEqualTo("C1");

        orig.getCodeClass().setId(20);
        assertThat(orig.getCodeClass().getId()).isEqualTo(20);
        assertThat(copy.getCodeClass().getId()).isEqualTo(10);
    }

}
