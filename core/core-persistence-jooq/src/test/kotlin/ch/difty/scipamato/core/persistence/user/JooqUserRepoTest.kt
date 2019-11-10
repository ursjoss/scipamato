package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.core.db.tables.ScipamatoUser
import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.search.UserFilter
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.jooq.TableField
import org.mockito.Mockito.verify

internal class JooqUserRepoTest :
    JooqEntityRepoTest<ScipamatoUserRecord, User, Int, ScipamatoUser, UserRecordMapper, UserFilter>() {

    private val userRoleRepoMock = mock<UserRoleRepository>()

    override val sampleId: Int = SAMPLE_ID

    override val unpersistedEntity = mock<User>()
    override val persistedEntity = mock<User>()
    override val persistedRecord = mock<ScipamatoUserRecord>()
    override val unpersistedRecord = mock<ScipamatoUserRecord>()
    override val mapper = mock<UserRecordMapper>()
    override val filter = mock<UserFilter>()

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
        whenever(unpersistedEntity.id).thenReturn(SAMPLE_ID)
        whenever(persistedRecord.id).thenReturn(SAMPLE_ID)
    }

    override fun expectUnpersistedEntityIdNull() {
        whenever(unpersistedEntity.id).thenReturn(null)
    }

    override fun verifyUnpersistedEntityId() {
        verify(unpersistedEntity).id
    }

    override fun verifyPersistedRecordId() {
        verify(persistedRecord).id
    }

    companion object {
        private const val SAMPLE_ID = 3
    }
}
