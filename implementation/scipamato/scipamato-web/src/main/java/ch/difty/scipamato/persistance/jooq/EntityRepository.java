package ch.difty.scipamato.persistance.jooq;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.ScipamatoEntity;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.persistance.OptimisticLockingException;

/**
 * The generic repository interface for managing entities.
 *
 * @author u.joss
 *
 * @param <T> the type of the entity, extending {@link ScipamatoEntity}
 * @param <ID> the type of the ID of the entity
 * @param <F> the type of the filter extending {@link ScipamatoFilter}
 */
public interface EntityRepository<T extends ScipamatoEntity, ID, F extends ScipamatoFilter> extends ReadOnlyRepository<T, ID, F> {

    /**
     * Add an entity {@code T} to the database.
     *
     * @param entity - must not be null
     * @return the added entity, including the generated default values - or {@code null} if it can't be added.
     * @throws NullArgumentException if the entity is null.
     */
    T add(T entity);

    /**
     * Add an entity {@code T} to the database.
     *
     * @param entity - must not be null
     * @param languageCode - must not be null
     * @return the added entity, including the generated default values - or {@code null} if it can't be added.
     * @throws NullArgumentException if the entity is null.
     */
    T add(T entity, String languageCode);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws NullArgumentException if the id is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    T delete(ID id, int version);

    /**
     * Searches the persistent entity {@code T} and modifies it according to the values of the provided entity.
     *
     * @param entity
     *     the entity with some changed values - must not be null.
     * @return the modified persisted entity
     * @throws NullArgumentException
     *     if the entity is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    T update(T entity);

    /**
     * Searches the persistent entity {@code T} and modifies it according to the values of the provided entity.
     *
     * @param entity
     *     the entity with some changed values - must not be null.
     * @param languageCode
     * @return the modified persisted entity
     * @throws NullArgumentException
     *     if the entity is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    T update(T entity, String languageCode);

}
