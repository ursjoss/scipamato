package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;

/**
 * The generic {@link EntityService} interface, defining the common methods for manipulating entities.
 *
 * @author u.joss
 *
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <F> the filter, extending {@link SipamatoFilter}
 */
public interface EntityService<T extends SipamatoEntity, F extends SipamatoFilter> extends ReadOnlyService<T, F> {

    /**
     * Update the provided entity.
     *
     * @param the entity to persist
     * @return the persisted entity
     */
    T update(T entity);

}
