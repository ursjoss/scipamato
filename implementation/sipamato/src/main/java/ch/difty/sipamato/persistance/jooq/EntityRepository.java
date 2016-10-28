package ch.difty.sipamato.persistance.jooq;

import org.jooq.Record;
import org.jooq.RecordMapper;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.lib.NullArgumentException;

/**
 * The generic repository interface for managing entities.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <ID> the type of the ID of the entity
 * @param <M> the type of the record mapper mapping records of type <literal>R</literal> into entities of type <literal>T</literal>
 * @param <F> the type of the filter extending {@link SipamatorFilter}
 */
public interface EntityRepository<R extends Record, T extends SipamatoEntity, ID, M extends RecordMapper<R, T>, F extends SipamatoFilter> extends ReadOnlyRepository<R, T, ID, M, F> {

    /**
     * Add an entity <code>T</code> to the database.
     *
     * @param entity - must not be null
     * @return the added entity, including the generated default values - or <literal>null</literal> if it can't be added.
     * @throws NullArgumentException if the entity is null.
     */
    T add(T entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id - must not be null
     * @return the deleted entity
     * @throws NullArgumentException if the id is null.
     */
    T delete(ID id);

    /**
     * Searches the persistent entity <code>T</code> and modifies it according to the values of the provided entity.
     *
     * @param entity the entity with some changed values - must not be null.
     * @return the modified persisted entity
     * @throws NullArgumentException if the entity is null.
     */
    T update(T entity);

}
