package ch.difty.scipamato.persistance.jooq;

import org.jooq.Record;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;

import ch.difty.scipamato.entity.ScipamatoEntity;

/**
 * Sets the various fields from the entity into the UpdateSetStep.
 *
 * @author u.joss
 *
 * @param <R> Record extending {@link Record}
 * @param <T> Entity extending {@link ScipamatoEntity}
 */
@FunctionalInterface
public interface UpdateSetStepSetter<R extends Record, T extends ScipamatoEntity> {

    /**
     * Sets all fields except the primary key(s) from the entity into the step.
     *
     * @param step the {@link UpdateSetFirstStep} to set the values into
     * @param entity the entity to set the values from
     * @return {@link UpdateSetMoreStep} for further usage with jOOQ
     */
    UpdateSetMoreStep<R> setFieldsFor(UpdateSetFirstStep<R> step, T entity);

}
