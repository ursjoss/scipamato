package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper
import ch.difty.scipamato.common.persistence.JooqSortMapper
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
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

    protected val dsl = mock<DSLContext>()
    protected var filterConditionMapper = mock<GenericFilterConditionMapper<F>>()
    protected val sortMapper = mock<JooqSortMapper<R, T, TI>>()
    private val selectWhereStepMock = mock<SelectWhereStep<R>>()
    private val selectConditionStepMock = mock<SelectConditionStep<R>>()
    private val selectSelectStepMock = mock<SelectSelectStep<Record1<Int>>>()
    private val selectJoinStepMock = mock<SelectJoinStep<Record1<Int>>>()
    private val selectConditionStepMock2 = mock<SelectConditionStep<Record1<Int>>>()
    private val paginationContextMock = mock<PaginationContext>()
    private val sortMock = mock<Sort>()
    private val sortFieldsMock = mock<Collection<SortField<T>>>()
    private val selectSeekStepNMock = mock<SelectSeekStepN<R>>()

    protected val conditionMock = mock<Condition>()
    protected val applicationProperties = mock<ApplicationProperties>()

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
        verifyNoMoreInteractions(dsl, mapper, sortMapper)
        verifyNoMoreInteractions(unpersistedEntity, persistedEntity, unpersistedRecord, persistedRecord)
        verifyNoMoreInteractions(selectWhereStepMock, selectConditionStepMock)
        verifyNoMoreInteractions(selectSelectStepMock, selectJoinStepMock)
        verifyNoMoreInteractions(paginationContextMock, sortMock, sortFieldsMock, selectSeekStepNMock)
        verifyNoMoreInteractions(filter, conditionMock)
        verifyNoMoreInteractions(applicationProperties)
    }

    protected open fun specificTearDown() {}

    protected open fun specificNullCheck() {}

    @Test
    internal fun countingByFilter() {
        whenever(filterConditionMapper.map(filter)).thenReturn(conditionMock)
        whenever(dsl.selectOne()).thenReturn(selectSelectStepMock)
        whenever(selectSelectStepMock.from(table)).thenReturn(selectJoinStepMock)
        whenever(selectJoinStepMock.where(isA<Condition>())).thenReturn(selectConditionStepMock2)
        whenever(dsl.fetchCount(selectConditionStepMock2)).thenReturn(2)

        assertThat(repo.countByFilter(filter)).isEqualTo(2)

        verify(dsl).selectOne()
        verify(selectSelectStepMock).from(table)
        verify(selectJoinStepMock).where(isA<Condition>())
        verify(dsl).fetchCount(selectConditionStepMock2)
    }
}
