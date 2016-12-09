package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.db.tables.User;
import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.persistance.jooq.FilterConditionMapperTest;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;

public class UserFilterConditionMapperTest extends FilterConditionMapperTest<UserRecord, ch.difty.sipamato.db.tables.User, UserFilter> {

    private final UserFilterConditionMapper mapper = new UserFilterConditionMapper();

    private final UserFilter filter = new UserFilter();

    @Override
    protected User getTable() {
        return USER;
    }

    @Override
    protected GenericFilterConditionMapper<UserFilter> getMapper() {
        return mapper;
    }

    @Override
    protected UserFilter getFilter() {
        return filter;
    }

    @Test
    public void creatingWhereCondition_withNameMask_searchesUserNameAndFirstNameAndLastName() {
        String pattern = "am";
        filter.setNameMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualTo(makeWhereClause(pattern, "USER_NAME", "FIRST_NAME", "LAST_NAME"));
    }

    @Test
    public void creatingWhereCondition_withEmailMask_searchesEmail() {
        String pattern = "m";
        filter.setEmailMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualTo("lower(\"PUBLIC\".\"USER\".\"EMAIL\") like lower('%m%')");
    }

    @Test
    public void creatingWhereCondition_withEnabledMask_searchesEnabled() {
        boolean pattern = true;
        filter.setEnabled(pattern);
        assertThat(mapper.map(filter).toString()).isEqualTo("\"PUBLIC\".\"USER\".\"ENABLED\" = true");
    }

}
