package ch.difty.scipamato.service;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.persistance.jooq.search.SearchOrderFilter;

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
     * @param languageCode
     * @return the saved search condition
     */
    SearchCondition saveOrUpdateSearchCondition(SearchCondition searchCondition, long searchOrderId, String languageCode);

    /**
     * Remove SearchCondition from Database.
     * @param searchConditionId
     */
    void removeSearchConditionWithId(Long searchConditionId);

}
