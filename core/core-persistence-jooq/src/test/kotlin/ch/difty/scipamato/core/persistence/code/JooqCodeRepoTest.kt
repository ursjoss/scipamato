package ch.difty.scipamato.core.persistence.code

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.CodeTr.CODE_TR
import ch.difty.scipamato.core.db.tables.records.CodeTrRecord
import ch.difty.scipamato.core.entity.code.CodeTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jooq.DSLContext
import org.jooq.Result
import org.junit.jupiter.api.Test

internal class JooqCodeRepoTest {

    private val dslContextMock = mockk<DSLContext>()
    private val dtsMock = mockk<DateTimeService>()
    private val codeClassRepoMock = mockk<CodeClassRepository>()

    private val repo = JooqCodeRepo(dslContextMock, dtsMock, codeClassRepoMock)

    @Test
    fun removingObsoletePersistedRecords() {
        val ct = CodeTranslation(1, "de", "1ade", "", 1)

        val ctr1 = mockk<CodeTrRecord>(relaxed = true) {
            every { get(CODE_TR.ID) } returns 1
        }
        val ctr2 = mockk<CodeTrRecord>(relaxed = true) {
            every { get(CODE_TR.ID) } returns 2
        }
        val resultMock: Result<CodeTrRecord> = mockk {
            every { iterator() } returns mockk {
                every { hasNext() } returnsMany listOf(true, true, false)
                every { next() } returnsMany listOf(ctr1, ctr2)
            }
        }

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(ct))

        verify { resultMock.iterator() }
        verify { ctr1.get(CODE_TR.ID) }
        verify { ctr2.get(CODE_TR.ID) }
        verify { ctr2.delete() }

        confirmVerified(resultMock, ctr1, ctr2)
    }

    @Test
    fun removingObsoletePersistedRecords_whenCheckingIfTranslationIsPresent_doesNotConsiderIdLessEntityTranslations() {
        val ct = CodeTranslation(null, "de", "1ade", "", 1)
        val ctr1 = mockk<CodeTrRecord>(relaxed = true)
        val ctr2 = mockk<CodeTrRecord>(relaxed = true)
        val resultMock: Result<CodeTrRecord> = mockk {
            every { iterator() } returns mockk() {
                every { hasNext() } returnsMany listOf(true, true, false)
                every { next() } returnsMany listOf(ctr1, ctr2)
            }
        }

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(ct))

        verify { resultMock.iterator() }
        verify { ctr1.delete() }
        verify { ctr2.delete() }

        confirmVerified(resultMock, ctr1, ctr2)
    }

    @Test
    fun consideringAdding_withNullRecord_throwsOptimisticLockingException() {
        invoking {
            repo.considerAdding(null, ArrayList(), CodeTranslation(1, "de", "c1", "comm", 10))
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'code_tr' has been modified prior to the update attempt. " +
            "Aborting.... [CodeTranslation(comment=comm)]"
    }

    @Test
    fun logOrThrow_withDeleteCount0_throws() {
        invoking {
            repo.logOrThrow(0, "1A", "deletedObject")
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'code' has been modified prior to the delete attempt. Aborting.... [deletedObject]"
    }
}
