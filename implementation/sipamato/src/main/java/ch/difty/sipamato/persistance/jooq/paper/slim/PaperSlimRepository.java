package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.SearchOrderRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperSlim, Long, PaperFilter> {

    /**
     * {@link SearchOrderRepository#findBySearchOrder(SearchOrder)}
     */
    List<PaperSlim> findBySearchOrder(SearchOrder searchOrder);

    /**
     * {@link SearchOrderRepository#findPageBySearchOrder(SearchOrder, PaginationContext)}
     */
    List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * {@link SearchOrderRepository#countBySearchOrder(SearchOrder)}
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
