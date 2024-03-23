package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.core.db.tables.SearchOrder
import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class SearchOrderFilterConditionMapperTest :
    FilterConditionMapperTest<SearchOrderRecord, SearchOrder, SearchOrderFilter>() {

    override val mapper = SearchOrderFilterConditionMapper()

    override val filter = SearchOrderFilter()

    override val table: SearchOrder = SEARCH_ORDER

    @Test
    fun creatingWhereCondition_withNameMask_searchesForName() {
        filter.nameMask = "fOo"
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."search_order"."name" ilike (('%' || replace(
                |  replace(
                |    replace('fOo', '!', '!!'),
                |    '%',
                |    '!%'
                |  ),
                |  '_',
                |  '!_'
                |)) || '%') escape '!'""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withOwnerIncludingGlobal_searchesForOwnerIdOrGlobal() {
        filter.ownerIncludingGlobal = 10
        mapper.map(filter).toString() shouldBeEqualTo
            """(
            |  "public"."search_order"."owner" = 10
            |  or "public"."search_order"."global" = true
            |)""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withOwner_searchesForOwnerId() {
        filter.owner = 20
        mapper.map(filter).toString() shouldBeEqualTo """"public"."search_order"."owner" = 20"""
    }

    @Test
    fun creatingWhereCondition_forGlobal_searchesForGlobal() {
        filter.global = true
        mapper.map(filter).toString() shouldBeEqualTo """"public"."search_order"."global" = true"""
    }

    @Test
    fun creatingWhereCondition_forGlobal_searchesForNotGlobal() {
        filter.global = false
        mapper.map(filter).toString() shouldBeEqualTo """"public"."search_order"."global" = false"""
    }
}
