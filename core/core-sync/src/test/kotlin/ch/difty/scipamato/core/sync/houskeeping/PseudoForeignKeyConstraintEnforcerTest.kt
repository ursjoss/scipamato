package ch.difty.scipamato.core.sync.houskeeping

import ch.difty.scipamato.publ.db.tables.records.CodeRecord
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.DeleteConditionStep
import org.junit.jupiter.api.Test
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus

internal class PseudoForeignKeyConstraintEnforcerTest {

    private val stepMock = mockk<DeleteConditionStep<CodeRecord>>(relaxed = true)
    private val contributionMock = mockk<StepContribution>()
    private val chunkContextMock = mockk<ChunkContext>()

    private var enforcer = PseudoForeignKeyConstraintEnforcer(stepMock, "code", "s")

    @Test
    fun executing_withNullStep_doesNotThrow() {
        enforcer = PseudoForeignKeyConstraintEnforcer(null, "code", "s")
        enforcer.execute(contributionMock, chunkContextMock) shouldBeEqualTo RepeatStatus.FINISHED
    }

    @Test
    fun executing_returnsFinishedStatus() {
        enforcer.execute(contributionMock, chunkContextMock) shouldBeEqualTo RepeatStatus.FINISHED
        verify { stepMock.execute() }
    }

    @Test
    fun executing_withNoExplicitPlural_returnsFinishedStatus_asIfPluralWereS() {
        enforcer = PseudoForeignKeyConstraintEnforcer(stepMock, "code")
        enforcer.execute(contributionMock, chunkContextMock) shouldBeEqualTo RepeatStatus.FINISHED
        verify { stepMock.execute() }
    }

    @Test
    fun executing_withSingleModifications_logs() {
        every { stepMock.execute() } returns 1
        enforcer.execute(contributionMock, chunkContextMock)
        verify { stepMock.execute() }
        // log un-asserted, log format different from the one in the next test (visual assertion)
    }

    @Test
    fun executing_withMultipleModifications_logs() {
        every { stepMock.execute() } returns 2
        enforcer.execute(contributionMock, chunkContextMock)
        verify { stepMock.execute() }
        // log un-asserted, log format different form that in the previous test (visual assertion)
    }

    @Test
    fun executing_withoutModifications_skipsLog() {
        every { stepMock.execute() } returns 0
        enforcer.execute(contributionMock, chunkContextMock)
        verify { stepMock.execute() }
        // missing log un-asserted (visual assertion)
    }

    @Test
    fun executing_ignoresContributionMock() {
        enforcer.execute(contributionMock, chunkContextMock)
        confirmVerified(contributionMock)
    }

    @Test
    fun executing_ignoresChunkContextMock() {
        enforcer.execute(contributionMock, chunkContextMock)
        confirmVerified(chunkContextMock)
    }
}
