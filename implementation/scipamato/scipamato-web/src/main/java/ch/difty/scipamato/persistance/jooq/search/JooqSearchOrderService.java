package ch.difty.scipamato.persistance.jooq.search;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.entity.filter.SearchOrderFilter;
import ch.difty.scipamato.persistance.jooq.JooqEntityService;
import ch.difty.scipamato.persistence.SearchOrderService;

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
