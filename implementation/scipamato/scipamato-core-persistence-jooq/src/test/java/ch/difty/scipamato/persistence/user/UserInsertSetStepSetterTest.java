package ch.difty.scipamato.persistence.user;

import static ch.difty.scipamato.db.tables.ScipamatoUser.*;
import static ch.difty.scipamato.persistence.user.UserRecordMapperTest.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.persistence.InsertSetStepSetter;
import ch.difty.scipamato.persistence.InsertSetStepSetterTest;

public class UserInsertSetStepSetterTest extends InsertSetStepSetterTest<ScipamatoUserRecord, User> {

    private final InsertSetStepSetter<ScipamatoUserRecord, User> setter = new UserInsertSetStepSetter();

    @Mock
    private User entityMock;

    @Mock
    private ScipamatoUserRecord recordMock;

    @Override
    protected InsertSetStepSetter<ScipamatoUserRecord, User> getSetter() {
        return setter;
    }

    @Override
    protected User getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock, recordMock);
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
    protected void setStepFixtureAudit() {
        when(getMoreStep().set(SCIPAMATO_USER.CREATED_BY, UserRecordMapperTest.CREATED_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(SCIPAMATO_USER.LAST_MODIFIED_BY, UserRecordMapperTest.LAST_MOD_BY)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToFieldsExceptKeyAndAudit() {
        verify(entityMock).getUserName();
        verify(entityMock).getFirstName();
        verify(entityMock).getLastName();
        verify(entityMock).getEmail();
        verify(entityMock).getPassword();
        verify(entityMock).isEnabled();
    }

    @Override
    protected void verifySettingFieldsExceptKeyAndAudit() {
        verify(getStep()).set(SCIPAMATO_USER.USER_NAME, USER_NAME);

        verify(getMoreStep()).set(SCIPAMATO_USER.FIRST_NAME, FIRST_NAME);
        verify(getMoreStep()).set(SCIPAMATO_USER.LAST_NAME, LAST_NAME);
        verify(getMoreStep()).set(SCIPAMATO_USER.EMAIL, EMAIL);
        verify(getMoreStep()).set(SCIPAMATO_USER.PASSWORD, PASSWORD);
        verify(getMoreStep()).set(SCIPAMATO_USER.ENABLED, ENABLED);
    }

    @Override
    protected void verifySettingAuditFields() {
        verify(getMoreStep()).set(SCIPAMATO_USER.CREATED_BY, UserRecordMapperTest.CREATED_BY);
        verify(getMoreStep()).set(SCIPAMATO_USER.LAST_MODIFIED_BY, UserRecordMapperTest.LAST_MOD_BY);
    }

    @Test
    public void consideringSettingKeyOf_withNullId_doesNotSetId() {
        when(getEntity().getId()).thenReturn(null);
        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());
        verify(getEntity()).getId();
    }

    @Test
    public void consideringSettingKeyOf_withNonNullId_doesSetId() {
        when(getEntity().getId()).thenReturn(ID);

        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());

        verify(getEntity()).getId();
        verify(getMoreStep()).set(SCIPAMATO_USER.ID, ID);
    }

    @Test
    public void resettingIdToEntity_withNullRecord_doesNothing() {
        getSetter().resetIdToEntity(entityMock, null);
        verify(entityMock, never()).setId(anyInt());
    }

    @Test
    public void resettingIdToEntity_withNonNullRecord_setsId() {
        when(recordMock.getId()).thenReturn(3);
        getSetter().resetIdToEntity(entityMock, recordMock);
        verify(recordMock).getId();
        verify(entityMock).setId(anyInt());
    }

}
