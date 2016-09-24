package ch.difty.sipamato.persistance.jooq;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;

import ch.difty.sipamato.entity.SipamatoEntity;

/**
 * Sets the various fields from the entity into the InsertSetStep.
 *
 * @author u.joss
 *
 * @param <R> Record extending {@link Record}
 * @param <E> Entity extending {@link SapamtoEntity}
 */
public interface InsertSetStepSetter<R extends Record, E extends SipamatoEntity> {

    /**
     * Sets all the non-key fields of the provided entity into the setter.
     *
     * @param step {@link InsertSetStep} to set the values into
     * @param entity the entity with the values to set
     * @return {@link InsertSetMoreStep} for execution
     */
    InsertSetMoreStep<R> setNonKeyFieldsFor(InsertSetStep<R> step, E entity);

    /**
     * Sets the key field(s) of the provided entity into the setter - provided it is not null.
     * Note: If null is set explicitly into the setter, jOOQ will not apply the default values from the sequences.
     *
     * @param step {@link InsertSetMoreStep} to set the values into
     * @param entity the entity with the values to set
     */
    void considerSettingKeyOf(InsertSetMoreStep<R> step, E entity);

}
