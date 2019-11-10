package ch.difty.scipamato.core.sync.houskeeping

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.publ.db.tables.records.CodeClassRecord
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import java.sql.Timestamp
import java.time.LocalDateTime

internal class HouseKeeperTest {

    private val dateTimeServiceMock = mock<DateTimeService>()
    private val dslContextMock = mock<DSLContext>()
    private val tableMock = mock<Table<CodeClassRecord>>()
    private val deleteWhereStepMock = mock<DeleteWhereStep<CodeClassRecord>>()
    private val deleteCondStepMock = mock<DeleteConditionStep<CodeClassRecord>>()
    private val lastSynchedField = mock<TableField<CodeClassRecord, Timestamp>>()
    private val contributionMock = mock<StepContribution>()
    private val chunkContextMock = mock<ChunkContext>()

    private var hk = HouseKeeper(dslContextMock, lastSynchedField, dateTimeServiceMock, 30, "code_class")

    @Test
    fun executing_returnsFinishedStatus() {
        commonTestFixture()
        assertThat(hk.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED)
        verify<DateTimeService>(dateTimeServiceMock).currentDateTime
    }

    private fun commonTestFixture() {
        whenever(lastSynchedField.table).thenReturn(tableMock)
        whenever(dslContextMock.deleteFrom(tableMock)).thenReturn(deleteWhereStepMock)
        whenever(dateTimeServiceMock.currentDateTime).thenReturn(TS)
        whenever(deleteWhereStepMock.where(lastSynchedField.lessThan(Timestamp.valueOf(TS))))
            .thenReturn(deleteCondStepMock)
    }

    @Test
    fun executing_withSingleModifications_logs() {
        commonTestFixture()
        whenever(deleteCondStepMock.execute()).thenReturn(1)
        hk.execute(contributionMock, chunkContextMock)
        verify<DateTimeService>(dateTimeServiceMock).currentDateTime
        verify(deleteCondStepMock).execute()
        // log un-asserted, log format different from the one in the next test (visual assertion)
    }

    @Test
    fun executing_withMultipleModifications_logs() {
        commonTestFixture()
        whenever(deleteCondStepMock.execute()).thenReturn(2)
        hk.execute(contributionMock, chunkContextMock)
        verify<DateTimeService>(dateTimeServiceMock).currentDateTime
        verify(deleteCondStepMock).execute()
        // log un-asserted, log format different form that in the previous test (visual assertion)
    }

    @Test
    fun executing_withoutModifications_skipsLog() {
        commonTestFixture()
        whenever(deleteCondStepMock.execute()).thenReturn(0)
        hk.execute(contributionMock, chunkContextMock)
        verify<DateTimeService>(dateTimeServiceMock).currentDateTime
        verify(deleteCondStepMock).execute()
        // missing log un-asserted (visual assertion)
    }

    @Test
    fun executing_ignoresContributionMock() {
        commonTestFixture()
        hk.execute(contributionMock, chunkContextMock)
        verify<DateTimeService>(dateTimeServiceMock).currentDateTime
        verifyNoMoreInteractions(contributionMock)
    }

    @Test
    fun executing_ignoresChunkContextMock() {
        commonTestFixture()
        hk.execute(contributionMock, chunkContextMock)
        verify<DateTimeService>(dateTimeServiceMock).currentDateTime
        verifyNoMoreInteractions(chunkContextMock)
    }

    companion object {
        val TS: LocalDateTime = LocalDateTime.parse("2018-10-28T22:18:00.000")
    }
}
