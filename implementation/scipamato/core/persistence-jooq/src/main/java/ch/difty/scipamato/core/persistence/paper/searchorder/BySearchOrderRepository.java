package ch.difty.scipamato.core.persistence.paper.searchorder;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.search.SearchOrder;

/**
 * Repository to find Papers or PaperSlims by {@link SearchOrder}
 * specifications.
 *
 * @param <T>
 *     derivatives of {@link IdScipamatoEntity}, should actually be Paper
 *     or PaperSlim
 * @author u.joss
 */
public interface BySearchOrderRepository<T extends IdScipamatoEntity<Long>> {

    /**
     * Finds all entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *     {@link SearchOrder} the search specification
     * @return list of entities
     */
    @NotNull
    List<T> findBySearchOrder(@NotNull SearchOrder searchOrder);

    /**
     * Finds a single page of entities of type {@code T} matching the provided
     * {@link SearchOrder} and {@link PaginationContext}.
     *
     * @param searchOrder
     *     the search specification
     * @param paginationContext
     *     the pagination specification
     * @return paged list of entities
     * @see #findBySearchOrder(SearchOrder)
     */
    @NotNull
    List<T> findPageBySearchOrder(@NotNull SearchOrder searchOrder, @NotNull PaginationContext paginationContext);

    /**
     * Counts all persisted entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *     the search specification
     * @return T entity count
     */
    int countBySearchOrder(@NotNull SearchOrder searchOrder);

    /**
     * Finds a single page of entity ids matching the provided {@link SearchOrder}
     * and {@link PaginationContext}.
     *
     * @param searchOrder
     *     the search specification
     * @param paginationContext
     *     the pagination specification
     * @return paged list of entity ids
     * @see #findBySearchOrder(SearchOrder)
     */
    @NotNull
    List<Long> findPageOfIdsBySearchOrder(@NotNull SearchOrder searchOrder,
        @NotNull PaginationContext paginationContext);
}
