package ch.difty.scipamato.core.entity;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;
import java.util.Set;

import org.apache.bval.jsr.ApacheValidationProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.ScipamatoEntity;

public abstract class Jsr303ValidatedEntityTest<T extends ScipamatoEntity> {

    private LocalValidatorFactoryBean   validatorFactoryBean;
    private Set<ConstraintViolation<T>> violations;

    @Before
    public final void setUp() {
        validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(ApacheValidationProvider.class);
        validatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void assertCompleteEntityPassesValidation() {
        verifySuccessfulValidation(newValidEntity());
    }

    /**
     * Implement to return a fully valid entity of type T
     *
     * @return entity
     */
    protected abstract T newValidEntity();

    private void validate(T validateable) {
        violations = validatorFactoryBean.validate(validateable);
    }

    private Set<ConstraintViolation<T>> getViolations() {
        return violations;
    }

    /**
     * Validates the entity that is assumed to be complete and valid. Asserts there
     * are not violations
     *
     * @param validateable
     *     the entity to validate
     */
    protected void verifySuccessfulValidation(T validateable) {
        validate(validateable);
        assertThat(getViolations()).isEmpty();
    }

    /**
     * Validates the passed in entity that is assumed to have exactly one validation
     * issue. Asserts the validation message.
     *
     * @param validateable
     *     the entity with issues to be validated
     * @param fieldType
     *     the field that breaks the validation
     * @param invalidValue
     *     the invalid value
     * @param msg
     *     the validation message
     */
    protected void validateAndAssertFailure(final T validateable, final FieldEnumType fieldType,
        final Object invalidValue, final String msg) {
        validate(validateable);

        assertThat(getViolations()).isNotEmpty();
        ConstraintViolation<T> violation = getViolations()
            .iterator()
            .next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation
            .getPropertyPath()
            .toString()).isEqualTo(fieldType.getName());
    }

}
