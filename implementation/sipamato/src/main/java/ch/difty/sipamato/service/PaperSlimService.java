package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

/**
 * The {@link PaperSlimService} interface - defining {@link PaperSlim} specific service methods.
 *
 * @author u.joss
 */
public interface PaperSlimService extends ReadOnlyService<Long, PaperSlim, PaperFilter> {

    /**
     * Find any paper matching the provided {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a page full of {@link PaperSlim}s matching the provided {@link SearchOrder},
     * and pagination specification
     *
     * @param searchOrder the filter
     * @param pageable defining paging and sorting
     * @return paged list of papers
     */
    List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * Counts the number of entities matching the specified {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return paper count
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
