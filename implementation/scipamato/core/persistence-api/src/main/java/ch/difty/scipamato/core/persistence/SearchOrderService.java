package ch.difty.scipamato.core.persistence;

import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;

/**
 * The {@link SearchOrderService} interface - defining {@link SearchOrder}
 * specific service methods.
 *
 * @author u.joss
 */
public interface SearchOrderService extends EntityService<Long, SearchOrder, SearchOrderFilter> {

    /**
     * Persists the {@link SearchCondition} provided as parameter.
     *
     * @param searchCondition
     *     the search condition to update
     * @param searchOrderId
     *     the id of the search order the condition belongs to
     * @param languageCode
     *     the language code that was used in the search condition
     * @return the saved search condition
     */
    SearchCondition saveOrUpdateSearchCondition(SearchCondition searchCondition, long searchOrderId,
        String languageCode);

    /**
     * Remove SearchCondition from Database.
     *
     * @param searchConditionId
     *     the id of the search condition that shall be removed
     */
    void removeSearchConditionWithId(Long searchConditionId);

}
