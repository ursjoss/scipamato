package ch.difty.scipamato.persistence;

import ch.difty.scipamato.entity.IdScipamatoEntity;
import ch.difty.scipamato.entity.ScipamatoEntity;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;

/**
 * The generic {@link EntityService} interface, defining the common methods for manipulating entities which derive from {@link IdScipamatoEntity}.
 *
 * @author u.joss
 *
 * @param <ID> the type of the id in the {@link ScipamatoEntity}
 * @param <T> the entity type, extending {@link ScipamatoEntity}
 * @param <F> the filter, extending {@link ScipamatoFilter}
 */
public interface EntityService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter> extends ReadOnlyService<ID, T, F> {

    /**
     * Update the provided entity.
     *
     * @param entity the entity to persist
     * @return the persisted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    T saveOrUpdate(T entity);

    /**
     * Removes the provided entity.
     *
     * @param entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    void remove(T entity);

}
