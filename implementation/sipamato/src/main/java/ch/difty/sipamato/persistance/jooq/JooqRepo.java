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

@Profile("DB_JOOQ")
@Transactional(readOnly = true)
public abstract class JooqRepo<R extends Record, E extends SipamatoEntity, ID, T extends TableImpl<R>, M extends RecordMapper<R, E>> implements GenericRepository<R, E, ID, M> {

    private final DSLContext dsl;
    private final M mapper;
    private final InsertSetStepSetter<R, E> insertSetStepSetter;
    private final UpdateSetStepSetter<R, E> updateSetStepSetter;

    protected JooqRepo(DSLContext dsl, M mapper, InsertSetStepSetter<R, E> insertSetStepSetter, UpdateSetStepSetter<R, E> updateSetStepSetter) {
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

    public InsertSetStepSetter<R, E> getInsertSetStepSetter() {
        return insertSetStepSetter;
    }

    /** protected for test purposes */
    public UpdateSetStepSetter<R, E> getUpdateSetStepSetter() {
        return updateSetStepSetter;
    }

    /**
     * @return return the Repo specific {@link Logger}
     */
    protected abstract Logger getLogger();

    /**
     * @return the Entity Class <code>E.class</code>
     */
    protected abstract Class<? extends E> getEntityClass();

    /**
     * @return the jOOQ generated table of type <code>T</code>
     */
    protected abstract T getTable();

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
    protected abstract ID getIdFrom(E entity);

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public E add(final E entity) {
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
    public E delete(final ID id) {
        Asserts.notNull(id, "id");

        final E toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            final int deleteCount = dsl.delete(getTable()).where(getTableId().equal(id)).execute();
            if (deleteCount > 0) {
                getLogger().info("Deleted {} record: {} with id {}.", deleteCount, getTable().getName(), String.valueOf(id));
            } else {
                getLogger().error("Unable to delete {} with id {}", getTable().getName(), id);
            }
        }
        return toBeDeleted;
    }

    /** {@inheritDoc} */
    @Override
    public List<E> findAll() {
        return dsl.selectFrom(getTable()).fetchInto(getEntityClass());
    }

    @Override
    public E findById(final ID id) {
        Asserts.notNull(id, "id");
        return dsl.selectFrom(getTable()).where(getTableId().equal(id)).fetchOneInto(getEntityClass());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public E update(final E entity) {
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
