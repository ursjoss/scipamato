package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.validation.ConstraintViolation;

import org.junit.Test;

public class CodeTest extends Jsr303ValidatedEntityTest<Code> {

    private static final LocalDateTime CREATED = LocalDateTime.parse("2017-01-01T08:01:33.821");
    private static final LocalDateTime LAST_MOD = LocalDateTime.parse("2017-02-02T08:01:33.821");

    private final Code c = new Code("1A", "code1", null, false, 1, "c1", "", 1);

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
        Code c1 = new Code("C1", "c1", null, false, 10, "cc10", "codeclass10", 2);
        assertThat(c1.getCodeClass()).isNotNull();
        assertThat(c1.getCodeClass().getId()).isEqualTo(10);
        assertThat(c1.getCodeClass().getName()).isEqualTo("cc10");
        assertThat(c1.getCodeClass().getDescription()).isEqualTo("codeclass10");
    }

    @Test
    public void constructing_withNullCodeClassId_leaveCodeClassNull() {
        Code c1 = new Code("C1", "c1", null, false, null, "cc10", "codeclass10", 1);
        assertThat(c1.getCodeClass()).isNull();
    }

    @Test
    public void validatingCode_beginValid_succeeds() {
        verifySuccessfulValidation(c);
    }

    @Test
    public void validatingCode_withNullCode_fails() {
        Code c1 = new Code(null, "code1", null, false, 1, "c1", "", 1);
        validateAndAssertFailure(c1, Code.CODE, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withNullName_fails() {
        Code c1 = new Code("1A", null, null, false, 1, "c1", "", 1);
        validateAndAssertFailure(c1, Code.NAME, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withNullCodeClass_fails() {
        Code c1 = new Code("1A", "code1", null, false, null, null, null, 1);
        validateAndAssertFailure(c1, Code.CODE_CLASS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingCode_withWrongCodeFormat_fails() {
        Code c1 = new Code("xyz", "code1", null, false, 1, "c1", "", 1);
        validateAndAssertFailure(c1, Code.CODE, "xyz", "{code.invalidCode}");
    }

    @Test
    public void testingToString() {
        final Code c1 = new Code("1A", "code1", null, false, 1, "c1", "", 1, CREATED, 10, LAST_MOD, 20, 3);
        assertThat(c1.toString()).isEqualTo(
                "Code[code=1A,name=code1,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,created=2017-01-01T08:01:33.821,createdBy=10,lastModified=2017-02-02T08:01:33.821,lastModifiedBy=20,version=3]");
    }

    @Test
    public void cloning_copiesValues() {
        Code c1 = new Code("1A", "code1", "foo", true, 1, "c1", "", 2);
        Code c2 = new Code(c1);
        assertThat(c2.getCode()).isEqualTo("1A");
        assertThat(c2.getName()).isEqualTo("code1");
        assertThat(c2.getComment()).isEqualTo("foo");
        assertThat(c2.isInternal()).isTrue();
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
        Code c1 = new Code("1A", "code1", null, false, 1, "c1", "", 1);
        Code c2 = new Code("1B", "code1", null, false, 1, "c1", "", 1);
        Code c3 = new Code("1A", "code2", null, false, 1, "c1", "", 1);
        Code c4 = new Code("1A", "code1", null, false, 2, "c2", "", 1);
        Code c5 = new Code("1A", "code1", null, false, 1, "c2", "", 2);
        Code c6 = new Code("1A", "code1", "foo", false, 1, "c1", "", 1);
        Code c7 = new Code("1A", "code1", null, true, 1, "c1", "", 1);

        assertThat(c1.equals(c2)).isFalse();
        assertThat(c1.equals(c3)).isFalse();
        assertThat(c1.equals(c4)).isFalse();
        assertThat(c1.equals(c5)).isFalse();
        assertThat(c1.equals(c6)).isFalse();
        assertThat(c1.equals(c7)).isFalse();
        assertThat(c2.equals(c3)).isFalse();
        assertThat(c2.equals(c4)).isFalse();
        assertThat(c2.equals(c5)).isFalse();
        assertThat(c2.equals(c6)).isFalse();
        assertThat(c2.equals(c7)).isFalse();
        assertThat(c3.equals(c4)).isFalse();
        assertThat(c3.equals(c5)).isFalse();
        assertThat(c3.equals(c6)).isFalse();
        assertThat(c3.equals(c7)).isFalse();
        assertThat(c4.equals(c5)).isFalse();
        assertThat(c4.equals(c6)).isFalse();
        assertThat(c4.equals(c7)).isFalse();
        assertThat(c5.equals(c6)).isFalse();
        assertThat(c5.equals(c7)).isFalse();
        assertThat(c6.equals(c7)).isFalse();

        assertThat(c1.hashCode()).isNotEqualTo(c2.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c3.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c4.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c1.hashCode()).isNotEqualTo(c7.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c3.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c4.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c2.hashCode()).isNotEqualTo(c7.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c4.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c3.hashCode()).isNotEqualTo(c7.hashCode());
        assertThat(c4.hashCode()).isNotEqualTo(c5.hashCode());
        assertThat(c4.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c4.hashCode()).isNotEqualTo(c7.hashCode());
        assertThat(c5.hashCode()).isNotEqualTo(c6.hashCode());
        assertThat(c5.hashCode()).isNotEqualTo(c7.hashCode());
        assertThat(c6.hashCode()).isNotEqualTo(c7.hashCode());
    }

    @Test
    public void equalingToSpecialCases() {
        assertThat(c.equals(c)).isTrue();
        assertThat(c.equals(null)).isFalse();
        assertThat(c.equals("")).isFalse();
    }

    @Test
    public void displayValue() {
        assertThat(c.getDisplayValue()).isEqualTo("code1 (1A)");
    }
}
