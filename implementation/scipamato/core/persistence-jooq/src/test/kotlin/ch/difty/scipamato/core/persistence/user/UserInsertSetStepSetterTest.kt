package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.InsertSetStepSetter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt

internal class UserInsertSetStepSetterTest : InsertSetStepSetterTest<ScipamatoUserRecord, User>() {

    override val setter: InsertSetStepSetter<ScipamatoUserRecord, User> = UserInsertSetStepSetter()

    override val entity = mock<User>()
    private val recordMock = mock<ScipamatoUserRecord>()

    override fun specificTearDown() {
        verifyNoMoreInteractions(entity, recordMock)
    }

    override fun entityFixture() {
        UserRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.PASSWORD, UserRecordMapperTest.PASSWORD)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED)
    }

    override fun setStepFixtureAudit() {
        doReturn(moreStep).whenever(moreStep)
            .set(SCIPAMATO_USER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY)
        doReturn(moreStep).whenever(moreStep)
            .set(SCIPAMATO_USER.LAST_MODIFIED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY)
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify(entity).userName
        verify(entity).firstName
        verify(entity).lastName
        verify(entity).email
        verify(entity).password
        verify(entity).isEnabled
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify(step).set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME)

        verify(moreStep).set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME)
        verify(moreStep).set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME)
        verify(moreStep).set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL)
        verify(moreStep).set(SCIPAMATO_USER.PASSWORD, UserRecordMapperTest.PASSWORD)
        verify(moreStep).set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED)
    }

    override fun verifySettingAuditFields() {
        verify(moreStep).set(SCIPAMATO_USER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY)
        verify(moreStep)
            .set(SCIPAMATO_USER.LAST_MODIFIED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY)
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        whenever(entity.id).thenReturn(null)
        setter.considerSettingKeyOf(moreStep, entity)
        verify<User>(entity).id
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        whenever(entity.id).thenReturn(UserRecordMapperTest.ID)
        setter.considerSettingKeyOf(moreStep, entity)
        verify<User>(entity).id
        verify(moreStep).set(SCIPAMATO_USER.ID, UserRecordMapperTest.ID)
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entity, null)
        verify(entity, never()).id = anyInt()
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        whenever(recordMock.id).thenReturn(3)
        setter.resetIdToEntity(entity, recordMock)
        verify(recordMock).id
        verify<User>(entity).id = anyInt()
    }
}
