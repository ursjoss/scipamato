package ch.difty.scipamato.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.IdScipamatoEntity;
import ch.difty.scipamato.entity.ScipamatoEntity;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.paging.PaginationContext;

/**
 * The generic {@link ReadOnlyService} interface, defining the common methods.
 *
 * @author u.joss
 *
 * @param <ID> the type of the ID of entity {@code T}
 * @param <T> the entity type, extending {@link ScipamatoEntity}
 * @param <F> the filter, extending {@link ScipamatoFilter}
 */
public interface ReadOnlyService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter> extends Serializable {

    /**
     * Finds an individual entity by ID. Returns it as an optional of type {@code T}
     *
     * @param id - must not be null
     * @return Optional
     * @throws NullArgumentException if id is null
     */
    Optional<T> findById(ID id);

    /**
     * Finds a page full of records of type {@code T} matching the provided filter and pagination context.
     *
     * @param filter the filter
     * @param paginationContext context defining paging and sorting
     * @return a page of entities of type {@code T} as list
     */
    List<T> findPageByFilter(F filter, PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     * @return entity count
     */
    int countByFilter(F filter);

    /**
     * Finds the ids of the persisted entities matching the provided filter and pagination context.
     *
     * @param filter of type {@code F}
     * @param paginationContext {@link PaginationContext}
     * @return list of the ids of type {@code ID} of matching entities {@code T}
     */
    List<ID> findPageOfIdsByFilter(F filter, PaginationContext paginationContext);

}
