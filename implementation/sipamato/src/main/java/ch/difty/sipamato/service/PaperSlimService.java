package ch.difty.sipamato.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * The {@link PaperSlimService} interface - defining {@link PaperSlim} specific service methods.
 *
 * @author u.joss
 */
public interface PaperSlimService extends ReadOnlyService<PaperSlim, Long, SimplePaperFilter> {

    /**
     * Find any paper matching the provided {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a page of records of type <literal>T</literal> matching the provided {@link SearchOrder},
     * returned in pages.
     *
     * @param searchOrder the filter
     * @param pageable defining paging and sorting
     * @return a list of entities of type <literal>T</literal>
     */
    Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * Counts the number of entities matching the specified {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return entity count
     */
    int countByFilter(SearchOrder searchOrder);

}
