package ch.difty.scipamato.persistence;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;

import ch.difty.scipamato.entity.ScipamatoEntity;

/**
 * Sets the various fields from the entity into the InsertSetStep.
 *
 * @author u.joss
 *
 * @param <R> Record extending {@link Record}
 * @param <T> Entity extending {@link ScipamatoEntity}
 */
public interface InsertSetStepSetter<R extends Record, T extends ScipamatoEntity> {

    /**
     * Sets all the non-key fields of the provided entity into the setter.
     *
     * @param step {@link InsertSetStep} to set the values into
     * @param entity the entity with the values to set
     * @return {@link InsertSetMoreStep} for execution
     */
    InsertSetMoreStep<R> setNonKeyFieldsFor(InsertSetStep<R> step, T entity);

    /**
     * Sets the key field(s) of the provided entity into the setter - provided it is not null.
     * Note: If null is set explicitly into the setter, jOOQ will not apply the default values from the sequences.
     *
     * @param step {@link InsertSetMoreStep} to set the values into
     * @param entity the entity with the values to set
     */
    void considerSettingKeyOf(InsertSetMoreStep<R> step, T entity);

    /**
     * Sets the id of the saved record into the entity (unless the saved record is null).
     *
     * @param entity still missing the id
     * @param saved the record that has the id assigned from the db
     */
    void resetIdToEntity(T entity, R saved);

}
