package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper
import ch.difty.scipamato.common.persistence.FilterConditionMapper
import ch.difty.scipamato.core.db.tables.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * Mapper turning the provider [SearchOrderFilter] into a jOOQ [Condition].
 */
@FilterConditionMapper
class SearchOrderFilterConditionMapper : AbstractFilterConditionMapper<SearchOrderFilter>() {

    override fun internalMap(filter: SearchOrderFilter): List<Condition> {
        val conditions = mutableListOf<Condition>()
        if (filter.ownerIncludingGlobal != null) {
            conditions.add(
                DSL.or(
                    SearchOrder.SEARCH_ORDER.OWNER.equal(filter.ownerIncludingGlobal),
                    SearchOrder.SEARCH_ORDER.GLOBAL.equal(true)
                )
            )
        } else {
            filter.nameMask?.let { mask ->
                conditions.add(
                    DSL.lower(SearchOrder.SEARCH_ORDER.NAME).contains(mask.toLowerCase())
                )
            }
            filter.owner?.let { conditions.add(SearchOrder.SEARCH_ORDER.OWNER.equal(it)) }
            filter.global?.let { conditions.add(SearchOrder.SEARCH_ORDER.GLOBAL.equal(it)) }
        }
        return conditions
    }
}
