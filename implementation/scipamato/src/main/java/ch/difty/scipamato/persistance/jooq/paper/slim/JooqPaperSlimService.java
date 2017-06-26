package ch.difty.scipamato.persistance.jooq.paper.slim;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.persistance.jooq.JooqReadOnlyService;
import ch.difty.scipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.scipamato.service.PaperSlimService;

/**
 * jOOQ specific implementation of the {@link PaperSlimService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperSlimService extends JooqReadOnlyService<Long, PaperSlim, PaperFilter, PaperSlimRepository> implements PaperSlimService {

    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findBySearchOrder(SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext) {
        return getRepository().findPageBySearchOrder(searchOrder, paginationContext);
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }
}
