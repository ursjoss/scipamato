package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.core.db.tables.ScipamatoUser;
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.search.UserFilter;

public class UserFilterConditionMapperTest extends
    FilterConditionMapperTest<ScipamatoUserRecord, ch.difty.scipamato.core.db.tables.ScipamatoUser, UserFilter> {

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
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "user_name", "first_name", "last_name"));
    }

    @Test
    public void creatingWhereCondition_withEmailMask_searchesEmail() {
        String pattern = "m";
        filter.setEmailMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("lower(\"public\".\"scipamato_user\".\"email\") like lower('%m%')");
    }

    @Test
    public void creatingWhereCondition_withEnabledMask_searchesEnabled() {
        boolean pattern = true;
        filter.setEnabled(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"public\".\"scipamato_user\".\"enabled\" = true");
    }

}
