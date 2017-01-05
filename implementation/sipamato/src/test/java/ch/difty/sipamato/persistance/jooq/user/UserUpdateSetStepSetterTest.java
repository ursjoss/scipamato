package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;
import static ch.difty.sipamato.persistance.jooq.RecordMapperTest.CREATED;
import static ch.difty.sipamato.persistance.jooq.RecordMapperTest.CREATED_BY;
import static ch.difty.sipamato.persistance.jooq.RecordMapperTest.LAST_MOD;
import static ch.difty.sipamato.persistance.jooq.RecordMapperTest.LAST_MOD_BY;
import static ch.difty.sipamato.persistance.jooq.RecordMapperTest.VERSION;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.EMAIL;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.ENABLED;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.FIRST_NAME;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.LAST_NAME;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.PASSWORD;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.USER_NAME;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetterTest;

public class UserUpdateSetStepSetterTest extends UpdateSetStepSetterTest<UserRecord, User> {

    private final UpdateSetStepSetter<UserRecord, User> setter = new UserUpdateSetStepSetter();

    @Mock
    private User entityMock;

    @Override
    protected UpdateSetStepSetter<UserRecord, User> getSetter() {
        return setter;
    }

    @Override
    protected User getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock);
    }

    @Override
    protected void entityFixture() {
        when(entityMock.getId()).thenReturn(ID);
        UserRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixtureExceptAudit() {
        when(getStep().set(USER.USER_NAME, USER_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.FIRST_NAME, FIRST_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.LAST_NAME, LAST_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.EMAIL, EMAIL)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.PASSWORD, PASSWORD)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.ENABLED, ENABLED)).thenReturn(getMoreStep());
    }

    @Override
    protected void stepSetFixtureAudit() {
        when(getMoreStep().set(USER.CREATED, CREATED)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.VERSION, VERSION)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllFieldsExceptAudit() {
        verify(entityMock).getUserName();
        verify(entityMock).getFirstName();
        verify(entityMock).getLastName();
        verify(entityMock).getEmail();
        verify(entityMock).getPassword();
        verify(entityMock).isEnabled();
    }

    @Override
    protected void verifyStepSettingExceptAudit() {
        verify(getStep()).set(USER.USER_NAME, USER_NAME);
        verify(getMoreStep()).set(USER.FIRST_NAME, FIRST_NAME);
        verify(getMoreStep()).set(USER.LAST_NAME, LAST_NAME);
        verify(getMoreStep()).set(USER.EMAIL, EMAIL);
        verify(getMoreStep()).set(USER.PASSWORD, PASSWORD);
        verify(getMoreStep()).set(USER.ENABLED, ENABLED);
    }

    @Override
    protected void verifyStepSettingAudit() {
        verify(getMoreStep()).set(USER.CREATED, CREATED);
        verify(getMoreStep()).set(USER.CREATED_BY, CREATED_BY);
        verify(getMoreStep()).set(USER.LAST_MODIFIED, LAST_MOD);
        verify(getMoreStep()).set(USER.LAST_MODIFIED_BY, LAST_MOD_BY);
        verify(getMoreStep()).set(USER.VERSION, VERSION + 1);
    }

}
