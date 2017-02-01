package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.BySearchOrderFinder;

public interface PaperRepository extends EntityRepository<PaperRecord, Paper, Long, PaperRecordMapper, PaperFilter> {

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
     * {@link BySearchOrderFinder#findBySearchOrder(SearchOrder)}
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder);

    /**
     * {@link BySearchOrderFinder#findPagedBySearchOrder(SearchOrder, Pageable)}
     */
    Page<Paper> findBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * {@link BySearchOrderFinder#countBySearchOrder(SearchOrder)}
     */
    int countBySearchOrder(SearchOrder searchOrder);
}
