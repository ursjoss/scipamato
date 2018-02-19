package ch.difty.scipamato.core.persistence.search;

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
public class JooqSearchOrderService extends
        JooqEntityService<Long, SearchOrder, SearchOrderFilter, SearchOrderRepository> implements SearchOrderService {

    protected JooqSearchOrderService(final SearchOrderRepository repo, final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @Override
    @Transactional(readOnly = false)
    public SearchCondition saveOrUpdateSearchCondition(final SearchCondition searchCondition, final long searchOrderId,
            final String languageCode) {
        if (searchCondition.getSearchConditionId() == null)
            return getRepository().addSearchCondition(searchCondition, searchOrderId, languageCode);
        else
            return getRepository().updateSearchCondition(searchCondition, searchOrderId, languageCode);
    }

    @Override
    @Transactional(readOnly = false)
    public void removeSearchConditionWithId(Long searchConditionId) {
        if (searchConditionId != null)
            getRepository().deleteSearchConditionWithId(searchConditionId);

    }
}
