package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.VERSION;
import static ch.difty.scipamato.core.persistence.user.UserRecordMapperTest.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest;

public class UserUpdateSetStepSetterTest extends UpdateSetStepSetterTest<ScipamatoUserRecord, User> {

    private final UpdateSetStepSetter<ScipamatoUserRecord, User> setter = new UserUpdateSetStepSetter();

    @Mock
    private User entityMock;

    @Override
    protected UpdateSetStepSetter<ScipamatoUserRecord, User> getSetter() {
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
        UserRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixtureExceptAudit() {
        when(getStep().set(SCIPAMATO_USER.USER_NAME, USER_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.FIRST_NAME, FIRST_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.LAST_NAME, LAST_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.EMAIL, EMAIL)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.PASSWORD, PASSWORD)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.ENABLED, ENABLED)).thenReturn(getMoreStep());
    }

    @Override
    protected void stepSetFixtureAudit() {
        when(getMoreStep().set(SCIPAMATO_USER.CREATED, CREATED)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
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
        verify(getStep()).set(SCIPAMATO_USER.USER_NAME, USER_NAME);
        verify(getMoreStep()).set(SCIPAMATO_USER.FIRST_NAME, FIRST_NAME);
        verify(getMoreStep()).set(SCIPAMATO_USER.LAST_NAME, LAST_NAME);
        verify(getMoreStep()).set(SCIPAMATO_USER.EMAIL, EMAIL);
        verify(getMoreStep()).set(SCIPAMATO_USER.PASSWORD, PASSWORD);
        verify(getMoreStep()).set(SCIPAMATO_USER.ENABLED, ENABLED);
    }

    @Override
    protected void verifyStepSettingAudit() {
        verify(getMoreStep()).set(SCIPAMATO_USER.CREATED, CREATED);
        verify(getMoreStep()).set(SCIPAMATO_USER.CREATED_BY, CREATED_BY);
        verify(getMoreStep()).set(SCIPAMATO_USER.LAST_MODIFIED, LAST_MOD);
        verify(getMoreStep()).set(SCIPAMATO_USER.LAST_MODIFIED_BY, LAST_MOD_BY);
        verify(getMoreStep()).set(SCIPAMATO_USER.VERSION, VERSION + 1);
    }

}
