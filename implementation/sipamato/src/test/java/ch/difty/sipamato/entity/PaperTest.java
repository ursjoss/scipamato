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

    @Test
    public void validatingPaper_withNullTitle() {
        p.setTitle(null);

        validate(p);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<Paper> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
        assertThat(violation.getInvalidValue()).isNull();
        assertThat(violation.getPropertyPath().toString()).isEqualTo(Paper.TITLE);
    }

    private void verifySuccessfulAuthorValidation() {
        validate(p);
        assertThat(getViolations()).isEmpty();
    }

    private void verifyFailedAuthorValidation(final String invalidValue) {
        p.setTitle(NON_NULL_STRING);
        p.setFirstAuthor(NON_NULL_STRING);

        validate(p);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<Paper> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo("{paper.invalidAuthor}");
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(Paper.AUTHORS);
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
        p.setFirstAuthor(NON_NULL_STRING);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withPeriod_succeeds() {
        final String validValue = "Turner.";
        p.setAuthors(validValue);
        p.setFirstAuthor(NON_NULL_STRING);
        verifySuccessfulAuthorValidation();
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withoutPeriod_fails() {
        p.setAuthors("Turner MC");
        p.setFirstAuthor(NON_NULL_STRING);
        verifyFailedAuthorValidation("Turner MC");
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withPeriod_succeeds() {
        p.setAuthors("Turner MC.");
        verifySuccessfulAuthorValidation();
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
        verifySuccessfulAuthorValidation();
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstnames_withoutMissingSpace_fails() {
        final String invalidValue = "Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstname_withPeriod_succeeds() {
        p.setAuthors(VALID_AUTHORS);
        verifySuccessfulAuthorValidation();
    }

    @Test
    public void validatingPaper_withNullFirstAuthor() {
        p.setFirstAuthor(null);

        validate(p);

        assertThat(getViolations()).isNotEmpty().hasSize(1);
        ConstraintViolation<Paper> violation = getViolations().iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
        assertThat(violation.getInvalidValue()).isNull();
        assertThat(violation.getPropertyPath().toString()).isEqualTo(Paper.FIRST_AUTHOR);
    }

    @Test
    public void validatingPaper_withAuthorWithDashInName_shouldSucceed() {
        final String invalidValue = "Alpha-Beta G.";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
        // TODO this should actually succeed
//        verifySuccessfulAuthorValidation();
    }

}
