package ch.difty.scipamato.core.entity;

import javax.validation.ConstraintViolation;
import java.util.Set;

import org.apache.bval.jsr.ApacheValidationProvider;
import org.junit.Before;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.difty.scipamato.common.entity.ScipamatoEntity;

public abstract class Jsr303ValidatedEntityTest<T extends ScipamatoEntity> {

    private LocalValidatorFactoryBean   validatorFactoryBean;
    private Set<ConstraintViolation<T>> violations;

    @Before
    public final void setUp() {
        validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(ApacheValidationProvider.class);
        validatorFactoryBean.afterPropertiesSet();
        localSetUp();
    }

    /**
     * Local setup in the implementing test
     */
    protected abstract void localSetUp();

    protected void validate(T validatable) {
        violations = validatorFactoryBean.validate(validatable);
    }

    protected Set<ConstraintViolation<T>> getViolations() {
        return violations;
    }
}
