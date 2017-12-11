package ch.difty.scipamato.core.persistence.search;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.SearchCondition;
import ch.difty.scipamato.core.entity.filter.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.JooqEntityService;
import ch.difty.scipamato.core.persistence.SearchOrderService;

/**
 * jOOQ specific implementation of the {@link SearchOrderService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqSearchOrderService extends JooqEntityService<Long, SearchOrder, SearchOrderFilter, SearchOrderRepository> implements SearchOrderService {

    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public SearchCondition saveOrUpdateSearchCondition(final SearchCondition searchCondition, final long searchOrderId, final String languageCode) {
        if (searchCondition.getSearchConditionId() == null) {
            return getRepository().addSearchCondition(searchCondition, searchOrderId, languageCode);
        } else {
            return getRepository().updateSearchCondition(searchCondition, searchOrderId, languageCode);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public void removeSearchConditionWithId(Long searchConditionId) {
        if (searchConditionId != null) {
            getRepository().deleteSearchConditionWithId(searchConditionId);
        }

    }
}
