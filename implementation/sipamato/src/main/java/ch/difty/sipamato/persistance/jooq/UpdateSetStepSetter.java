package ch.difty.sipamato.persistance.jooq;

import org.jooq.Record;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;

import ch.difty.sipamato.entity.SipamatoEntity;

/**
 * Sets the various fields from the entity into the UpdateSetStep.
 *
 * @author u.joss
 *
 * @param <R> Record extending {@link Record}
 * @param <T> Entity extending {@link SipamtoEntity}
 */
public interface UpdateSetStepSetter<R extends Record, T extends SipamatoEntity> {

    /**
     * Sets all fields from the entity into the step.
     *
     * @param step the {@link UpdateSetFirstStep} to set the values into
     * @param entity the entity to set the values from
     * @return {@link UpdateSetMoreStep} for further usage with jOOQ
     */
    UpdateSetMoreStep<R> setFieldsFor(UpdateSetFirstStep<R> step, T entity);

}
