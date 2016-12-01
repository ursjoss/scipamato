package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.db.tables.SearchOrder;
import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.persistance.jooq.FilterConditionMapperTest;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;

public class SearchOrderFilterConditionMapperTest extends FilterConditionMapperTest<SearchOrderRecord, ch.difty.sipamato.db.tables.SearchOrder, SearchOrderFilter> {

    private final SearchOrderFilterConditionMapper mapper = new SearchOrderFilterConditionMapper();

    private final SearchOrderFilter filter = new SearchOrderFilter();

    @Override
    protected SearchOrder getTable() {
        return SEARCH_ORDER;
    }

    @Override
    protected GenericFilterConditionMapper<SearchOrderFilter> getMapper() {
        return mapper;
    }

    @Override
    protected SearchOrderFilter getFilter() {
        return filter;
    }

    @Test
    public void creatingWhereCondition_withOwnerIncludingGlobal_searchesForOwnerIdOrGlobal() {
        filter.setOwnerIncludingGlobal(10);
        assertThat(mapper.map(filter).toString()).isEqualTo("(\n  \"PUBLIC\".\"SEARCH_ORDER\".\"OWNER\" = 10\n  or \"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = true\n)");
    }

    @Test
    public void creatingWhereCondition_withOwner_searchesForOwnerId() {
        filter.setOwner(20);
        assertThat(mapper.map(filter).toString()).isEqualTo("\"PUBLIC\".\"SEARCH_ORDER\".\"OWNER\" = 20");
    }

    @Test
    public void creatingWhereCondition_forGlobal_searchesForGlobal() {
        filter.setGlobal(true);
        assertThat(mapper.map(filter).toString()).isEqualTo("\"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = true");
    }

    @Test
    public void creatingWhereCondition_forGlobal_searchesForNotGlobal() {
        filter.setGlobal(false);
        assertThat(mapper.map(filter).toString()).isEqualTo("\"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = false");
    }

}
