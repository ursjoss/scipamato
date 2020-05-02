package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.RecordMapperTest
import io.mockk.every
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.RecordMapper

internal class UserRecordMapperTest : RecordMapperTest<ScipamatoUserRecord, User>() {

    override val mapper: RecordMapper<ScipamatoUserRecord, User> = UserRecordMapper()

    override fun makeRecord(): ScipamatoUserRecord = ScipamatoUserRecord(
        ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED,
        VERSION, CREATED, CREATED_BY, LAST_MOD, LAST_MOD_BY
    )

    override fun setAuditFieldsIn(record: ScipamatoUserRecord) {
        // no-op
    }

    override fun assertEntity(entity: User) {
        entity.id shouldBeEqualTo ID
        entity.userName shouldBeEqualTo USER_NAME
        entity.firstName shouldBeEqualTo FIRST_NAME
        entity.lastName shouldBeEqualTo LAST_NAME
        entity.email shouldBeEqualTo EMAIL
        entity.password shouldBeEqualTo PASSWORD
        entity.isEnabled shouldBeEqualTo ENABLED

        entity.roles.shouldBeEmpty()
    }

    companion object {
        const val ID = 3
        const val USER_NAME = "userName"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val ENABLED = true

        fun entityFixtureWithoutIdFields(entityMock: User) {
            every { entityMock.userName } returns USER_NAME
            every { entityMock.firstName } returns FIRST_NAME
            every { entityMock.lastName } returns LAST_NAME
            every { entityMock.email } returns EMAIL
            every { entityMock.password } returns PASSWORD
            every { entityMock.isEnabled } returns ENABLED
        }
    }
}
