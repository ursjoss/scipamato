package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeTest extends Jsr303ValidatedEntityTest<Code> {

    private final Code c = new Code("1A", "code1", null, 1, "c1", "", 1);

    private void verifySuccessfulValidation(Code code) {
        validate(code);
        assertThat(getViolations()).isEmpty();
    }

    private void validateAndAssertFailure(Code code, final String field, final Object invalidValue, final String msg) {
        validate(code);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<Code> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(field);
    }

    @Test
    public void constructing_withAllValues_populatesCodeClass() {
        Code c = new Code("C1", "c1", null, 10, "cc10", "codeclass10", 2);
        assertThat(c.getCodeClass()).isNotNull();
        assertThat(c.getCodeClass().getId()).isEqualTo(10);
        assertThat(c.getCodeClass().getName()).isEqualTo("cc10");
        assertThat(c.getCodeClass().getDescription()).isEqualTo("codeclass10");
    }

    @Test
    public void constructing_withNullCodeClassId_leaveCodeClassNull() {
        Code c = new Code("C1", "c1", null, null, "cc10", "codeclass10", 1);
        assertThat(c.getCodeClass()).isNull();
    }

    @Test
    public void validatingCode_beginValid_succeeds() {
        verifySuccessfulValidation(c);
    }

    @Test
    public void validatingCode_withNullCode_fails() {
        Code c = new Code(null, "code1", null, 1, "c1", "", 1);
        validateAndAssertFailure(c, Code.CODE, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withNullName_fails() {
        Code c = new Code("1A", null, null, 1, "c1", "", 1);
        validateAndAssertFailure(c, Code.NAME, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withNullCodeClass_fails() {
        Code c = new Code("1A", "code1", null, null, null, null, 1);
        validateAndAssertFailure(c, Code.CODE_CLASS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withWrongCodeFormat_fails() {
        Code c = new Code("xyz", "code1", null, 1, "c1", "", 1);
        validateAndAssertFailure(c, Code.CODE, "xyz", "{code.invalidCode}");
    }

    @Test
    public void testingToString() {
        assertThat(c.toString()).isEqualTo("Code[code=1A,name=code1,comment=<null>,codeClass=CodeClass[id=1],sort=1]");
    }

    @Test
    public void cloning_copiesValues() {
        Code c1 = new Code("1A", "code1", "foo", 1, "c1", "", 2);
        Code c2 = new Code(c1);
        assertThat(c2.getCode()).isEqualTo("1A");
        assertThat(c2.getName()).isEqualTo("code1");
        assertThat(c2.getComment()).isEqualTo("foo");
        assertThat(c2.getCodeClass()).isEqualToComparingFieldByField(c1.getCodeClass());
        assertThat(c2.getSort()).isEqualTo(2);
    }

    @Test
    public void sameValues_makeEquality() {
        Code c2 = new Code(c);
        assertThat(c.equals(c2)).isTrue();
        assertThat(c2.equals(c)).isTrue();
        assertThat(c.hashCode()).isEqualTo(c2.hashCode());
    }

    @Test
    public void differingValues_makeInequality() {
        Code c1 = new Code("1A", "code1", null, 1, "c1", "", 1);
        Code c2 = new Code("1B", "code1", null, 1, "c1", "", 1);
        Code c3 = new Code("1A", "code2", null, 1, "c1", "", 1);
        Code c4 = new Code("1A", "code1", null, 2, "c2", "", 1);
        Code c5 = new Code("1A", "code1", null, 1, "c2", "", 2);
        Code c6 = new Code("1A", "code1", "foo", 1, "c1", "", 1);

        assertThat(c1.equals(c2)).isFalse();
        assertThat(c1.equals(c3)).isFalse();
        assertThat(c1.equals(c4)).isFalse();
        assertThat(c1.equals(c5)).isFalse();
        assertThat(c1.equals(c6)).isFalse();
        assertThat(c2.equals(c3)).isFalse();
        assertThat(c2.equals(c4)).isFalse();
        assertThat(c2.equals(c5)).isFalse();
        assertThat(c2.equals(c6)).isFalse();
        assertThat(c3.equals(c4)).isFalse();
        assertThat(c3.equals(c5)).isFalse();
        assertThat(c3.equals(c6)).isFalse();
        assertThat(c4.equals(c5)).isFalse();
        assertThat(c4.equals(c6)).isFalse();
        assertThat(c5.equals(c6)).isFalse();

        assertThat(c1.hashCode()).isNotEqualTo(c2.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c3.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c4.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c3.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c4.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c4.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c4.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c4.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c5.hashCode()).isNotEqualTo(c6.hashCode());
    }

    @Test
    public void equalingToSpecialCases() {
        assertThat(c.equals(c)).isTrue();
        assertThat(c.equals(null)).isFalse();
        assertThat(c.equals(new String())).isFalse();
    }

    @Test
    public void displayValue() {
        assertThat(c.getDisplayValue()).isEqualTo("code1 (1A)");
    }
}
