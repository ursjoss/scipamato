package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Before;
import org.junit.Test;

public class PaperTest extends Jsr303ValidatedEntityTest<Paper> {

    private static final String VALID_AUTHORS = "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
    private static final String NON_NULL_STRING = "foo";

    private final Paper p = new Paper();

    @Before
    public void setUp() {
        super.setUp();

        p.setAuthors(VALID_AUTHORS);
        p.setFirstAuthor(NON_NULL_STRING);
        p.setTitle(NON_NULL_STRING);
    }

    private void verifySuccessfulValidation() {
        validate(p);
        assertThat(getViolations()).isEmpty();
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstname_withPeriod_succeeds() {
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withNullTitle_fails() {
        p.setTitle(null);
        validateAndAssertFailure(Paper.TITLE, null, "{javax.validation.constraints.NotNull.message}");
    }

    private void validateAndAssertFailure(final String field, final Object invalidValue, final String msg) {
        validate(p);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<Paper> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(field);
    }

    private void verifyFailedAuthorValidation(final String invalidValue) {
        validateAndAssertFailure(Paper.AUTHORS, invalidValue, "{paper.invalidAuthor}");
    }

    @Test
    public void validatingPaper_withBlankAuthor() {
        final String invalidValue = "";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withoutPeriod_fails() {
        final String invalidValue = "Turner";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withPeriod_succeeds() {
        final String validValue = "Turner.";
        p.setAuthors(validValue);
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withoutPeriod_fails() {
        p.setAuthors("Turner MC");
        verifyFailedAuthorValidation("Turner MC");
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withPeriod_succeeds() {
        p.setAuthors("Turner MC.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstnames_withoutPeriod_fails() {
        final String invalidValue = "Turner MC, Cohan A";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstname_withPeriod_succeeds() {
        p.setAuthors("Turner MC, Cohen A.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstnames_withoutMissingSpace_fails() {
        final String invalidValue = "Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withNullFirstAuthor() {
        p.setFirstAuthor(null);
        validateAndAssertFailure(Paper.FIRST_AUTHOR, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withAuthorWithDashInName_succeeds() {
        p.setAuthors("Alpha-Beta G.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withAuthorWithTickInName_succeeds() {
        p.setAuthors("d'Alpha G.");
        verifySuccessfulValidation();
    }

}
