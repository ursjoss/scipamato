package ch.difty.scipamato.entity;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class Jsr303ValidatedEntityTest<T extends ScipamatoEntity> {

    private LocalValidatorFactoryBean validatorFactoryBean;
    private Set<ConstraintViolation<T>> violations;

    @Before
    public void setUp() {
        validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.afterPropertiesSet();
    }

    protected void validate(T validatable) {
        violations = validatorFactoryBean.validate(validatable);
    }

    protected Set<ConstraintViolation<T>> getViolations() {
        return violations;
    }
}
