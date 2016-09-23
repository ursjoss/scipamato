package ch.difty.sipamato.persistance.repository;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.repo.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.repo.UpdateSetStepSetter;

public interface GenericRepository<R extends Record, E extends SipamatoEntity, ID, M extends RecordMapper<R, E>> {

    /**
     * Add an entity <code>T</code> to the database.
     *
     * @param entity - must not be null
     * @return the added entity, including the generated default values - or <literal>null</literal> if it can't be added.
     * @throws NullArgumentException if the entity is null.
     */
    E add(E entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id - must not be null
     * @return the deleted entity
     * @throws NullArgumentException if the id is null.
     */
    E delete(ID id);

    /**
     * Finds all persisted entitities.
     *
     * @return list of all entities <code>T</code>
     */
    List<E> findAll();

    /**
     * Finds the persistent entity <code>T</code> with the provided id.
     *
     * @param id - must not be null
     * @return the persisted entity <code>T</code> or null if it can't be found.
     * @throws NullArgumentException if the id is null.
     */
    E findById(ID id);

    /**
     * Searches the persistent entity <code>T</code> and modifies it according to the values of the provided entity.
     *
     * @param entity the entity with some changed values - must not be null.
     * @return the modified persisted entity
     * @throws NullArgumentException if the entity is null.
     */
    E update(E entity);

    DSLContext getDslContext();

    M getMapper();

    InsertSetStepSetter<R, E> getInsertSetStepSetter();

    UpdateSetStepSetter<R, E> getUpdateSetStepSetter();

}
