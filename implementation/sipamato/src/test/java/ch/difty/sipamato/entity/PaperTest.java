package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class PaperTest {

    private static final String VALID_AUTHOR = "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
    private static final String VALID_TITLE = "Some non-null title";

    private final Paper p = new Paper();

    private LocalValidatorFactoryBean validatorFactoryBean;
    private Set<ConstraintViolation<Paper>> violations;

    @Before
    public void setUp() {
        validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void gettingFirstAuthor() {
        assertThat(p.getFirstAuthor()).isEmpty();

        p.setAuthor("LastName");
        assertThat(p.getFirstAuthor()).isEqualTo("LastName");

        p.setAuthor("LastName FirstName");
        assertThat(p.getFirstAuthor()).isEqualTo("LastName");
    }

    @Test
    public void validatingPaper_withNullTitle() {
        p.setAuthor(VALID_AUTHOR);
        assertThat(p.getTitle()).isNull();

        violations = validatorFactoryBean.validate(p);

        assertThat(violations).isNotEmpty().hasSize(1);
        ConstraintViolation<Paper> violation = violations.iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
        assertThat(violation.getInvalidValue()).isNull();
        assertThat(violation.getPropertyPath().toString()).isEqualTo(Paper.TITLE);
    }

    private void verifySuccessfulAuthorValidation(final String invalidValue) {
        p.setTitle(VALID_TITLE);
        violations = validatorFactoryBean.validate(p);
        assertThat(violations).isEmpty();
    }

    private void verifyFailedAuthorValidation(final String invalidValue) {
        p.setTitle(VALID_TITLE);

        violations = validatorFactoryBean.validate(p);

        assertThat(violations).isNotEmpty().hasSize(1);
        ConstraintViolation<Paper> violation = violations.iterator().next();
        assertThat(violation.getMessageTemplate()).isEqualTo("{paper.invalidAuthor}");
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath().toString()).isEqualTo(Paper.AUTHOR);
    }

    @Test
    public void validatingPaper_withBlankAuthor() {
        final String invalidValue = "";
        p.setAuthor(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withoutPeriod_fails() {
        final String invalidValue = "Turner";
        p.setAuthor(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withPeriod_succeeds() {
        final String invalidValue = "Turner.";
        p.setAuthor(invalidValue);
        verifySuccessfulAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withoutPeriod_fails() {
        final String invalidValue = "Turner MC";
        p.setAuthor(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withPeriod_succeeds() {
        final String invalidValue = "Turner MC.";
        p.setAuthor(invalidValue);
        verifySuccessfulAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstnames_withoutPeriod_fails() {
        final String invalidValue = "Turner MC, Cohan A";
        p.setAuthor(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstname_withPeriod_succeeds() {
        final String invalidValue = "Turner MC, Cohen A.";
        p.setAuthor(invalidValue);
        verifySuccessfulAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstnames_withoutMissingSpace_fails() {
        final String invalidValue = "Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
        p.setAuthor(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstname_withPeriod_succeeds() {
        final String invalidValue = VALID_AUTHOR;
        p.setAuthor(invalidValue);
        verifySuccessfulAuthorValidation(invalidValue);
    }
}
