package ch.difty.scipamato.core.persistence.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.JooqEntityService;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.persistence.UserRepository;

/**
 * jOOQ specific implementation of the {@link SearchOrderService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqSearchOrderService extends JooqEntityService<Long, SearchOrder, SearchOrderFilter, SearchOrderRepository>
    implements SearchOrderService {

    protected JooqSearchOrderService(@NotNull final SearchOrderRepository repo, @NotNull final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @Nullable
    @Override
    @Transactional
    public SearchCondition saveOrUpdateSearchCondition(@NotNull final SearchCondition searchCondition, final long searchOrderId,
        @NotNull final String languageCode) {
        if (searchCondition.getSearchConditionId() == null)
            return getRepository().addSearchCondition(searchCondition, searchOrderId, languageCode);
        else
            return getRepository().updateSearchCondition(searchCondition, searchOrderId, languageCode);
    }

    @Override
    @Transactional
    public void removeSearchConditionWithId(@Nullable Long searchConditionId) {
        if (searchConditionId != null)
            getRepository().deleteSearchConditionWithId(searchConditionId);
    }
}
