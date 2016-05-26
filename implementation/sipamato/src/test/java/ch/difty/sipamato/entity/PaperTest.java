package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;

import org.junit.Before;
import org.junit.Test;

public class PaperTest extends Jsr303ValidatedEntityTest<Paper> {

    private static final String VALID_AUTHORS = "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
    private static final String VALID_DOI = "10.1093/aje/kwu275";
    private static final String NON_NULL_STRING = "foo";

    private final Paper p = new Paper();

    @Before
    public void setUp() {
        super.setUp();

        p.setId(1);
        p.setPmid(1000);
        p.setDoi(VALID_DOI);
        p.setAuthors(VALID_AUTHORS);
        p.setFirstAuthor(NON_NULL_STRING);
        p.setTitle(NON_NULL_STRING);
        p.setPublicationYear(2016);
        p.setGoals(NON_NULL_STRING);
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
    public void validatingPaper_withBlankAuthor_fails() {
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
    public void validatingPaper_withNullFirstAuthor_fails() {
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

    @Test
    public void validatingPaper_withTooSmallPublicationYear_fails() {
        final int tooEarly = 1499;
        p.setPublicationYear(tooEarly);
        validate(p);
        validateAndAssertFailure(Paper.PUBL_YEAR, tooEarly, "{paper.invalidPublicationYear}");
    }

    @Test
    public void validatingPaper_withOkPublicationYear_succeeds() {
        p.setPublicationYear(1500);
        verifySuccessfulValidation();

        p.setPublicationYear(2016);
        verifySuccessfulValidation();

        p.setPublicationYear(2100);
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withTooLargePublicationYear_fails() {
        final int tooLate = 2101;
        p.setPublicationYear(tooLate);
        validate(p);
        validateAndAssertFailure(Paper.PUBL_YEAR, tooLate, "{paper.invalidPublicationYear}");
    }

    @Test
    public void validatingPaper_withInvalidDoi_fails() {
        final String invalidDoi = "abc";
        p.setDoi(invalidDoi);
        validate(p);
        validateAndAssertFailure(Paper.DOI, invalidDoi, "{paper.invalidDOI}");
    }

    @Test
    public void validatingPaper_withNullGoals_fails() {
        p.setGoals(null);
        validateAndAssertFailure(Paper.GOALS, null, "{javax.validation.constraints.NotNull.message}");
    }

}
