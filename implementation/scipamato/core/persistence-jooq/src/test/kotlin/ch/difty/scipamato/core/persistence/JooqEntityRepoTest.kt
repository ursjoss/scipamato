package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.FrozenDateTimeService
import ch.difty.scipamato.common.NullArgumentException
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.*
import org.jooq.impl.TableImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * TODO find a more feasible approach to test the actual jOOQ part via unit
 * tests. The current approach is too cumbersome.
 *
 *
 * I played around with the MockDataProvider (see branch exp/jooq_mocking), see
 * also https://www.jooq.org/doc/3.9/manual/tools/jdbc-mocking/ but that did not
 * seem to solve what I want to achieve (testing the query composition in the
 * repo methods).
 *
 *
 * Let's postpone this and remove the ignored test methods for now (gentle
 * pressure of sonarqube :-) )
 */
@ExtendWith(SpringExtension::class)
abstract class JooqEntityRepoTest<R : Record, T : IdScipamatoEntity<ID>, ID : Number, TI : TableImpl<R>, M : RecordMapper<R, T>, F : ScipamatoFilter> : JooqReadOnlyRepoTest<R, T, ID, TI, M, F>() {

    protected val insertSetStepSetter = mock<InsertSetStepSetter<R, T>>()
    protected val updateSetStepSetter = mock<UpdateSetStepSetter<R, T>>()
    private val insertSetStepMock = mock<InsertSetStep<R>>()
    private val insertSetMoreStepMock = mock<InsertSetMoreStep<R>>()
    private val insertResultStepMock = mock<InsertResultStep<R>>()
    protected var deleteWhereStepMock = mock<DeleteWhereStep<R>>()
    private val deleteConditionStep1Mock = mock<DeleteConditionStep<R>>()
    private val deleteConditionStep2Mock = mock<DeleteConditionStep<R>>()
    private val updateSetFirstStepMock = mock<UpdateSetFirstStep<R>>()
    private val updateConditionStepMock = mock<UpdateConditionStep<R>>()
    private val updateSetMoreStepMock = mock<UpdateSetMoreStep<R>>()
    private val updateResultStepMock = mock<UpdateResultStep<R>>()
    private val paginationContextMock = mock<PaginationContext>()

    abstract override val repo: EntityRepository<T, ID, F>

    private val entities = mutableListOf<T>()
    private val records = mutableListOf<R>()

    private lateinit var id: ID

    protected abstract val sampleId: ID

    protected val dateTimeService: DateTimeService = FrozenDateTimeService()

    protected abstract val recordVersion: TableField<R, Int>

    override fun specificSetUp() {
        id = sampleId
        testSpecificSetUp()
    }

    /**
     * Hook for concrete tests to inject their specific set up instructions.
     */
    protected open fun testSpecificSetUp() {}

    public override fun specificTearDown() {
        verifyNoMoreInteractions(insertSetStepMock, insertSetMoreStepMock, insertResultStepMock,
                insertSetStepSetter)
        verifyNoMoreInteractions(deleteWhereStepMock, deleteConditionStep1Mock, deleteConditionStep2Mock)
        verifyNoMoreInteractions(updateSetFirstStepMock, updateConditionStepMock, updateSetMoreStepMock,
                updateResultStepMock, updateSetStepSetter)
    }

    /**
     * Hand-rolled spy that returns the provided entity in the method `doSaving(T entity)`
     *
     * @param returning
     * the record to be returned after the save.
     * @return the entity
     */
    protected abstract fun makeRepoSavingReturning(returning: R): EntityRepository<T, ID, F>

    @Test
    internal fun deleting_withIdNull_throws() {
        Assertions.assertThrows(NullArgumentException::class.java) { repo.delete(null, 1) }
    }

    @Test
    internal fun deleting_validPersistentEntity_returnsDeletedEntity() {
        val repo = makeRepoFindingEntityById(persistedEntity)

        whenever(dsl.delete(table)).thenReturn(deleteWhereStepMock)
        whenever(deleteWhereStepMock.where(tableId.equal(id))).thenReturn(deleteConditionStep1Mock)
        whenever(deleteConditionStep1Mock.and(recordVersion.eq(0))).thenReturn(deleteConditionStep2Mock)
        whenever(deleteConditionStep2Mock.execute()).thenReturn(1)

        assertThat(repo.delete(id, 0)).isEqualTo(persistedEntity)

        verify(dsl).delete(table)
        verify<DeleteWhereStep<R>>(deleteWhereStepMock).where(tableId.equal(id))
        verify(deleteConditionStep1Mock).and(recordVersion.eq(0))
        verify(deleteConditionStep2Mock).execute()
    }

    abstract override fun makeRepoFindingEntityById(entity: T): EntityRepository<T, ID, F>

    @Test
    internal fun deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        val repo = makeRepoFindingEntityById(persistedEntity)
        whenever(dsl.delete(table)).thenReturn(deleteWhereStepMock)
        whenever(deleteWhereStepMock.where(tableId.equal(id))).thenReturn(deleteConditionStep1Mock)
        whenever(deleteConditionStep1Mock.and(recordVersion.eq(0))).thenReturn(deleteConditionStep2Mock)
        whenever(deleteConditionStep2Mock.execute()).thenReturn(0)

        try {
            repo.delete(id, 0)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(OptimisticLockingException::class.java)
        }

        verify(dsl).delete(table)
        verify(deleteConditionStep1Mock).and(recordVersion.eq(0))
        verify<DeleteWhereStep<R>>(deleteWhereStepMock).where(tableId.equal(id))
        verify(deleteConditionStep2Mock).execute()
    }

}
