package ch.difty.scipamato.core.persistence.codeclass

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR
import ch.difty.scipamato.core.db.tables.records.CodeClassTrRecord
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
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

internal class JooqCodeClassRepoTest {

    private val dslContextMock = mockk<DSLContext>()
    private val dtsMock = mockk<DateTimeService>()
    private var repo = JooqCodeClassRepo(dslContextMock, dtsMock)

    @Suppress("SpellCheckingInspection")
    @Test
    fun removingObsoletePersistedRecords() {
        val cct = CodeClassTranslation(1, "de", "cc1", "", 1)
        val cctr1 = mockk<CodeClassTrRecord>(relaxed = true) {
            every { get(CODE_CLASS_TR.ID) } returns 1
        }
        val cctr2 = mockk<CodeClassTrRecord>(relaxed = true) {
            every { get(CODE_CLASS_TR.ID) } returns 2
        }
        val resultMock: Result<CodeClassTrRecord> = mockk {
            every { iterator() } returns mockk {
                every { hasNext() } returnsMany listOf(true, true, false)
                every { next() } returnsMany listOf(cctr1, cctr2)
            }
        }

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(cct))

        verify { resultMock.iterator() }
        verify { cctr1.get(CODE_CLASS_TR.ID) }
        verify { cctr2.get(CODE_CLASS_TR.ID) }
        verify { cctr2.delete() }

        confirmVerified(resultMock, cctr1, cctr2)
    }

    @Test
    fun consideringAdding_withNullRecord_throwsOptimisticLockingException() {
        invoking {
            repo.considerAdding(null, ArrayList(), CodeClassTranslation(1, "de", "c1", "comm", 10))
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'code_class_tr' has been modified prior to the update attempt. " +
            "Aborting.... [CodeClassTranslation(description=comm)]"
    }

    @Test
    fun logOrThrow_withDeleteCount0_throws() {
        invoking {
            repo.logOrThrow(0, 1, "deletedObject")
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'code_class' has been modified prior to the delete attempt. " +
            "Aborting.... [deletedObject]"
    }
}
