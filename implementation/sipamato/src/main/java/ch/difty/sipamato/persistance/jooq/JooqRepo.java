package ch.difty.sipamato.persistance.jooq;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.lib.Asserts;

/**
 * The generic jOOQ repository.
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
public abstract class JooqRepo<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter>
        implements GenericRepository<R, T, ID, M, F> {

    private static final long serialVersionUID = 1L;

    private final DSLContext dsl;
    private final M mapper;
    private final InsertSetStepSetter<R, T> insertSetStepSetter;
    private final UpdateSetStepSetter<R, T> updateSetStepSetter;
    private final Configuration jooqConfig;
    private final JooqSortMapper<R, T, TI> sortMapper;

    protected JooqRepo(DSLContext dsl, M mapper, InsertSetStepSetter<R, T> insertSetStepSetter, UpdateSetStepSetter<R, T> updateSetStepSetter, JooqSortMapper<R, T, TI> sortMapper,
            Configuration jooqConfig) {
        Asserts.notNull(dsl, "dsl");
        Asserts.notNull(mapper, "mapper");
        Asserts.notNull(insertSetStepSetter, "insertSetStepSetter");
        Asserts.notNull(updateSetStepSetter, "updateSetStepSetter");
        Asserts.notNull(sortMapper, "sortMapper");
        Asserts.notNull(jooqConfig, "jooqConfig");

        this.dsl = dsl;
        this.mapper = mapper;
        this.insertSetStepSetter = insertSetStepSetter;
        this.updateSetStepSetter = updateSetStepSetter;
        this.sortMapper = sortMapper;
        this.jooqConfig = jooqConfig;
    }

    protected M getMapper() {
        return mapper;
    }

    /**
     * @return return the Repo specific {@link Logger}
     */
    protected abstract Logger getLogger();

    /**
     * @return the Entity Class <code>T.class</code>
     */
    protected abstract Class<? extends T> getEntityClass();

    /**
     * @return the Record Class <code>R.class</code>
     */
    protected abstract Class<? extends R> getRecordClass();

    /**
     * @return the jOOQ generated table of type <code>T</code>
     */
    protected abstract TI getTable();

    /**
     * @return the jOOQ generated {@link TableField} representing the <code>ID</code>
     */
    protected abstract TableField<R, ID> getTableId();

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

    /**
     * @param filter the filter to translate
     * @return the translated {@link Condition}s
     */
    protected abstract Condition createWhereConditions(F filter);

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T add(final T entity) {
        Asserts.notNull(entity);

        InsertSetMoreStep<R> step = insertSetStepSetter.setNonKeyFieldsFor(dsl.insertInto(getTable()), entity);
        insertSetStepSetter.considerSettingKeyOf(step, entity);

        R saved = step.returning().fetchOne();
        if (saved != null) {
            getLogger().info("Inserted 1 record: {} with id {}.", getTable().getName(), getIdFrom(saved));
            return mapper.map(saved);
        } else {
            getLogger().warn("Unable to insert {} record", getTable().getName());
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T delete(final ID id) {
        Asserts.notNull(id, "id");

        final T toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            final int deleteCount = dsl.delete(getTable()).where(getTableId().equal(id)).execute();
            if (deleteCount > 0) {
                getLogger().info("Deleted {} record: {} with id {}.", deleteCount, getTable().getName(), id);
            } else {
                getLogger().error("Unable to delete {} with id {}", getTable().getName(), id);
            }
        }
        return toBeDeleted;
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findAll() {
        return dsl.selectFrom(getTable()).fetchInto(getEntityClass());
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id) {
        Asserts.notNull(id, "id");
        return dsl.selectFrom(getTable()).where(getTableId().equal(id)).fetchOneInto(getEntityClass());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T update(final T entity) {
        Asserts.notNull(entity, "entity");
        ID id = getIdFrom(entity);
        Asserts.notNull(id, "entity.id");

        R updated = updateSetStepSetter.setFieldsFor(dsl.update(getTable()), entity).where(getTableId().equal(id)).returning().fetchOne();
        if (updated != null) {
            getLogger().info("Updated 1 record: {} with id {}.", getTable().getName(), id);
            return mapper.map(updated);
        } else {
            // Ugly, need to work around the problem that update...returning().fetchOne() is not supported for H2
            if (SQLDialect.H2.equals(jooqConfig.dialect())) {
                return findById(id);
            } else {
                getLogger().warn("Unable to persist {} with id {}.", getTable().getName(), id);
                return null;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(F filter) {
        final Condition conditions = createWhereConditions(filter);
        return dsl.fetchCount(dsl.selectOne().from(getTable()).where(conditions));
    }

    /** {@inheritDoc} */
    @Override
    public Page<T> findByFilter(F filter, Pageable pageable) {
        final Condition conditions = createWhereConditions(filter);
        final Collection<SortField<T>> sortCriteria = sortMapper.map(pageable.getSort(), getTable());
        final List<R> queryResults = dsl.selectFrom(getTable()).where(conditions).orderBy(sortCriteria).fetchInto(getRecordClass());

        final List<T> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());

        return new PageImpl<>(entities, pageable, (long) countByFilter(filter));
    }

}
