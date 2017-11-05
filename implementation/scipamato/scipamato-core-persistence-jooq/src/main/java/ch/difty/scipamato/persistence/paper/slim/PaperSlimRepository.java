package ch.difty.scipamato.persistence.paper.slim;

import java.util.List;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.PaperFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistence.ReadOnlyRepository;
import ch.difty.scipamato.persistence.paging.PaginationContext;
import ch.difty.scipamato.persistence.paper.searchorder.BySearchOrderRepository;

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
