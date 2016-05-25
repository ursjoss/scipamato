package ch.difty.sipamato.web.validator;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.apache.wicket.validation.Validatable;
import org.junit.Test;

public class AuthorFieldValidatorTest {

    private final AuthorFieldValidator validator = AuthorFieldValidator.getInstance();

    @Test
    public void degenerate() {
        try {
            validator.validate(null);
            fail("expected to throw NPE");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullPointerException.class);
        }
        assertValidationFailure("");
    }

    @Test
    public void singleAuthorWithoutFirstname() {
        assertValidationFailure("Turner");
        assertValidationSuccess("Turner.");
    }

    @Test
    public void singleAuthorWithFirstname() {
        assertValidationFailure("Turner MC");
        assertValidationSuccess("Turner MC.");
    }

    @Test
    public void twoAuthorsWithFirstname() {
        assertValidationFailure("Turner MC, Cohan A");
        assertValidationSuccess("Turner MC, Cohen A.");
    }

    @Test
    public void multipleAuthors() {
        assertValidationSuccess("Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.");
        assertValidationFailure("Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.");
    }

    private void assertValidationFailure(final String validatee) {
        assertValidation(validatee, false);
    }

    private void assertValidationSuccess(final String validatee) {
        assertValidation(validatee, true);
    }

    private void assertValidation(final String candidate, final boolean success) {
        Validatable<String> validatable = new Validatable<>(candidate);
        validator.validate(validatable);
        assertThat(validatable.isValid()).isEqualTo(success);
    }
}
