package ch.difty.scipamato.core.persistence.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jooq.RecordMapper;

import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.RecordMapperTest;

public class UserRecordMapperTest extends RecordMapperTest<ScipamatoUserRecord, User> {

    public static final int ID = 3;
    public static final String USER_NAME = "userName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final Boolean ENABLED = true;

    public final List<User> users = new ArrayList<>();

    public static void entityFixtureWithoutIdFields(User entityMock) {
        when(entityMock.getUserName()).thenReturn(USER_NAME);
        when(entityMock.getFirstName()).thenReturn(FIRST_NAME);
        when(entityMock.getLastName()).thenReturn(LAST_NAME);
        when(entityMock.getEmail()).thenReturn(EMAIL);
        when(entityMock.getPassword()).thenReturn(PASSWORD);
        when(entityMock.isEnabled()).thenReturn(ENABLED);

        auditFixtureFor(entityMock);
    }

    @Override
    protected RecordMapper<ScipamatoUserRecord, User> getMapper() {
        return new UserRecordMapper();
    }

    @Override
    protected ScipamatoUserRecord makeRecord() {
        return new ScipamatoUserRecord(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, VERSION, CREATED, CREATED_BY, LAST_MOD, LAST_MOD_BY);
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
