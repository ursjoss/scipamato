package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.jooq.UpdateSetMoreStep
import org.mockito.Mockito.*

internal class UserUpdateSetStepSetterWithoutPasswordTest : UpdateSetStepSetterTest<ScipamatoUserRecord, User>() {

    override val setter: UpdateSetStepSetter<ScipamatoUserRecord, User> = UserUpdateSetStepSetter()

    override val entity = mock<User>()

    override fun specificTearDown() {
        verifyNoMoreInteractions(entity)
    }

    override fun entityFixture() {
        whenever(entity.userName).thenReturn(UserRecordMapperTest.USER_NAME)
        whenever(entity.firstName).thenReturn(UserRecordMapperTest.FIRST_NAME)
        whenever(entity.lastName).thenReturn(UserRecordMapperTest.LAST_NAME)
        whenever(entity.email).thenReturn(UserRecordMapperTest.EMAIL)
        whenever(entity.isEnabled).thenReturn(UserRecordMapperTest.ENABLED)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED)
    }

    override fun stepSetFixtureAudit() {
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.CREATED, RecordMapperTest.CREATED)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.CREATED_BY, RecordMapperTest.CREATED_BY)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.LAST_MODIFIED, RecordMapperTest.LAST_MOD)
        doReturn(moreStep).whenever(moreStep).set(SCIPAMATO_USER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify(entity).userName
        verify(entity).firstName
        verify(entity).lastName
        verify(entity).email
        verify(entity).password
        verify(entity).isEnabled
    }

    override fun verifyStepSettingExceptAudit() {
        verify(step).set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME)
        verify(moreStep).set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME)
        verify(moreStep).set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME)
        verify(moreStep).set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL)
        verify(moreStep, never()).set(SCIPAMATO_USER.PASSWORD, UserRecordMapperTest.PASSWORD)
        verify(moreStep).set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED)
    }

    override fun verifyStepSettingAudit() {
        verify<UpdateSetMoreStep<ScipamatoUserRecord>>(moreStep).set(SCIPAMATO_USER.CREATED, RecordMapperTest.CREATED)
        verify<UpdateSetMoreStep<ScipamatoUserRecord>>(moreStep).set(SCIPAMATO_USER.CREATED_BY, RecordMapperTest.CREATED_BY)
        verify<UpdateSetMoreStep<ScipamatoUserRecord>>(moreStep).set(SCIPAMATO_USER.LAST_MODIFIED, RecordMapperTest.LAST_MOD)
        verify<UpdateSetMoreStep<ScipamatoUserRecord>>(moreStep).set(SCIPAMATO_USER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
        verify<UpdateSetMoreStep<ScipamatoUserRecord>>(moreStep).set(SCIPAMATO_USER.VERSION, RecordMapperTest.VERSION + 1)
    }

}
