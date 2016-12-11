package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.EMAIL;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.ENABLED;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.FIRST_NAME;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.LAST_NAME;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.PASSWORD;
import static ch.difty.sipamato.persistance.jooq.user.UserRecordMapperTest.USER_NAME;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetterTest;

public class UserInsertSetStepSetterTest extends InsertSetStepSetterTest<UserRecord, User> {

    private final InsertSetStepSetter<UserRecord, User> setter = new UserInsertSetStepSetter();

    @Mock
    private User entityMock;

    @Mock
    private UserRecord recordMock;

    @Override
    protected InsertSetStepSetter<UserRecord, User> getSetter() {
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
    protected void stepSetFixture() {
        when(getStep().set(USER.USER_NAME, USER_NAME)).thenReturn(getMoreStep());

        when(getMoreStep().set(USER.FIRST_NAME, FIRST_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.LAST_NAME, LAST_NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.EMAIL, EMAIL)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.PASSWORD, PASSWORD)).thenReturn(getMoreStep());
        when(getMoreStep().set(USER.ENABLED, ENABLED)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllNonKeyFields() {
        verify(entityMock).getUserName();
        verify(entityMock).getFirstName();
        verify(entityMock).getLastName();
        verify(entityMock).getEmail();
        verify(entityMock).getPassword();
        verify(entityMock).isEnabled();
    }

    @Override
    protected void verifySetting() {
        verify(getStep()).set(USER.USER_NAME, USER_NAME);

        verify(getMoreStep()).set(USER.FIRST_NAME, FIRST_NAME);
        verify(getMoreStep()).set(USER.LAST_NAME, LAST_NAME);
        verify(getMoreStep()).set(USER.EMAIL, EMAIL);
        verify(getMoreStep()).set(USER.PASSWORD, PASSWORD);
        verify(getMoreStep()).set(USER.ENABLED, ENABLED);
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
        verify(getMoreStep()).set(USER.ID, ID);
    }


    @Test
    public void resettingIdToEntity_withNullRecord_doesNothing() {
        getSetter().resetIdToEntity(entityMock, null);
        verify(entityMock, never()).setId(Mockito.anyInt());
    }

    @Test
    public void resettingIdToEntity_withNonNullRecord_setsId() {
        when(recordMock.getId()).thenReturn(3);
        getSetter().resetIdToEntity(entityMock, recordMock);
        verify(recordMock).getId();
        verify(entityMock).setId(Mockito.anyInt());
    }

}
