package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.core.db.tables.SearchOrder;
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;

public class SearchOrderFilterConditionMapperTest extends
        FilterConditionMapperTest<SearchOrderRecord, ch.difty.scipamato.core.db.tables.SearchOrder, SearchOrderFilter> {

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
    public void creatingWhereCondition_withNameMaskl_searchesForName() {
        filter.setNameMask("fOo");
        assertThat(mapper.map(filter)
            .toString()).isEqualToIgnoringCase(
                "lower(\"PUBLIC\".\"SEARCH_ORDER\".\"NAME\") like ('%' || 'foo' || '%') escape '!'");
    }

    @Test
    public void creatingWhereCondition_withOwnerIncludingGlobal_searchesForOwnerIdOrGlobal() {
        filter.setOwnerIncludingGlobal(10);
        assertThat(mapper.map(filter)
            .toString()).isEqualToIgnoringCase(
                "(\n  \"PUBLIC\".\"SEARCH_ORDER\".\"OWNER\" = 10\n  or \"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = true\n)");
    }

    @Test
    public void creatingWhereCondition_withOwner_searchesForOwnerId() {
        filter.setOwner(20);
        assertThat(mapper.map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"SEARCH_ORDER\".\"OWNER\" = 20");
    }

    @Test
    public void creatingWhereCondition_forGlobal_searchesForGlobal() {
        filter.setGlobal(true);
        assertThat(mapper.map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = true");
    }

    @Test
    public void creatingWhereCondition_forGlobal_searchesForNotGlobal() {
        filter.setGlobal(false);
        assertThat(mapper.map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"SEARCH_ORDER\".\"GLOBAL\" = false");
    }

}
