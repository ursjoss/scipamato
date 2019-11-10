package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.RecordMapperTest
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(entity.id).isEqualTo(ID)
        assertThat(entity.userName).isEqualTo(USER_NAME)
        assertThat(entity.firstName).isEqualTo(FIRST_NAME)
        assertThat(entity.lastName).isEqualTo(LAST_NAME)
        assertThat(entity.email).isEqualTo(EMAIL)
        assertThat(entity.password).isEqualTo(PASSWORD)
        assertThat(entity.isEnabled).isEqualTo(ENABLED)

        assertThat(entity.roles).isEmpty()
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
            whenever(entityMock.userName).thenReturn(USER_NAME)
            whenever(entityMock.firstName).thenReturn(FIRST_NAME)
            whenever(entityMock.lastName).thenReturn(LAST_NAME)
            whenever(entityMock.email).thenReturn(EMAIL)
            whenever(entityMock.password).thenReturn(PASSWORD)
            whenever(entityMock.isEnabled).thenReturn(ENABLED)
        }
    }
}
