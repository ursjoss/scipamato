package ch.difty.sipamato.persistance.jooq.search;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface SearchOrderRepository extends EntityRepository<SearchOrder, Long, SearchOrderFilter> {

    /**
     * Add a new {@link SearchCondition} to the table.
     *
     * @param searchCondition a new (yet unpersisted) search condition
     * @param searchOrderId the id of the parent SearchOrder of the condition
     * @return the updated search condition (including it's id
     */
    SearchCondition addSearchCondition(SearchCondition searchCondition, long searchOrderId);

    /**
     * Updates an already persisted but modified {@link SearchCondition}.
     * @param searchCondition the modifed search condition.
     * @param searchOrderId the id of the parent SearchOrder of the condition
     * @return the persisted search condition.
     */
    SearchCondition updateSearchCondition(SearchCondition searchCondition, long searchOrderId);

    /**
     * Remove SearchCondition from Database.
     * @param searchConditionId
     */
    void deleteSearchConditionWithId(long searchConditionId);

}
