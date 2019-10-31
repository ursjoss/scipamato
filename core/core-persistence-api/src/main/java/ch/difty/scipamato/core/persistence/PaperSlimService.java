package ch.difty.scipamato.core.persistence;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;

/**
 * The {@link PaperSlimService} interface - defining {@link PaperSlim} specific
 * service methods.
 *
 * @author u.joss
 */
public interface PaperSlimService extends ReadOnlyService<Long, PaperSlim, PaperFilter> {

    /**
     * Find any paper matching the provided {@link SearchOrder}.
     *
     * @param searchOrder
     *     {@link SearchOrder}
     * @return list of {@link PaperSlim}s
     */
    @NotNull
    List<PaperSlim> findBySearchOrder(@NotNull SearchOrder searchOrder);

    /**
     * Finds a page full of {@link PaperSlim}s matching the provided
     * {@link SearchOrder} and {@link PaginationContext}
     *
     * @param searchOrder
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return paged list of papers
     */
    @NotNull
    List<PaperSlim> findPageBySearchOrder(@NotNull SearchOrder searchOrder,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified {@link SearchOrder}.
     *
     * @param searchOrder
     *     {@link SearchOrder}
     * @return paper count
     */
    int countBySearchOrder(@NotNull SearchOrder searchOrder);
}
