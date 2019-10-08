package ch.difty.scipamato.core.persistence.codeclass

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR
import ch.difty.scipamato.core.db.tables.records.CodeClassTrRecord
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.DSLContext
import org.jooq.Result
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

internal class JooqCodeClassRepoTest {

    private val dslContextMock = mock<DSLContext>()
    private val dtsMock = mock<DateTimeService>()
    private var repo = JooqCodeClassRepo(dslContextMock, dtsMock)

    @Suppress("SpellCheckingInspection")
    @Test
    fun removingObsoletePersistedRecords() {
        val cct = CodeClassTranslation(1, "de", "cc1", "", 1)
        val resultMock: Result<CodeClassTrRecord> = mock()
        val itMock: MutableIterator<CodeClassTrRecord> = mock()
        whenever(resultMock.iterator()).thenReturn(itMock)
        val cctr1 = mock(CodeClassTrRecord::class.java)
        whenever(cctr1.get(CODE_CLASS_TR.ID)).thenReturn(1)
        val cctr2 = mock(CodeClassTrRecord::class.java)
        whenever(cctr2.get(CODE_CLASS_TR.ID)).thenReturn(2)
        whenever(itMock.hasNext()).thenReturn(true, true, false)
        whenever<Any>(itMock.next()).thenReturn(cctr1, cctr2)

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(cct))

        verify<Result<CodeClassTrRecord>>(resultMock).iterator()
        verify(itMock, times(3)).hasNext()
        verify(itMock, times(2)).next()
        verify(cctr1).get(CODE_CLASS_TR.ID)
        verify(cctr2).get(CODE_CLASS_TR.ID)
        verify(cctr2).delete()

        verifyNoMoreInteractions(resultMock, itMock, cctr1, cctr2)
    }

    @Test
    fun consideringAdding_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.considerAdding(null, ArrayList(), CodeClassTranslation(1, "de", "c1", "comm", 10))
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(OptimisticLockingException::class.java)
                    .hasMessage("Record in table 'code_class_tr' has been modified prior to the update attempt. Aborting.... [CodeClassTranslation(description=comm)]")
        }
    }

    @Test
    fun logOrThrow_withDeleteCount0_throws() {
        try {
            repo.logOrThrow(0, 1, "deletedObject")
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(OptimisticLockingException::class.java)
                    .hasMessage("Record in table 'code_class' has been modified prior to the delete attempt. Aborting.... [deletedObject]")
        }
    }
}