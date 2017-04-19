package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.SearchOrderRepository;

public interface PaperRepository extends EntityRepository<Paper, Long, PaperFilter> {

    /**
     * Find Papers with the provided ids.  The codes are not enriched.
     * @param ids
     * @return list of papers (codes not available)
     */
    List<Paper> findByIds(List<Long> ids);

    /**
     * Find Papers (including codes) with the provided ids
     * @param ids
     * @return list of papers
     */
    List<Paper> findWithCodesByIds(List<Long> ids);

    /**
     * {@link SearchOrderRepository#findBySearchOrder(SearchOrder)}
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder);

    /**
     * {@link SearchOrderRepository#findPageBySearchOrder(SearchOrder, Pageable)}
     */
    List<Paper> findPageBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * {@link SearchOrderRepository#countBySearchOrder(SearchOrder)}
     */
    int countBySearchOrder(SearchOrder searchOrder);

    /**
     * Find Papers by a number of PmIds
     * @param pmIds
     * @return list of {@Paper}s
     */
    List<Paper> findByPmIds(List<Integer> pmIds);

}
