package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.core.db.tables.SearchOrder
import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SearchOrderFilterConditionMapperTest :
    FilterConditionMapperTest<SearchOrderRecord, SearchOrder, SearchOrderFilter>() {

    override val mapper = SearchOrderFilterConditionMapper()

    override val filter = SearchOrderFilter()

    override val table: SearchOrder = SEARCH_ORDER

    @Test
    fun creatingWhereCondition_withNameMask_searchesForName() {
        filter.nameMask = "fOo"
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(
            """lower("public"."search_order"."name") like ('%' || replace(
                |  replace(
                |    replace(
                |      'foo', 
                |      '!', 
                |      '!!'
                |    ), 
                |    '%', 
                |    '!%'
                |  ), 
                |  '_', 
                |  '!_'
                |) || '%') escape '!'""".trimMargin()
        )
    }

    @Test
    fun creatingWhereCondition_withOwnerIncludingGlobal_searchesForOwnerIdOrGlobal() {
        filter.ownerIncludingGlobal = 10
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(
            "(\n  \"PUBLIC\".\"SEARCH_ORDER\".\"OWNER\" = 10\n  or \"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = true\n)"
        )
    }

    @Test
    fun creatingWhereCondition_withOwner_searchesForOwnerId() {
        filter.owner = 20
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(""""PUBLIC"."SEARCH_ORDER"."OWNER" = 20""")
    }

    @Test
    fun creatingWhereCondition_forGlobal_searchesForGlobal() {
        filter.global = true
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(""""PUBLIC"."SEARCH_ORDER"."GLOBAL" = true""")
    }

    @Test
    fun creatingWhereCondition_forGlobal_searchesForNotGlobal() {
        filter.global = false
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(""""PUBLIC"."SEARCH_ORDER"."GLOBAL" = false""")
    }
}
