package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;

import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * Sets the various fields from the entity into the InsertSetStep.
 *
 * @param <R>
 *     Record extending {@link Record}
 * @param <T>
 *     Entity extending {@link CoreEntity}
 * @author u.joss
 */
public interface InsertSetStepSetter<R extends Record, T extends CoreEntity> {

    /**
     * Sets all the non-key fields of the provided entity into the setter.
     *
     * @param step
     *     {@link InsertSetStep} to set the values into
     * @param entity
     *     the entity with the values to set
     * @return {@link InsertSetMoreStep} for execution
     */
    @NotNull
    InsertSetMoreStep<R> setNonKeyFieldsFor(@NotNull InsertSetStep<R> step, @NotNull T entity);

    /**
     * Sets the key field(s) of the provided entity into the setter - provided it is
     * not null. Note: If null is set explicitly into the setter, jOOQ will not
     * apply the default values from the sequences.
     *
     * @param step
     *     {@link InsertSetMoreStep} to set the values into
     * @param entity
     *     the entity with the values to set
     */
    void considerSettingKeyOf(@NotNull InsertSetMoreStep<R> step, @NotNull T entity);

    /**
     * Sets the id of the saved record into the entity (unless the saved record is
     * null).
     *
     * @param entity
     *     still missing the id
     * @param saved
     *     the record that has the id assigned from the db
     */
    void resetIdToEntity(@NotNull T entity, @Nullable R saved);
}
