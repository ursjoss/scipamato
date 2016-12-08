package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.persistance.jooq.search.SearchOrderFilter;

/**
 * The {@link SearchOrderService} interface - defining {@link SearchOrder} specific service methods.
 *
 * @author u.joss
 */
public interface SearchOrderService extends EntityService<Long, SearchOrder, SearchOrderFilter> {

    /**
     * Persists the {@link SearchCondition} provided as parameter.
     *
     * @param searchCondition
     * @param searchOrderId
     * @return the saved search condition
     */
    SearchCondition saveOrUpdateSearchCondition(SearchCondition searchCondition, long searchOrderId);

}
