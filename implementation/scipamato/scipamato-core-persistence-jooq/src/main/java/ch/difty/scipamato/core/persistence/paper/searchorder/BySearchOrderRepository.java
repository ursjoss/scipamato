package ch.difty.scipamato.core.persistence.paper.searchorder;

import java.util.List;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.search.SearchOrder;

/**
 * Repository to find Papers or PaperSlims by {@link SearchOrder}
 * specifications.
 *
 * @author u.joss
 *
 * @param <T>
 *            derivatives of {@link IdScipamatoEntity}, should actually be Paper
 *            or PaperSlim
 */
public interface BySearchOrderRepository<T extends IdScipamatoEntity<Long>> {

    /**
     * Finds all entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *            {@link SearchOrder} the search specification
     * @return list of entities
     */
    List<T> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a single page of entities of type {@code T} matching the provided
     * {@link SearchOrder} and {@link PaginationContext}.
     *
     * @see #findBySearchOrder(SearchOrder)
     *
     * @return paged list of entities
     */
    List<T> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * Counts all persisted entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *            the search specification
     * @return T entity count
     */
    int countBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a single page of entity ids matching the provided {@link SearchOrder}
     * and {@link PaginationContext}.
     *
     * @see #findBySearchOrder(SearchOrder)
     *
     * @return paged list of entity ids
     */
    List<Long> findPageOfIdsBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

}
