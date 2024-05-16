@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.keyword

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.KeywordTr.KEYWORD_TR
import ch.difty.scipamato.core.db.tables.records.KeywordTrRecord
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jooq.DSLContext
import org.jooq.Result
import org.junit.jupiter.api.Test

internal class JooqKeywordRepoTest {

    private val dslContextMock = mockk<DSLContext>()
    private val dateTimeServiceMock = mockk<DateTimeService>()

    private var repo = JooqKeywordRepo(dslContextMock, dateTimeServiceMock)

    @Test
    fun insertingKeywordDefinition_withEntityWithNonNullId_throws() {
        val ntd = KeywordDefinition(1, "de", 1)
        invoking { repo.insert(ntd) } shouldThrow IllegalArgumentException::class withMessage "id must be null."
    }

    @Test
    fun removingObsoletePersistedRecords() {
        val kt = KeywordTranslation(1, "de", "kw1", 1)

        val ktr1 = mockk<KeywordTrRecord>(relaxed = true) {
            every { get(KEYWORD_TR.ID) } returns 1
        }
        val ktr2 = mockk<KeywordTrRecord>(relaxed = true) {
            every { get(KEYWORD_TR.ID) } returns 2
        }
        val resultMock: Result<KeywordTrRecord> = mockk {
            every { iterator() } returns mockk {
                every { hasNext() } returnsMany listOf(true, true, false)
                every { next() } returnsMany listOf(ktr1, ktr2)
            }
        }

        repo.removeObsoletePersistedRecordsFor(resultMock, listOf(kt))

        verify { resultMock.iterator() }
        verify { ktr1.get(KEYWORD_TR.ID) }
        verify { ktr2.get(KEYWORD_TR.ID) }
        verify { ktr2.delete() }

        confirmVerified(resultMock, ktr1, ktr2)
    }

    @Test
    fun removingObsoletePersistedRecords_whenCheckingIfTranslationIsPresentInEntity_doesntConsiderIdLessEntityTransl() {
        val ct = KeywordTranslation(null, "de", "1ade", 1)
        val ctr1 = mockk<KeywordTrRecord>(relaxed = true)
        val ctr2 = mockk<KeywordTrRecord>(relaxed = true)
        val resultMock: Result<KeywordTrRecord> = mockk {
            every { iterator() } returns mockk {
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
    fun managingTranslations() {
        val entity = KeywordDefinition(1, "de", 10)
        invoking {
            repo.manageTranslations(null, entity, emptyList())
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'keyword' has been modified prior to the update attempt. Aborting...." +
            " [KeywordDefinition(id=1, searchOverride=null)]"
    }

    @Test
    fun addingOrUpdatingTranslation() {
        val ktrMock = mockk<KeywordTrRecord> {
            every { get(KEYWORD_TR.ID) } returns 1000
            every { get(KEYWORD_TR.LANG_CODE) } returns "de"
            every { get(KEYWORD_TR.NAME) } returns "someName"
            every { get(KEYWORD_TR.VERSION) } returns 500
        }

        repo = object : JooqKeywordRepo(dslContextMock, dateTimeServiceMock) {
            override fun insertAndGetKeywordTr(keywordId: Int, userId: Int, kt: KeywordTranslation) = ktrMock
        }

        val entity = KeywordDefinition(1, "de", 1)
        val userId = 5
        val translations = ArrayList<KeywordTranslation>()
        val kt = KeywordTranslation(null, "de", "trs1", 1)

        kt.id.shouldBeNull()

        repo.addOrUpdateTranslation(kt, entity, userId, translations)

        translations shouldHaveSize 1
        val translation = translations[0]

        translation.id shouldBeEqualTo 1000
        translation.langCode shouldBeEqualTo "de"
        translation.name shouldBeEqualTo "someName"
        translation.version shouldBeEqualTo 500
    }

    @Test
    fun addOrThrow_withNullRecord_throwsOptimisticLockingException() {
        invoking {
            repo.addOrThrow(null, "trslString", ArrayList())
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'keyword_tr' has been modified prior to the update attempt." +
            " Aborting.... [trslString]"
    }

    @Test
    fun loggingOrThrowing_withDeleteCountZero_throws() {
        invoking {
            repo.logOrThrow(0, 1, KeywordDefinition(10, "de", 100))
        } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'keyword' has been modified prior to the delete attempt." +
            " Aborting.... [KeywordDefinition(id=10, searchOverride=null)]"
    }
}
