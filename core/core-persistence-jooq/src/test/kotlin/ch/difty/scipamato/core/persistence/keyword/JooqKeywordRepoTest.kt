package ch.difty.scipamato.core.persistence.keyword

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.KeywordTr.KEYWORD_TR
import ch.difty.scipamato.core.db.tables.records.KeywordTrRecord
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.persistence.mock
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.DSLContext
import org.jooq.Result
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class JooqKeywordRepoTest {

    private val dslContextMock = Mockito.mock(DSLContext::class.java)
    private val dateTimeServiceMock = Mockito.mock(DateTimeService::class.java)

    private var repo = JooqKeywordRepo(dslContextMock, dateTimeServiceMock)

    @Test
    fun insertingKeywordDefinition_withEntityWithNonNullId_throws() {
        val ntd = KeywordDefinition(1, "de", 1)
        try {
            repo.insert(ntd)
            fail<Any>("Should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("id must be null.")
        }
    }

    @Test
    fun removingObsoletePersistedRecords() {
        val kt = KeywordTranslation(1, "de", "kw1", 1)
        val resultMock: Result<KeywordTrRecord> = mock()
        val itMock: MutableIterator<KeywordTrRecord> = mock()
        whenever(resultMock.iterator()).thenReturn(itMock)
        val ktr1 = Mockito.mock(KeywordTrRecord::class.java)
        whenever(ktr1.get(KEYWORD_TR.ID)).thenReturn(1)
        val ktr2 = Mockito.mock(KeywordTrRecord::class.java)
        whenever(ktr2.get(KEYWORD_TR.ID)).thenReturn(2)
        whenever(itMock.hasNext()).thenReturn(true, true, false)
        whenever<Any>(itMock.next()).thenReturn(ktr1, ktr2)

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(kt))

        verify(resultMock).iterator()
        verify(itMock, times(3)).hasNext()
        verify(itMock, times(2)).next()
        verify(ktr1).get(KEYWORD_TR.ID)
        verify(ktr2).get(KEYWORD_TR.ID)
        verify(ktr2).delete()

        verifyNoMoreInteractions(resultMock, itMock, ktr1, ktr2)
    }

    @Test
    fun removingObsoletePersistedRecords_whenCheckingIfTranslationIsPresentInEntity_doesntConsiderIdLessEntityTransl() {
        val ct = KeywordTranslation(null, "de", "1ade", 1)
        val resultMock: Result<KeywordTrRecord> = mock()
        val itMock: MutableIterator<KeywordTrRecord> = mock()
        whenever(resultMock.iterator()).thenReturn(itMock)
        val ctr1 = Mockito.mock(KeywordTrRecord::class.java)
        val ctr2 = Mockito.mock(KeywordTrRecord::class.java)
        whenever(itMock.hasNext()).thenReturn(true, true, false)
        whenever(itMock.next()).thenReturn(ctr1, ctr2)

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(ct))

        verify(resultMock).iterator()
        verify(itMock, times(3)).hasNext()
        verify(itMock, times(2)).next()
        verify(ctr1).delete()
        verify(ctr2).delete()

        verifyNoMoreInteractions(resultMock, itMock, ctr1, ctr2)
    }

    @Test
    fun managingTranslations() {
        val entity = KeywordDefinition(1, "de", 10)

        try {
            repo.manageTranslations(null, entity, emptyList())
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java).hasMessage(
                    "Record in table 'keyword' has been modified prior to the update attempt. Aborting...." +
                        " [KeywordDefinition(id=1, searchOverride=null)]"
                )
        }
    }

    @Test
    fun addingOrUpdatingTranslation() {
        val ktrMock = Mockito.mock(KeywordTrRecord::class.java)
        doReturn(1000).whenever(ktrMock).get(KEYWORD_TR.ID)
        doReturn("de").whenever(ktrMock).get(KEYWORD_TR.LANG_CODE)
        doReturn("someName").whenever(ktrMock).get(KEYWORD_TR.NAME)
        doReturn(500).whenever(ktrMock).get(KEYWORD_TR.VERSION)

        repo = object : JooqKeywordRepo(dslContextMock, dateTimeServiceMock) {
            override fun insertAndGetKeywordTr(keywordId: Int, userId: Int, kt: KeywordTranslation) = ktrMock
        }

        val entity = KeywordDefinition(1, "de", 1)
        val userId = 5
        val translations = ArrayList<KeywordTranslation>()
        val kt = KeywordTranslation(null, "de", "trs1", 1)

        assertThat(kt.id).isNull()

        repo.addOrUpdateTranslation(kt, entity, userId, translations)

        assertThat(translations).hasSize(1)
        val translation = translations[0]

        assertThat(translation.id).isEqualTo(1000)
        assertThat(translation.langCode).isEqualTo("de")
        assertThat(translation.name).isEqualTo("someName")
        assertThat(translation.version).isEqualTo(500)
    }

    @Test
    fun addOrThrow_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.addOrThrow(null, "trslString", ArrayList())
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java).hasMessage(
                    "Record in table 'keyword_tr' has been modified prior to the update attempt." +
                        " Aborting.... [trslString]"
                )
        }
    }

    @Test
    fun loggingOrThrowing_withDeleteCountZero_throws() {
        try {
            repo.logOrThrow(0, 1, KeywordDefinition(10, "de", 100))
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java).hasMessage(
                    "Record in table 'keyword' has been modified prior to the delete attempt." +
                        " Aborting.... [KeywordDefinition(id=10, searchOverride=null)]"
                )
        }
    }
}
