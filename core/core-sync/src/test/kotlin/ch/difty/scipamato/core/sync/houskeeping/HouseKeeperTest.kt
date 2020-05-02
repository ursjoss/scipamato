@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.sync.houskeeping

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.publ.db.tables.records.CodeClassRecord
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.DeleteConditionStep
import org.jooq.DeleteWhereStep
import org.jooq.Table
import org.jooq.TableField
import org.junit.jupiter.api.Test
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import java.sql.Timestamp
import java.time.LocalDateTime

internal class HouseKeeperTest {

    private val dateTimeServiceMock = mockk<DateTimeService>()
    private val dslContextMock = mockk<DSLContext>()
    private val tableMock = mockk<Table<CodeClassRecord>>()
    private val deleteWhereStepMock = mockk<DeleteWhereStep<CodeClassRecord>>()
    private val deleteCondStepMock = mockk<DeleteConditionStep<CodeClassRecord>>()
    private val lastSynchedField = mockk<TableField<CodeClassRecord, Timestamp>>()
    private val contributionMock = mockk<StepContribution>()
    private val chunkContextMock = mockk<ChunkContext>()

    private var hk = HouseKeeper(dslContextMock, lastSynchedField, dateTimeServiceMock, 30, "code_class")

    @Test
    fun executing_returnsFinishedStatus() {
        commonTestFixture()
        hk.execute(contributionMock, chunkContextMock) shouldBeEqualTo RepeatStatus.FINISHED
        verify { dateTimeServiceMock.currentDateTime }
    }

    private fun commonTestFixture() {
        every { lastSynchedField.table } returns tableMock
        val conditionMock = mockk<Condition>()
        every { lastSynchedField.lessThan(any<Timestamp>()) } returns conditionMock
        every { dslContextMock.deleteFrom(tableMock) } returns deleteWhereStepMock
        every { dateTimeServiceMock.currentDateTime } returns TS
        every { deleteWhereStepMock.where(conditionMock) } returns deleteCondStepMock
        every { deleteCondStepMock.execute() } returns 1
    }

    @Test
    fun executing_withSingleModifications_logs() {
        commonTestFixture()
        every { deleteCondStepMock.execute() } returns 1
        hk.execute(contributionMock, chunkContextMock)
        verify { dateTimeServiceMock.currentDateTime }
        verify { deleteCondStepMock.execute() }
        // log un-asserted, log format different from the one in the next test (visual assertion)
    }

    @Test
    fun executing_withMultipleModifications_logs() {
        commonTestFixture()
        every { deleteCondStepMock.execute() } returns 2
        hk.execute(contributionMock, chunkContextMock)
        verify { dateTimeServiceMock.currentDateTime }
        verify { deleteCondStepMock.execute() }
        // log un-asserted, log format different form that in the previous test (visual assertion)
    }

    @Test
    fun executing_withoutModifications_skipsLog() {
        commonTestFixture()
        every { deleteCondStepMock.execute() } returns 0
        hk.execute(contributionMock, chunkContextMock)
        verify { dateTimeServiceMock.currentDateTime }
        verify { deleteCondStepMock.execute() }
        // missing log un-asserted (visual assertion)
    }

    @Test
    fun executing_ignoresContributionMock() {
        commonTestFixture()
        hk.execute(contributionMock, chunkContextMock)
        verify { dateTimeServiceMock.currentDateTime }
        confirmVerified(contributionMock)
    }

    @Test
    fun executing_ignoresChunkContextMock() {
        commonTestFixture()
        hk.execute(contributionMock, chunkContextMock)
        verify { dateTimeServiceMock.currentDateTime }
        confirmVerified(chunkContextMock)
    }

    companion object {
        val TS: LocalDateTime = LocalDateTime.parse("2018-10-28T22:18:00.000")
    }
}
