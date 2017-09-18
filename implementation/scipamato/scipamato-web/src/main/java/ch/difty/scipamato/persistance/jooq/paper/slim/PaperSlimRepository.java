package ch.difty.scipamato.persistance.jooq.paper.slim;

import java.util.List;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.PaperFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.scipamato.persistance.jooq.paper.searchorder.BySearchOrderRepository;
import ch.difty.scipamato.persistence.paging.PaginationContext;

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
