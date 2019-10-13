package ch.difty.scipamato.core.persistence.paper.slim;

import java.util.List;

import org.jetbrains.annotations.NotNull;
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

    protected JooqPaperSlimService(@NotNull final PaperSlimRepository repo, @NotNull final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @NotNull
    @Override
    public List<PaperSlim> findBySearchOrder(@NotNull SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    @NotNull
    @Override
    public List<PaperSlim> findPageBySearchOrder(@NotNull SearchOrder searchOrder,
        @NotNull PaginationContext paginationContext) {
        return getRepository().findPageBySearchOrder(searchOrder, paginationContext);
    }

    @Override
    public int countBySearchOrder(@NotNull SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }
}
