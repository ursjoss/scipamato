package ch.difty.scipamato.core.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.RecordMapper;

import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.RecordMapperTest;

class UserRecordMapperTest extends RecordMapperTest<ScipamatoUserRecord, User> {

    public static final int     ID         = 3;
    static final        String  USER_NAME  = "userName";
    static final        String  FIRST_NAME = "firstName";
    static final        String  LAST_NAME  = "lastName";
    static final        String  EMAIL      = "email";
    static final        String  PASSWORD   = "password";
    static final        Boolean ENABLED    = true;

    public static void entityFixtureWithoutIdFields(User entityMock) {
        when(entityMock.getUserName()).thenReturn(USER_NAME);
        when(entityMock.getFirstName()).thenReturn(FIRST_NAME);
        when(entityMock.getLastName()).thenReturn(LAST_NAME);
        when(entityMock.getEmail()).thenReturn(EMAIL);
        when(entityMock.getPassword()).thenReturn(PASSWORD);
        when(entityMock.isEnabled()).thenReturn(ENABLED);
    }

    @Override
    protected RecordMapper<ScipamatoUserRecord, User> getMapper() {
        return new UserRecordMapper();
    }

    @Override
    protected ScipamatoUserRecord makeRecord() {
        return new ScipamatoUserRecord(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, VERSION, CREATED,
            CREATED_BY, LAST_MOD, LAST_MOD_BY);
    }

    @Override
    protected void setAuditFieldsIn(ScipamatoUserRecord record) {
        // no-op
    }

    @Override
    protected void assertEntity(final User role) {
        assertThat(role.getId()).isEqualTo(ID);
        assertThat(role.getUserName()).isEqualTo(USER_NAME);
        assertThat(role.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(role.getLastName()).isEqualTo(LAST_NAME);
        assertThat(role.getEmail()).isEqualTo(EMAIL);
        assertThat(role.getPassword()).isEqualTo(PASSWORD);
        assertThat(role.isEnabled()).isEqualTo(ENABLED);

        assertThat(role.getRoles()).isEmpty();
    }

}
