package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.InsertSetStepSetter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class UserInsertSetStepSetterTest : InsertSetStepSetterTest<ScipamatoUserRecord, User>() {

    override val setter: InsertSetStepSetter<ScipamatoUserRecord, User> = UserInsertSetStepSetter()

    override val entity = mockk<User>(relaxed = true)
    private val recordMock = mockk<ScipamatoUserRecord>()

    override fun specificTearDown() {
        confirmVerified(entity, recordMock)
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

    override fun setStepFixtureAudit() {
        every {
            moreStep.set(SCIPAMATO_USER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY)
        } returns moreStep
        every {
            moreStep.set(
                SCIPAMATO_USER.LAST_MODIFIED_BY,
                ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY
            )
        } returns moreStep
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify { entity.userName }
        verify { entity.firstName }
        verify { entity.lastName }
        verify { entity.email }
        verify { entity.password }
        verify { entity.isEnabled }
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify { step.set(SCIPAMATO_USER.USER_NAME, UserRecordMapperTest.USER_NAME) }

        verify { moreStep.set(SCIPAMATO_USER.FIRST_NAME, UserRecordMapperTest.FIRST_NAME) }
        verify { moreStep.set(SCIPAMATO_USER.LAST_NAME, UserRecordMapperTest.LAST_NAME) }
        verify { moreStep.set(SCIPAMATO_USER.EMAIL, UserRecordMapperTest.EMAIL) }
        verify { moreStep.set(SCIPAMATO_USER.PASSWORD, UserRecordMapperTest.PASSWORD) }
        verify { moreStep.set(SCIPAMATO_USER.ENABLED, UserRecordMapperTest.ENABLED) }
    }

    override fun verifySettingAuditFields() {
        verify {
            moreStep.set(SCIPAMATO_USER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY)
        }
        verify {
            moreStep.set(
                SCIPAMATO_USER.LAST_MODIFIED_BY,
                ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY,
            )
        }
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        every { entity.id } returns null
        setter.considerSettingKeyOf(moreStep, entity)
        verify { entity.id }
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        every { moreStep.set(SCIPAMATO_USER.ID, UserRecordMapperTest.ID) } returns moreStep
        every { entity.id } returns UserRecordMapperTest.ID
        setter.considerSettingKeyOf(moreStep, entity)
        verify { entity.id }
        verify { moreStep.set(SCIPAMATO_USER.ID, UserRecordMapperTest.ID) }
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entity, null)
        verify(exactly = 0) { entity.id = any() }
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        every { moreStep.set(SCIPAMATO_USER.ID, UserRecordMapperTest.ID) } returns moreStep
        every { recordMock.id } returns 3
        setter.resetIdToEntity(entity, recordMock)
        verify { recordMock.id }
        verify { entity.id = any() }
    }
}
