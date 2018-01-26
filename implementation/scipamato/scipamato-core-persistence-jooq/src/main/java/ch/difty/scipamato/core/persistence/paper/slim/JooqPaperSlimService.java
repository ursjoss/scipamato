package ch.difty.scipamato.core.persistence.paper.slim;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqReadOnlyService;
import ch.difty.scipamato.core.persistence.PaperSlimService;
import ch.difty.scipamato.core.persistence.UserRepository;

/**
 * jOOQ specific implementation of the {@link PaperSlimService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperSlimService extends JooqReadOnlyService<Long, PaperSlim, PaperFilter, PaperSlimRepository>
        implements PaperSlimService {

    private static final long serialVersionUID = 1L;

    protected JooqPaperSlimService(final PaperSlimRepository repo, final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @Override
    public List<PaperSlim> findBySearchOrder(SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    @Override
    public List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext) {
        return getRepository().findPageBySearchOrder(searchOrder, paginationContext);
    }

    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }
}
