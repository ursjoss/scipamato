package ch.difty.sipamato.persistance.jooq;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.lib.Asserts;
import ch.difty.sipamato.persistance.repository.GenericRepository;

/**
 * The generic jOOQ repository.
 *
 * @author u.joss
 *
 * @param <R> the record, extending {@link Record}
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <ID> the id of entity <literal>T</literal>
 * @param <TI> the table implementation of record <literal>R</literal>
 * @param <M> the record mapper, mapping record <literal>R</literal> into entity <literal>T</literal>
 */
@Profile("DB_JOOQ")
@Transactional(readOnly = true)
public abstract class JooqRepo<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>> implements GenericRepository<R, T, ID, M> {

    private static final long serialVersionUID = 1L;

    private final DSLContext dsl;
    private final M mapper;
    private final InsertSetStepSetter<R, T> insertSetStepSetter;
    private final UpdateSetStepSetter<R, T> updateSetStepSetter;

    protected JooqRepo(DSLContext dsl, M mapper, InsertSetStepSetter<R, T> insertSetStepSetter, UpdateSetStepSetter<R, T> updateSetStepSetter) {
        Asserts.notNull(dsl, "dsl");
        Asserts.notNull(mapper, "mapper");
        Asserts.notNull(insertSetStepSetter, "insertSetStepSetter");
        Asserts.notNull(updateSetStepSetter, "updateSetStepSetter");
        this.dsl = dsl;
        this.mapper = mapper;
        this.insertSetStepSetter = insertSetStepSetter;
        this.updateSetStepSetter = updateSetStepSetter;
    }

    /** protected for test purposes */
    protected DSLContext getDslContext() {
        return dsl;
    }

    /** protected for test purposes */
    public M getMapper() {
        return mapper;
    }

    public InsertSetStepSetter<R, T> getInsertSetStepSetter() {
        return insertSetStepSetter;
    }

    /** protected for test purposes */
    public UpdateSetStepSetter<R, T> getUpdateSetStepSetter() {
        return updateSetStepSetter;
    }

    /**
     * @return return the Repo specific {@link Logger}
     */
    protected abstract Logger getLogger();

    /**
     * @return the Entity Class <code>E.class</code>
     */
    protected abstract Class<? extends T> getEntityClass();

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
            getLogger().warn("Unable to update {} record with id {}.", getTable().getName(), id);
            return null;
        }
    }
}
