package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.difty.scipamato.common.entity.FieldEnumType;

public abstract class Jsr303ValidatedEntityTest<T extends CoreEntity> {

    private final Class<T> clazz;

    private LocalValidatorFactoryBean   validatorFactoryBean;
    private Set<ConstraintViolation<T>> violations;

    protected Jsr303ValidatedEntityTest(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @BeforeEach
    final void setUp() {
        validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.afterPropertiesSet();
    }

    @Test
    void assertCompleteEntityPassesValidation() {
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
            .toString()).isEqualTo(fieldType.getFieldName());
    }

    @Test
    @Disabled("TODO")
    void toString_isMinimal() {
        final T entity = newValidEntity();
        assertThat(entity.toString()).isEqualTo(getToString());
    }

    /**
     * @return toString value of the valid entity
     */
    protected abstract String getToString();

    @Test
    void displayValue_isEqualTo() {
        final T entity = newValidEntity();
        assertThat(entity.getDisplayValue()).isEqualTo(getDisplayValue());
    }

    /**
     * @return the display value of the entity
     */
    protected abstract String getDisplayValue();

    @Test
    void verifyEquals() {
        EqualsVerifier
            .forClass(clazz)
            .usingGetClass()
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getFieldName(), CREATOR_ID.getFieldName(), MODIFIED.getFieldName(),
                MODIFIER_ID.getFieldName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
