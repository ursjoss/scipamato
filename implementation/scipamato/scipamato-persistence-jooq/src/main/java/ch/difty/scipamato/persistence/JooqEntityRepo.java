package ch.difty.scipamato.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.UpdateSetStep;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.DateTimeService;
import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.entity.ScipamatoEntity;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.persistence.OptimisticLockingException.Type;

/**
 * The generic jOOQ entity repository for manipulation of entities.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link ScipamatoEntity}
 * @param <ID> the type of the ID of the entity {@code T}
 * @param <TI> the type of the table implementation of record {@code R}
 * @param <M> the type of the record mapper, mapping record {@code R} into entity {@code T}
 * @param <F> the type of the filter, extending {@link ScipamatoFilter}
 */
public abstract class JooqEntityRepo<R extends Record, T extends ScipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter>
        extends JooqReadOnlyRepo<R, T, ID, TI, M, F> implements EntityRepository<T, ID, F> {

    private static final long serialVersionUID = 1L;

    private final DateTimeService dateTimeService;
    private final InsertSetStepSetter<R, T> insertSetStepSetter;
    private final UpdateSetStepSetter<R, T> updateSetStepSetter;

    /**
     * @param dsl the {@link DSLContext}
     * @param mapper record mapper mapping record {@code R} into entity {@code T}
     * @param sortMapper {@link JooqSortMapper} mapping spring data sort specifications into jOOQ specific sort specs
     * @param filterConditionMapper the {@link GenericFilterConditionMapper} mapping a derivative of {@link ScipamatoFilter} into jOOC Condition
     * @param dateTimeService the {@link DateTimeService} providing access to the system time
     * @param insertSetStepSetter {@link InsertSetStepSetter} mapping the entity fields into the jOOQ {@link InsertSetStep}.
     * @param updateSetStepSetter {@link UpdateSetStepSetter} mapping the entity fields into the jOOQ {@link UpdateSetStep}.
     * @param applicationProperties {@link ApplicationProperties}
     */
    protected JooqEntityRepo(DSLContext dsl, M mapper, JooqSortMapper<R, T, TI> sortMapper, GenericFilterConditionMapper<F> filterConditionMapper, DateTimeService dateTimeService,
            InsertSetStepSetter<R, T> insertSetStepSetter, UpdateSetStepSetter<R, T> updateSetStepSetter, ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, applicationProperties);
        this.insertSetStepSetter = AssertAs.notNull(insertSetStepSetter, "insertSetStepSetter");
        this.updateSetStepSetter = AssertAs.notNull(updateSetStepSetter, "updateSetStepSetter");
        this.dateTimeService = AssertAs.notNull(dateTimeService, "dateTimeService");
    }

    protected DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    /**
     * @return return the Repo specific {@link Logger}
     */
    protected abstract Logger getLogger();

    /**
     * @param record persisted record that now holds the ID from the database.
     * @return the id of type {@code ID}
     */
    protected abstract ID getIdFrom(R record);

    /**
     * @param entity persisted entity that now holds the ID from the database.
     * @return the id of type {@code ID}
     */
    protected abstract ID getIdFrom(T entity);

    /** {@inheritDoc} */
    @Override
    public T add(final T entity) {
        return add(entity, getApplicationProperties().getDefaultLocalization());
    }

    /** {@inheritDoc} */
    @Override
    public T add(final T entity, final String languageCode) {
        AssertAs.notNull(entity, "entity");
        AssertAs.notNull(languageCode, "languageCode");

        entity.setCreatedBy(getUserId());
        entity.setLastModifiedBy(getUserId());

        InsertSetMoreStep<R> step = insertSetStepSetter.setNonKeyFieldsFor(getDsl().insertInto(getTable()), entity);
        insertSetStepSetter.considerSettingKeyOf(step, entity);

        R saved = step.returning().fetchOne();
        insertSetStepSetter.resetIdToEntity(entity, saved);
        saveAssociatedEntitiesOf(entity, languageCode);
        if (saved != null) {
            getLogger().info("Inserted 1 record: {} with id {}.", getTable().getName(), getIdFrom(saved));
            T savedEntity = getMapper().map(saved);
            enrichAssociatedEntitiesOf(savedEntity, languageCode);
            return savedEntity;
        } else {
            getLogger().warn("Unable to insert {} record", getTable().getName());
            return null;
        }
    }

    /**
     * Implement if associated entities need separate saving.
     * @param entity
     * @param languageCode
     */
    protected void saveAssociatedEntitiesOf(T entity, String languageCode) {
    }

    /** {@inheritDoc} */
    @Override
    public T delete(final ID id, int version) {
        AssertAs.notNull(id, "id");

        final T toBeDeleted = findById(id, version);
        if (toBeDeleted != null) {
            deleteAssociatedEntitiesOf(toBeDeleted);
            final int deleteCount = getDsl().delete(getTable()).where(getTableId().equal(id)).and(getRecordVersion().eq(version)).execute();
            if (deleteCount > 0) {
                getLogger().info("Deleted {} record: {} with id {}.", deleteCount, getTable().getName(), id);
            } else {
                throw new OptimisticLockingException(getTable().getName(), toBeDeleted.toString(), Type.DELETE);
            }
        } else {
            throw new OptimisticLockingException(getTable().getName(), Type.DELETE);
        }
        return toBeDeleted;
    }

    /**
     * Implement if associated entities need separate deletion. Not necessary if cascaded delete is set.
     * @param entity
     */
    protected void deleteAssociatedEntitiesOf(T entity) {
    }

    /** {@inheritDoc} */
    @Override
    public T update(final T entity) {
        return update(entity, getApplicationProperties().getDefaultLocalization());
    }

    /** {@inheritDoc} */
    @Override
    public T update(final T entity, final String languageCode) {
        AssertAs.notNull(entity, "entity");
        AssertAs.notNull(languageCode, "languageCode");
        ID id = AssertAs.notNull(getIdFrom(entity), "entity.id");

        entity.setLastModified(now());
        entity.setLastModifiedBy(getUserId());

        R updated = updateSetStepSetter.setFieldsFor(getDsl().update(getTable()), entity).where(getTableId().equal(id)).and(getRecordVersion().equal(entity.getVersion())).returning().fetchOne();
        if (updated != null) {
            updateAssociatedEntities(entity, languageCode);
            T savedEntity = findById(id);
            enrichAssociatedEntitiesOf(savedEntity, languageCode);
            getLogger().info("Updated 1 record: {} with id {}.", getTable().getName(), id);
            return savedEntity;
        } else {
            throw new OptimisticLockingException(getTable().getName(), entity.toString(), Type.UPDATE);
        }
    }

    /**
     * Implement updates associated entities need separate saving.
     * @param entity
     * @param languageCode
     */
    protected void updateAssociatedEntities(final T entity, final String languageCode) {
    }

    /**
     * @return the current {@link User}
     */
    protected User getActiveUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return (User) auth.getPrincipal();
        } else {
            return User.NO_USER;
        }
    }

    /**
     * @return the id of the currently active {@link User}
     */
    protected Integer getUserId() {
        return getActiveUser().getId();
    }

    /**
     * @return the current date as {@link Timestamp}
     */
    protected Timestamp getTs() {
        return getDateTimeService().getCurrentTimestamp();
    }

    /**
     * @return the current date as {@link Timestamp}
     */
    protected LocalDateTime now() {
        return getDateTimeService().getCurrentDateTime();
    }

}
