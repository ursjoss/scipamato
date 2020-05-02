package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class UserUpdateSetStepSetterTest : UpdateSetStepSetterTest<ScipamatoUserRecord, User>() {

    override val setter: UpdateSetStepSetter<ScipamatoUserRecord, User> = UserUpdateSetStepSetter()

    override val entity = mockk<User>()

    override fun specificTearDown() {
        confirmVerified(entity)
    }

    override fun entityFixture() {
        UserRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        every { step.set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.PASSWORD, UserRecordMapperTest.PASSWORD) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED) } returns moreStep
    }

    override fun stepSetFixtureAudit() {
        every { moreStep.set(SCIPAMATO_USER.CREATED, RecordMapperTest.CREATED) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.CREATED_BY, RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) } returns moreStep
        every { moreStep.set(SCIPAMATO_USER.VERSION, RecordMapperTest.VERSION + 1) } returns moreStep
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify { entity.userName }
        verify { entity.firstName }
        verify { entity.lastName }
        verify { entity.email }
        verify { entity.password }
        verify { entity.isEnabled }
    }

    override fun verifyStepSettingExceptAudit() {
        verify { step.set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME) }
        verify { moreStep.set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME) }
        verify { moreStep.set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME) }
        verify { moreStep.set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL) }
        verify { moreStep.set(SCIPAMATO_USER.PASSWORD, UserRecordMapperTest.PASSWORD) }
        verify { moreStep.set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED) }
    }

    override fun verifyStepSettingAudit() {
        verify { moreStep.set(SCIPAMATO_USER.CREATED, RecordMapperTest.CREATED) }
        verify { moreStep.set(SCIPAMATO_USER.CREATED_BY, RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(SCIPAMATO_USER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) }
        verify { moreStep.set(SCIPAMATO_USER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) }
        verify { moreStep.set(SCIPAMATO_USER.VERSION, RecordMapperTest.VERSION + 1) }
    }
}
