package ch.difty.sipamato.persistance.jooq.search;

import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.persistance.jooq.JooqEntityService;
import ch.difty.sipamato.service.SearchOrderService;

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
    public SearchCondition saveOrUpdateSearchCondition(SearchCondition searchCondition, long searchOrderId) {
        if (searchCondition.getSearchConditionId() == null) {
            return getRepository().addSearchCondition(searchCondition, searchOrderId);
        } else {
            return getRepository().updateSearchCondition(searchCondition, searchOrderId);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeSearchConditionWithId(Long searchConditionId) {
        if (searchConditionId != null) {
            getRepository().deleteSearchConditionWithId(searchConditionId);
        }

    }
}
