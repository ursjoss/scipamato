package ch.difty.scipamato.core.persistence.search;

import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;

/**
 * Repository to manage SearchOrders and the related sub-entities.
 *
 * @author u.joss
 */
public interface SearchOrderRepository extends EntityRepository<SearchOrder, Long, SearchOrderFilter> {

    /**
     * Add a new {@link SearchCondition} to the table.
     *
     * @param searchCondition
     *            a new (yet unpersisted) search condition
     * @param searchOrderId
     *            the id of the parent SearchOrder of the condition
     * @param languageCode
     *            Must not be null.
     * @return the updated search condition (including it's id
     */
    SearchCondition addSearchCondition(SearchCondition searchCondition, long searchOrderId, String languageCode);

    /**
     * Updates an already persisted but modified {@link SearchCondition}.
     *
     * @param searchCondition
     *            the modifed search condition.
     * @param searchOrderId
     *            the id of the parent SearchOrder of the condition
     * @param languageCode
     *            Must not be null.
     * @return the persisted search condition.
     */
    SearchCondition updateSearchCondition(SearchCondition searchCondition, long searchOrderId, String languageCode);

    /**
     * Remove SearchCondition from Database.
     *
     * @param searchConditionId
     */
    void deleteSearchConditionWithId(long searchConditionId);

}
