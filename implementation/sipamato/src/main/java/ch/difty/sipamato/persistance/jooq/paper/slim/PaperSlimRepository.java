package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.BySearchOrderFinder;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperSlim, Long, PaperFilter> {

    /**
     * {@link BySearchOrderFinder#findBySearchOrder(SearchOrder)}
     */
    List<PaperSlim> findBySearchOrder(SearchOrder searchOrder);

    /**
     * {@link BySearchOrderFinder#findPagedBySearchOrder(SearchOrder, Pageable)}
     */
    Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * {@link BySearchOrderFinder#countBySearchOrder(SearchOrder)}
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
