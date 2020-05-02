package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.ScipamatoUser
import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.search.UserFilter
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jooq.TableField

internal class JooqUserRepoTest :
    JooqEntityRepoTest<ScipamatoUserRecord, User, Int, ScipamatoUser, UserRecordMapper, UserFilter>() {

    private val userRoleRepoMock = mockk<UserRoleRepository>()

    override val sampleId: Int = SAMPLE_ID

    override val unpersistedEntity = mockk<User>()
    override val persistedEntity = mockk<User>()
    override val persistedRecord = mockk<ScipamatoUserRecord>()
    override val unpersistedRecord = mockk<ScipamatoUserRecord>()
    override val mapper = mockk<UserRecordMapper>()
    override val filter = mockk<UserFilter>()

    override val table: ScipamatoUser = SCIPAMATO_USER

    override val tableId: TableField<ScipamatoUserRecord, Int> = SCIPAMATO_USER.ID

    override val recordVersion: TableField<ScipamatoUserRecord, Int> = SCIPAMATO_USER.VERSION

    override val repo: JooqUserRepo = JooqUserRepo(
        dsl,
        mapper,
        sortMapper,
        filterConditionMapper,
        dateTimeService,
        insertSetStepSetter,
        updateSetStepSetter,
        applicationProperties,
        userRoleRepoMock
    )

    override fun makeRepoFindingEntityById(entity: User): EntityRepository<User, Int, UserFilter> =
        object : JooqUserRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties,
            userRoleRepoMock
        ) {
            override fun findById(id: Int, version: Int): User = entity
        }

    override fun makeRepoSavingReturning(returning: ScipamatoUserRecord): EntityRepository<User, Int, UserFilter> =
        object : JooqUserRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties,
            userRoleRepoMock
        ) {
            override fun doSave(entity: User, languageCode: String): ScipamatoUserRecord = returning
        }

    override fun expectEntityIdsWithValues() {
        every { unpersistedEntity.id } returns SAMPLE_ID
        every { persistedRecord.id } returns SAMPLE_ID
    }

    override fun expectUnpersistedEntityIdNull() {
        every { unpersistedEntity.id } returns null
    }

    override fun verifyUnpersistedEntityId() {
        verify { unpersistedEntity.id }
        verify { unpersistedEntity.toString() }
    }

    override fun verifyPersistedRecordId() {
        verify { persistedRecord.id }
    }

    companion object {
        private const val SAMPLE_ID = 3
    }
}
