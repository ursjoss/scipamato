package ch.difty.scipamato.core.persistence.paper.slim;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.ReadOnlyRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperSlim, Long, PaperFilter> {

    /**
     * Finds all entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *     {@link SearchOrder} the search specification
     * @return list of entities
     */
    @NotNull
    List<PaperSlim> findBySearchOrder(@NotNull SearchOrder searchOrder);

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
    List<PaperSlim> findPageBySearchOrder(@NotNull SearchOrder searchOrder,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts all persisted entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *     the search specification
     * @return T entity count
     */
    int countBySearchOrder(@NotNull SearchOrder searchOrder);
}
