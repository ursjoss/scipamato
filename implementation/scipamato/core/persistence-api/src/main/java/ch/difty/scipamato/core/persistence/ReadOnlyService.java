package ch.difty.scipamato.core.persistence;

import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * The generic {@link ReadOnlyService} interface, defining the common methods.
 *
 * @param <ID>
 *     the type of the ID of entity {@code T}
 * @param <T>
 *     the entity type, extending {@link CoreEntity}
 * @param <F>
 *     the filter, extending {@link ScipamatoFilter}
 * @author u.joss
 */
public interface ReadOnlyService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter> {

    /**
     * Finds an individual entity by ID. Returns it as an optional of type {@code T}
     *
     * @param id
     *     - must not be null
     * @return Optional
     * @throws NullArgumentException
     *     if id is null
     */
    Optional<T> findById(ID id);

    /**
     * Finds a page full of records of type {@code T} matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of entities of type {@code T} as list
     */
    List<T> findPageByFilter(F filter, PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     *     of type {@code F}
     * @return entity count
     */
    int countByFilter(F filter);

    /**
     * Finds the ids of the persisted entities matching the provided filter and
     * pagination context.
     *
     * @param filter
     *     of type {@code F}
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of the ids of type {@code ID} of matching entities {@code T}
     */
    List<ID> findPageOfIdsByFilter(F filter, PaginationContext paginationContext);

}
