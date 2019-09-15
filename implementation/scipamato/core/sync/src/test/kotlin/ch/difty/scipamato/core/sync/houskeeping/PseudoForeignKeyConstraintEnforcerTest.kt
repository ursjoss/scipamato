package ch.difty.scipamato.core.sync.houskeeping

import ch.difty.scipamato.publ.db.tables.records.CodeRecord
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DeleteConditionStep
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus

internal class PseudoForeignKeyConstraintEnforcerTest {

    private val stepMock = mock<DeleteConditionStep<CodeRecord>>()
    private val contributionMock = mock<StepContribution>()
    private val chunkContextMock = mock<ChunkContext>()

    private var enforcer = PseudoForeignKeyConstraintEnforcer(stepMock, "code", "s")

    @Test
    fun executing_withNullStep_doesNotThrow() {
        enforcer = PseudoForeignKeyConstraintEnforcer(null, "code", "s")
        assertThat(enforcer.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED)
    }

    @Test
    fun executing_returnsFinishedStatus() {
        assertThat(enforcer.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED)
        verify<DeleteConditionStep<CodeRecord>>(stepMock).execute()
    }

    @Test
    fun executing_withNullPlural_returnsFinishedStatus_asIfPluralWereS() {
        enforcer = PseudoForeignKeyConstraintEnforcer(stepMock, "code", null)
        assertThat(enforcer.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED)
        verify<DeleteConditionStep<CodeRecord>>(stepMock).execute()
    }

    @Test
    fun executing_withSingleModifications_logs() {
        whenever(stepMock.execute()).thenReturn(1)
        enforcer.execute(contributionMock, chunkContextMock)
        verify(stepMock).execute()
        // log un-asserted, log format different from the one in the next test (visual assertion)
    }

    @Test
    fun executing_withMultipleModifications_logs() {
        whenever(stepMock.execute()).thenReturn(2)
        enforcer.execute(contributionMock, chunkContextMock)
        verify(stepMock).execute()
        // log un-asserted, log format different form that in the previous test (visual assertion)
    }

    @Test
    fun executing_withoutModifications_skipsLog() {
        whenever(stepMock.execute()).thenReturn(0)
        enforcer.execute(contributionMock, chunkContextMock)
        verify(stepMock).execute()
        // missing log un-asserted (visual assertion)
    }

    @Test
    fun executing_ignoresContributionMock() {
        enforcer.execute(contributionMock, chunkContextMock)
        verifyNoMoreInteractions(contributionMock)
    }

    @Test
    fun executing_ignoresChunkContextMock() {
        enforcer.execute(contributionMock, chunkContextMock)
        verifyNoMoreInteractions(chunkContextMock)
    }
}