package ch.difty.scipamato.persistance.jooq.user;

import static ch.difty.scipamato.db.tables.ScipamatoUser.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.db.tables.ScipamatoUser;
import ch.difty.scipamato.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.entity.filter.UserFilter;
import ch.difty.scipamato.persistance.jooq.FilterConditionMapperTest;
import ch.difty.scipamato.persistance.jooq.GenericFilterConditionMapper;

public class UserFilterConditionMapperTest extends FilterConditionMapperTest<ScipamatoUserRecord, ch.difty.scipamato.db.tables.ScipamatoUser, UserFilter> {

    private final UserFilterConditionMapper mapper = new UserFilterConditionMapper();

    private final UserFilter filter = new UserFilter();

    @Override
    protected ScipamatoUser getTable() {
        return SCIPAMATO_USER;
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
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("lower(\"public\".\"scipamato_user\".\"email\") like lower('%m%')");
    }

    @Test
    public void creatingWhereCondition_withEnabledMask_searchesEnabled() {
        boolean pattern = true;
        filter.setEnabled(pattern);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"public\".\"scipamato_user\".\"enabled\" = true");
    }

}
