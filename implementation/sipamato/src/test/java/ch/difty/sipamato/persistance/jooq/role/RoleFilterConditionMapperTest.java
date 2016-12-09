package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.db.tables.Role;
import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.persistance.jooq.FilterConditionMapperTest;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;

public class RoleFilterConditionMapperTest extends FilterConditionMapperTest<RoleRecord, ch.difty.sipamato.db.tables.Role, RoleFilter> {

    private final RoleFilterConditionMapper mapper = new RoleFilterConditionMapper();

    private final RoleFilter filter = new RoleFilter();

    @Override
    protected Role getTable() {
        return ROLE;
    }

    @Override
    protected GenericFilterConditionMapper<RoleFilter> getMapper() {
        return mapper;
    }

    @Override
    protected RoleFilter getFilter() {
        return filter;
    }

    @Test
    public void creatingWhereCondition_withNameMask_searchesName() {
        String pattern = "am";
        filter.setNameMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualTo("lower(\"PUBLIC\".\"ROLE\".\"NAME\") like lower('%am%')");
    }

    @Test
    public void creatingWhereCondition_withCommentMask_searchesComment() {
        String pattern = "m";
        filter.setCommentMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualTo("lower(\"PUBLIC\".\"ROLE\".\"COMMENT\") like lower('%m%')");
    }

}
