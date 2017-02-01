package ch.difty.sipamato.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 *
 * @author u.joss
 */
public interface PaperService extends EntityService<Long, Paper, PaperFilter> {

    /**
     * Find the papers with the given ids. (codes are not enriched)
     * @param ids
     * @return list of papers
     */
    @Deprecated
    List<Paper> findByIds(List<Long> ids);

    /**
     * Find the papers (enriched with codes) with the given ids.
     * @param ids
     * @return list of papers
     */
    @Deprecated
    List<Paper> findWithCodesByIds(List<Long> ids);

    /**
     * Find any paper matching the provided {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return list of {@link Paper}s
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a page of papers matching the provided {@link SearchOrder}, returned in pages.
     *
     * @param searchOrder the filter
     * @param pageable defining paging and sorting
     * @return a list of papers
     */
    Page<Paper> findBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * Counts the number of entities matching the specified {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return paper count
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
