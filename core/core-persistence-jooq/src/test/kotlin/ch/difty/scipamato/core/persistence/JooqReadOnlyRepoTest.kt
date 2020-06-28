@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper
import ch.difty.scipamato.common.persistence.JooqSortMapper
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Record1
import org.jooq.RecordMapper
import org.jooq.SelectConditionStep
import org.jooq.SelectJoinStep
import org.jooq.SelectSeekStepN
import org.jooq.SelectSelectStep
import org.jooq.SelectWhereStep
import org.jooq.SortField
import org.jooq.TableField
import org.jooq.impl.TableImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class JooqReadOnlyRepoTest<
    R : Record,
    T : IdScipamatoEntity<ID>,
    ID : Number,
    TI : TableImpl<R>,
    M : RecordMapper<R, T>,
    F : ScipamatoFilter> {

    protected val dsl = mockk<DSLContext>()
    protected var filterConditionMapper = mockk<GenericFilterConditionMapper<F>>()
    protected val sortMapper = mockk<JooqSortMapper<R, T, TI>>()
    private val selectWhereStepMock = mockk<SelectWhereStep<R>>()
    private val selectConditionStepMock = mockk<SelectConditionStep<R>>()
    private val selectSelectStepMock = mockk<SelectSelectStep<Record1<Int>>>()
    private val selectJoinStepMock = mockk<SelectJoinStep<Record1<Int>>>()
    private val selectConditionStepMock2 = mockk<SelectConditionStep<Record1<Int>>>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val sortMock = mockk<Sort>()
    private val sortFieldsMock = mockk<Collection<SortField<T>>>()
    private val selectSeekStepNMock = mockk<SelectSeekStepN<R>>()

    protected val conditionMock = mockk<Condition>()
    protected val applicationProperties = mockk<ApplicationProperties>()

    protected abstract val persistedEntity: T

    protected abstract val unpersistedEntity: T

    protected abstract val persistedRecord: R

    protected abstract val unpersistedRecord: R

    protected abstract val mapper: M

    protected abstract val table: TI

    protected abstract val tableId: TableField<R, ID>

    protected abstract val filter: F

    protected abstract val repo: ReadOnlyRepository<T, ID, F>

    /**
     * Hand-rolled spy that returns the provided entity in the method `findById(ID id)`
     */
    protected abstract fun makeRepoFindingEntityById(entity: T): ReadOnlyRepository<T, ID, F>

    protected abstract fun expectEntityIdsWithValues()

    protected abstract fun expectUnpersistedEntityIdNull()

    protected abstract fun verifyUnpersistedEntityId()

    protected abstract fun verifyPersistedRecordId()

    private val entities = mutableListOf<T>()
    private val records = mutableListOf<R>()

    @BeforeEach
    internal fun setUp() {
        entities.add(persistedEntity)
        entities.add(persistedEntity)

        records.add(persistedRecord)
        records.add(persistedRecord)

        specificSetUp()
    }

    protected open fun specificSetUp() {}

    @AfterEach
    internal fun tearDown() {
        specificTearDown()
        confirmVerified(dsl, mapper, sortMapper)
        confirmVerified(unpersistedEntity, persistedEntity, unpersistedRecord, persistedRecord)
        confirmVerified(selectWhereStepMock, selectConditionStepMock)
        confirmVerified(selectSelectStepMock, selectJoinStepMock)
        confirmVerified(paginationContextMock, sortMock, sortFieldsMock, selectSeekStepNMock)
        confirmVerified(filter, conditionMock)
        confirmVerified(applicationProperties)
    }

    protected open fun specificTearDown() {}

    protected open fun specificNullCheck() {}

    @Test
    internal fun countingByFilter() {
        every { filterConditionMapper.map(filter) } returns conditionMock
        every { dsl.selectOne() } returns selectSelectStepMock
        every { selectSelectStepMock.from(table) } returns selectJoinStepMock
        every { selectJoinStepMock.where(any<Condition>()) } returns selectConditionStepMock2
        every { dsl.fetchCount(selectConditionStepMock2) } returns 2

        repo.countByFilter(filter) shouldBeEqualTo 2

        verify { dsl.selectOne() }
        verify { selectSelectStepMock.from(table) }
        verify { selectJoinStepMock.where(any<Condition>()) }
        verify { dsl.fetchCount(selectConditionStepMock2) }
    }
}
