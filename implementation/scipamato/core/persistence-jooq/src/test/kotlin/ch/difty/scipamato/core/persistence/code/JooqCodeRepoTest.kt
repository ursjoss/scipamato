package ch.difty.scipamato.core.persistence.code

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.CodeTr.CODE_TR
import ch.difty.scipamato.core.db.tables.records.CodeTrRecord
import ch.difty.scipamato.core.entity.code.CodeTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.DSLContext
import org.jooq.Result
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

internal class JooqCodeRepoTest {

    private val dslContextMock = mock<DSLContext>()
    private val dtsMock = mock<DateTimeService>()
    private val codeClassRepoMock = mock<CodeClassRepository>()

    private val repo = JooqCodeRepo(dslContextMock, dtsMock, codeClassRepoMock)

    @Test
    fun removingObsoletePersistedRecords() {
        val ct = CodeTranslation(1, "de", "1ade", "", 1)
        val resultMock: Result<CodeTrRecord> = mock()
        val itMock: MutableIterator<CodeTrRecord> = mock()
        whenever(resultMock.iterator()).thenReturn(itMock)
        val ctr1 = mock(CodeTrRecord::class.java)
        whenever(ctr1.get(CODE_TR.ID)).thenReturn(1)
        val ctr2 = mock(CodeTrRecord::class.java)
        whenever(ctr2.get(CODE_TR.ID)).thenReturn(2)
        whenever(itMock.hasNext()).thenReturn(true, true, false)
        whenever(itMock.next()).thenReturn(ctr1, ctr2)

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(ct))

        verify<Result<CodeTrRecord>>(resultMock).iterator()
        verify(itMock, times(3)).hasNext()
        verify(itMock, times(2)).next()
        verify(ctr1).get(CODE_TR.ID)
        verify(ctr2).get(CODE_TR.ID)
        verify(ctr2).delete()

        verifyNoMoreInteractions(resultMock, itMock, ctr1, ctr2)
    }

    @Test
    fun removingObsoletePersistedRecords_whenCheckingIfTranslationIsPresentInEntity_doesNotConsiderIdLessEntityTranslations() {
        val ct = CodeTranslation(null, "de", "1ade", "", 1)
        val resultMock: Result<CodeTrRecord> = mock()
        val itMock: MutableIterator<CodeTrRecord> = mock()
        whenever(resultMock.iterator()).thenReturn(itMock)
        val ctr1 = mock(CodeTrRecord::class.java)
        val ctr2 = mock(CodeTrRecord::class.java)
        whenever(itMock.hasNext()).thenReturn(true, true, false)
        whenever(itMock.next()).thenReturn(ctr1, ctr2)

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(ct))

        verify<Result<CodeTrRecord>>(resultMock).iterator()
        verify(itMock, times(3)).hasNext()
        verify(itMock, times(2)).next()
        verify(ctr1).delete()
        verify(ctr2).delete()

        verifyNoMoreInteractions(resultMock, itMock, ctr1, ctr2)
    }

    @Test
    fun consideringAdding_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.considerAdding(null, ArrayList(), CodeTranslation(1, "de", "c1", "comm", 10))
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(OptimisticLockingException::class.java)
                    .hasMessage("Record in table 'code_tr' has been modified prior to the update attempt. Aborting.... [CodeTranslation(comment=comm)]")
        }

    }

    @Test
    fun logOrThrow_withDeleteCount0_throws() {
        try {
            repo.logOrThrow(0, "1A", "deletedObject")
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(OptimisticLockingException::class.java)
                    .hasMessage("Record in table 'code' has been modified prior to the delete attempt. Aborting.... [deletedObject]")
        }

    }
}
