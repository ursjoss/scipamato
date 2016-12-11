package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.filter.SipamatoFilter;

/**
 * The generic {@link EntityService} interface, defining the common methods for manipulating entities which derive from {@link IdSipamatoEntity}.
 *
 * @author u.joss
 *
 * @param <ID> the type of the id in the {@link SipamatoEntity}
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <F> the filter, extending {@link SipamatoFilter}
 */
public interface EntityService<ID extends Number, T extends IdSipamatoEntity<ID>, F extends SipamatoFilter> extends ReadOnlyService<T, ID, F> {

    /**
     * Update the provided entity.
     *
     * @param the entity to persist
     * @return the persisted entity
     */
    T saveOrUpdate(T entity);

    /**
     * Removes the provided entity.
     *
     * @param entity
     */
    void remove(T entity);

}
