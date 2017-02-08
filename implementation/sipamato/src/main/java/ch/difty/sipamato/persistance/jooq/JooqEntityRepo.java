package ch.difty.sipamato.persistance.jooq;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetStep;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.entity.filter.SipamatoFilter;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.DateTimeService;
import ch.difty.sipamato.service.Localization;

/**
 * The generic jOOQ entity repository for manipulation of entities.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <ID> the type of the ID of the entity <literal>T</literal>
 * @param <TI> the type of the table implementation of record <literal>R</literal>
 * @param <M> the type of the record mapper, mapping record <literal>R</literal> into entity <literal>T</literal>
 * @param <F> the type of the filter, extending {@link SipamatoFilter}
 */
@Profile("DB_JOOQ")
@Transactional(readOnly = true)
public abstract class JooqEntityRepo<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter>
        extends JooqReadOnlyRepo<R, T, ID, TI, M, F> implements EntityRepository<T, ID, F> {

    private static final long serialVersionUID = 1L;

    private final DateTimeService dateTimeService;
    private final InsertSetStepSetter<R, T> insertSetStepSetter;
    private final UpdateSetStepSetter<R, T> updateSetStepSetter;
    private final Configuration jooqConfig;

    /**
     * @param dsl the {@link DSLContext}
     * @param mapper record mapper mapping record <literal>R</literal> into entity <literal>T</literal>
     * @param sortMapper {@link JooqSortMapper} mapping spring data sort specifications into jOOQ specific sort specs
     * @param filterConditionMapper the {@link GenericFilterConditionMapper} mapping a derivative of {@link SipamatoFilter} into jOOC Condition
     * @param dateTimeService the {@link DateTimeService} providing access to the system time
     * @param localization {@link Localization} been providing the information about the requested localization code.
     * @param insertSetStepSetter {@link InsertSetStepSetter} mapping the entity fields into the jOOQ {@link InsertSetStep}.
     * @param updateSetStepSetter{ @link UpdateSetStepSetter} mapping the entity fields into the jOOQ {@link UpdateSetStep}.
     * @param jooqConfig the {@link Configuration} needed to detect and manage db implementation specific aspects.
     */
    protected JooqEntityRepo(DSLContext dsl, M mapper, JooqSortMapper<R, T, TI> sortMapper, GenericFilterConditionMapper<F> filterConditionMapper, DateTimeService dateTimeService,
            Localization localization, InsertSetStepSetter<R, T> insertSetStepSetter, UpdateSetStepSetter<R, T> updateSetStepSetter, Configuration jooqConfig) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization);
        this.insertSetStepSetter = AssertAs.notNull(insertSetStepSetter, "insertSetStepSetter");
        this.updateSetStepSetter = AssertAs.notNull(updateSetStepSetter, "updateSetStepSetter");
        this.dateTimeService = AssertAs.notNull(dateTimeService, "dateTimeService");
        this.jooqConfig = AssertAs.notNull(jooqConfig, "jooqConfig");
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
     * @return the id of type <code>ID</code>
     */
    protected abstract ID getIdFrom(R record);

    /**
     * @param entity persisted entity that now holds the ID from the database.
     * @return the id of type <code>ID</code>
     */
    protected abstract ID getIdFrom(T entity);

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T add(final T entity) {
        AssertAs.notNull(entity, "entity");

        entity.setCreatedBy(getUserId());
        entity.setLastModifiedBy(getUserId());

        InsertSetMoreStep<R> step = insertSetStepSetter.setNonKeyFieldsFor(getDsl().insertInto(getTable()), entity);
        insertSetStepSetter.considerSettingKeyOf(step, entity);

        R saved = step.returning().fetchOne();
        insertSetStepSetter.resetIdToEntity(entity, saved);
        saveAssociatedEntitiesOf(entity);
        if (saved != null) {
            getLogger().info("Inserted 1 record: {} with id {}.", getTable().getName(), getIdFrom(saved));
            T savedEntity = getMapper().map(saved);
            enrichAssociatedEntitiesOf(savedEntity);
            return savedEntity;
        } else {
            getLogger().warn("Unable to insert {} record", getTable().getName());
            return null;
        }
    }

    /**
     * Implement if associated entities need separate saving.
     * @param entity
     */
    protected void saveAssociatedEntitiesOf(T entity) {
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T delete(final ID id) {
        AssertAs.notNull(id, "id");

        final T toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            deleteAssociatedEntitiesOf(toBeDeleted);
            final int deleteCount = getDsl().delete(getTable()).where(getTableId().equal(id)).execute();
            if (deleteCount > 0) {
                getLogger().info("Deleted {} record: {} with id {}.", deleteCount, getTable().getName(), id);
            } else {
                getLogger().error("Unable to delete {} with id {}", getTable().getName(), id);
            }
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
    @Transactional(readOnly = false)
    public T update(final T entity) {
        AssertAs.notNull(entity, "entity");
        ID id = AssertAs.notNull(getIdFrom(entity), "entity.id");

        entity.setLastModified(now());
        entity.setLastModifiedBy(getUserId());

        R updated = updateSetStepSetter.setFieldsFor(getDsl().update(getTable()), entity).where(getTableId().equal(id)).returning().fetchOne();
        updateAssociatedEntities(entity);
        if (updated != null) {
            getLogger().info("Updated 1 record: {} with id {}.", getTable().getName(), id);
            return getMapper().map(updated);
        } else {
            // Ugly, need to work around the problem that update...returning().fetchOne() is not supported for H2
            if (SQLDialect.H2.equals(jooqConfig.dialect())) {
                T savedEntity = findById(id);
                enrichAssociatedEntitiesOf(savedEntity);
                return savedEntity;
            } else {
                getLogger().warn("Unable to persist {} with id {}.", getTable().getName(), id);
                return null;
            }
        }
    }

    /**
     * Implement updates associated entities need separate saving.
     * @param entity
     */
    protected void updateAssociatedEntities(final T entity) {
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
