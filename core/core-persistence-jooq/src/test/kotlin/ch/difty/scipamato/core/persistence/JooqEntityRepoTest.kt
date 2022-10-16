@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.FrozenDateTimeService
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.jooq.DeleteConditionStep
import org.jooq.DeleteUsingStep
import org.jooq.InsertResultStep
import org.jooq.InsertSetMoreStep
import org.jooq.InsertSetStep
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UpdateConditionStep
import org.jooq.UpdateResultStep
import org.jooq.UpdateSetFirstStep
import org.jooq.UpdateSetMoreStep
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
abstract class JooqEntityRepoTest<R : Record, T : IdScipamatoEntity<ID>, ID : Number, TI : Table<R>,
    M : RecordMapper<R, T>, F : ScipamatoFilter> : JooqReadOnlyRepoTest<R, T, ID, TI, M, F>() {

    protected val insertSetStepSetter = mockk<InsertSetStepSetter<R, T>>()
    protected val updateSetStepSetter = mockk<UpdateSetStepSetter<R, T>>()
    private val insertSetStepMock = mockk<InsertSetStep<R>>()
    private val insertSetMoreStepMock = mockk<InsertSetMoreStep<R>>()
    private val insertResultStepMock = mockk<InsertResultStep<R>>()
    protected var deleteUsingStep = mockk<DeleteUsingStep<R>>()
    private val deleteConditionStep1Mock = mockk<DeleteConditionStep<R>>()
    private val deleteConditionStep2Mock = mockk<DeleteConditionStep<R>>()
    private val updateSetFirstStepMock = mockk<UpdateSetFirstStep<R>>()
    private val updateConditionStepMock = mockk<UpdateConditionStep<R>>()
    private val updateSetMoreStepMock = mockk<UpdateSetMoreStep<R>>()
    private val updateResultStepMock = mockk<UpdateResultStep<R>>()

    abstract override val repo: EntityRepository<T, ID, F>

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
        confirmVerified(
            insertSetStepMock, insertSetMoreStepMock, insertResultStepMock, insertSetStepSetter
        )
        confirmVerified(deleteUsingStep, deleteConditionStep1Mock, deleteConditionStep2Mock)
        confirmVerified(
            updateSetFirstStepMock, updateConditionStepMock, updateSetMoreStepMock,
            updateResultStepMock, updateSetStepSetter
        )
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
    internal fun deleting_validPersistentEntity_returnsDeletedEntity() {
        val repo = makeRepoFindingEntityById(persistedEntity)

        every { dsl.delete(table) } returns deleteUsingStep
        every { deleteUsingStep.where(tableId.equal(id)) } returns deleteConditionStep1Mock
        every { deleteConditionStep1Mock.and(recordVersion.eq(0)) } returns deleteConditionStep2Mock
        every { deleteConditionStep2Mock.execute() } returns 1

        repo.delete(id, 0) shouldBeEqualTo persistedEntity

        verify { dsl.delete(table) }
        verify { deleteUsingStep.where(tableId.equal(id)) }
        verify { deleteConditionStep1Mock.and(recordVersion.eq(0)) }
        verify { deleteConditionStep2Mock.execute() }
    }

    abstract override fun makeRepoFindingEntityById(entity: T): EntityRepository<T, ID, F>

    @Test
    internal fun deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        val repo = makeRepoFindingEntityById(persistedEntity)
        every { dsl.delete(table) } returns deleteUsingStep
        every { deleteUsingStep.where(tableId.equal(id)) } returns deleteConditionStep1Mock
        every { deleteConditionStep1Mock.and(recordVersion.eq(0)) } returns deleteConditionStep2Mock
        every { deleteConditionStep2Mock.execute() } returns 0

        invoking { repo.delete(id, 0) } shouldThrow OptimisticLockingException::class

        verify { dsl.delete(table) }
        verify { deleteConditionStep1Mock.and(recordVersion.eq(0)) }
        verify { deleteUsingStep.where(tableId.equal(id)) }
        verify { deleteConditionStep2Mock.execute() }
        verify { persistedEntity.toString() }
    }
}
