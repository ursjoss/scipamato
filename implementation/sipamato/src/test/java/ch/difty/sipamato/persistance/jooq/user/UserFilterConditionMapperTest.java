package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.SipamatoUser.SIPAMATO_USER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.db.tables.SipamatoUser;
import ch.difty.sipamato.db.tables.records.SipamatoUserRecord;
import ch.difty.sipamato.persistance.jooq.FilterConditionMapperTest;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;

public class UserFilterConditionMapperTest extends FilterConditionMapperTest<SipamatoUserRecord, ch.difty.sipamato.db.tables.SipamatoUser, UserFilter> {

    private final UserFilterConditionMapper mapper = new UserFilterConditionMapper();

    private final UserFilter filter = new UserFilter();

    @Override
    protected SipamatoUser getTable() {
        return SIPAMATO_USER;
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
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "user_name", "first_name", "last_name"));
    }

    @Test
    public void creatingWhereCondition_withEmailMask_searchesEmail() {
        String pattern = "m";
        filter.setEmailMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("lower(\"public\".\"sipamato_user\".\"email\") like lower('%m%')");
    }

    @Test
    public void creatingWhereCondition_withEnabledMask_searchesEnabled() {
        boolean pattern = true;
        filter.setEnabled(pattern);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"public\".\"sipamato_user\".\"enabled\" = true");
    }

}
