package ch.difty.sipamato.persistance.jooq;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.filter.SipamatoFilter;
import ch.difty.sipamato.lib.NullArgumentException;

/**
 * The generic repository interface for managing entities.
 *
 * @author u.joss
 *
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <ID> the type of the ID of the entity
 * @param <F> the type of the filter extending {@link SipamatorFilter}
 */
public interface EntityRepository<T extends SipamatoEntity, ID, F extends SipamatoFilter> extends ReadOnlyRepository<T, ID, F> {

    /**
     * Add an entity {@code T} to the database.
     *
     * @param entity - must not be null
     * @return the added entity, including the generated default values - or {@code null} if it can't be added.
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
     * Searches the persistent entity {@code T} and modifies it according to the values of the provided entity.
     *
     * @param entity the entity with some changed values - must not be null.
     * @return the modified persisted entity
     * @throws NullArgumentException if the entity is null.
     */
    T update(T entity);

}
