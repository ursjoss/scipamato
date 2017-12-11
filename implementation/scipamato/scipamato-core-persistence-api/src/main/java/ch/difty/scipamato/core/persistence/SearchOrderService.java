package ch.difty.scipamato.core.persistence;

import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.SearchCondition;
import ch.difty.scipamato.core.entity.filter.SearchOrderFilter;

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
