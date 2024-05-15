package ch.difty.scipamato.core.persistence;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.*;
import org.slf4j.Logger;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.persistence.OptimisticLockingException.Type;

/**
 * The generic jOOQ entity repository for manipulation of entities.
 *
 * @param <R>
 *     the type of the record, extending {@link Record}
 * @param <T>
 *     the type of the entity, extending {@link CoreEntity}
 * @param <ID>
 *     the type of the ID of the entity {@code T}
 * @param <TI>
 *     the type of the table implementation of record {@code R}
 * @param <M>
 *     the type of the record mapper, mapping record {@code R} into
 *     entity {@code T}
 * @param <F>
 *     the type of the filter, extending {@link ScipamatoFilter}
 * @author u.joss
 */
@SuppressWarnings("WeakerAccess")
public abstract class JooqEntityRepo<R extends Record, T extends CoreEntity, ID, TI extends Table<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter>
    extends JooqReadOnlyRepo<R, T, ID, TI, M, F> implements EntityRepository<T, ID, F> {

    private final InsertSetStepSetter<R, T> insertSetStepSetter;
    private final UpdateSetStepSetter<R, T> updateSetStepSetter;

    /**
     * @param dsl
     *     the {@link DSLContext}
     * @param mapper
     *     record mapper mapping record {@code R} into entity {@code T}
     * @param sortMapper
     *     {@link JooqSortMapper} mapping spring data sort specifications
     *     into jOOQ specific sort specs
     * @param filterConditionMapper
     *     the {@link GenericFilterConditionMapper} mapping a derivative of
     *     {@link ScipamatoFilter} into jOOC Condition
     * @param dateTimeService
     *     the {@link DateTimeService} providing access to the system time
     * @param insertSetStepSetter
     *     {@link InsertSetStepSetter} mapping the entity fields into the
     *     jOOQ {@link InsertSetStep}.
     * @param updateSetStepSetter
     *     {@link UpdateSetStepSetter} mapping the entity fields into the
     *     jOOQ {@link UpdateSetStep}.
     * @param applicationProperties
     *     {@link ApplicationProperties}
     */
    protected JooqEntityRepo(@NotNull final DSLContext dsl, @NotNull final M mapper, @NotNull final JooqSortMapper<R, T, TI> sortMapper,
        @NotNull final GenericFilterConditionMapper<F> filterConditionMapper, @NotNull final DateTimeService dateTimeService,
        @NotNull final InsertSetStepSetter<R, T> insertSetStepSetter, @NotNull final UpdateSetStepSetter<R, T> updateSetStepSetter,
        @NotNull final ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, applicationProperties);
        this.insertSetStepSetter = insertSetStepSetter;
        this.updateSetStepSetter = updateSetStepSetter;
    }

    /**
     * @return return the Repo specific {@link Logger}
     */
    @NotNull
    protected abstract Logger getLogger();

    /**
     * @param record
     *     persisted record that now holds the ID from the database.
     * @return the id of type {@code ID}
     */
    @NotNull
    protected abstract ID getIdFrom(@NotNull final R record);

    /**
     * @param entity
     *     persisted entity that now holds the ID from the database.
     * @return the id of type {@code ID}
     */
    @Nullable
    protected abstract ID getIdFrom(@NotNull final T entity);

    @Nullable
    @Override
    public T add(@NotNull final T entity) {
        return add(entity, getApplicationProperties().getDefaultLocalization());
    }

    @Nullable
    @Override
    public T add(@NotNull final T entity, @NotNull final String languageCode) {
        final R saved = doSave(entity, languageCode);
        if (saved != null) {
            getLogger().info("{} inserted 1 record: {} with id {}.", getActiveUser().getUserName(), getTable().getName(), getIdFrom(saved));
            final T savedEntity = getMapper().map(saved);
            enrichAssociatedEntitiesOf(savedEntity, languageCode);
            return savedEntity;
        } else {
            getLogger().warn("Unable to insert {} record", getTable().getName());
            return null;
        }
    }

    @Nullable
    protected R doSave(@NotNull final T entity, @NotNull final String languageCode) {
        entity.setCreatedBy(getUserId());
        entity.setLastModifiedBy(getUserId());

        final InsertSetMoreStep<R> step = insertSetStepSetter.setNonKeyFieldsFor(getDsl().insertInto(getTable()), entity);
        insertSetStepSetter.considerSettingKeyOf(step, entity);

        final R saved = step
            .returning()
            .fetchOne();
        insertSetStepSetter.resetIdToEntity(entity, saved);
        saveAssociatedEntitiesOf(entity, languageCode);
        return saved;
    }

    /**
     * Implement if associated entities need separate saving.
     *
     * @param entity
     *     the entity to save the associated entities for
     * @param languageCode
     *     the two character language code
     */
    protected void saveAssociatedEntitiesOf(@NotNull final T entity, @NotNull final String languageCode) {
    }

    @NotNull
    @Override
    public T delete(@NotNull final ID id, final int version) {
        final T toBeDeleted = findById(id, version);
        if (toBeDeleted != null) {
            deleteAssociatedEntitiesOf(toBeDeleted);
            final int deleteCount = getDsl()
                .delete(getTable())
                .where(getTableId().equal(id))
                .and(getRecordVersion().eq(version))
                .execute();
            if (deleteCount > 0) {
                getLogger().info("{} deleted {} record: {} with id {}.", getActiveUser().getUserName(), deleteCount, getTable().getName(), id);
            } else {
                throw new OptimisticLockingException(getTable().getName(), toBeDeleted.toString(), Type.DELETE);
            }
        } else {
            throw new OptimisticLockingException(getTable().getName(), Type.DELETE);
        }
        return toBeDeleted;
    }

    /**
     * Implement if associated entities need separate deletion. Not necessary if
     * cascaded delete is set.
     *
     * @param entity
     *     the entity to free from the associated sub entities
     */
    @SuppressWarnings({ "EmptyMethod", "unused" })
    protected void deleteAssociatedEntitiesOf(final T entity) {
    }

    @Nullable
    @Override
    public T update(@NotNull final T entity) {
        return update(entity, getApplicationProperties().getDefaultLocalization());
    }

    @Nullable
    @Override
    public T update(@NotNull final T entity, @NotNull final String languageCode) {
        final ID id = getIdFrom(entity);
        Objects.requireNonNull(id);

        entity.setLastModified(now());
        entity.setLastModifiedBy(getUserId());

        final R updated = updateSetStepSetter
            .setFieldsFor(getDsl().update(getTable()), entity)
            .where(getTableId().equal(id))
            .and(getRecordVersion().equal(entity.getVersion()))
            .returning()
            .fetchOne();
        if (updated != null) {
            updateAssociatedEntities(entity, languageCode);
            final T savedEntity = findById(id);
            enrichAssociatedEntitiesOf(savedEntity, languageCode);
            getLogger().info("{} updated 1 record: {} with id {}.", getActiveUser().getUserName(), getTable().getName(), id);
            return savedEntity;
        } else {
            throw new OptimisticLockingException(getTable().getName(), entity.toString(), Type.UPDATE);
        }
    }

    /**
     * Implement updates associated entities need separate saving.
     *
     * @param entity
     *     the entity to update
     * @param languageCode
     *     the two character language code
     */
    protected void updateAssociatedEntities(@NotNull final T entity, @NotNull final String languageCode) {
    }
}
