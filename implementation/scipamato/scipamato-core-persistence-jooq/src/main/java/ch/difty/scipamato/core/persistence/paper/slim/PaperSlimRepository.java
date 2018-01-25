package ch.difty.scipamato.core.persistence.paper.slim;

import java.util.List;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.ReadOnlyRepository;
import ch.difty.scipamato.core.persistence.paper.searchorder.BySearchOrderRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperSlim, Long, PaperFilter> {

    /**
     * {@link BySearchOrderRepository#findBySearchOrder(SearchOrder)}
     */
    List<PaperSlim> findBySearchOrder(SearchOrder searchOrder);

    /**
     * {@link BySearchOrderRepository#findPageBySearchOrder(SearchOrder, PaginationContext)}
     */
    List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * {@link BySearchOrderRepository#countBySearchOrder(SearchOrder)}
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
