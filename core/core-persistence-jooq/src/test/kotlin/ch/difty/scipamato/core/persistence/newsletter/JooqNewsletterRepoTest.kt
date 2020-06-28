package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.TableField
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import java.sql.Timestamp

internal class JooqNewsletterRepoTest : JooqEntityRepoTest<NewsletterRecord, Newsletter, Int,
    ch.difty.scipamato.core.db.tables.Newsletter, NewsletterRecordMapper, NewsletterFilter>() {

    override val unpersistedEntity = mock<Newsletter>()
    override val persistedEntity = mock<Newsletter>()
    override val persistedRecord = mock<NewsletterRecord>()
    override val unpersistedRecord = mock<NewsletterRecord>()
    override val mapper = mock<NewsletterRecordMapper>()
    override val filter = mock<NewsletterFilter>()
    override val repo: JooqNewsletterRepo = JooqNewsletterRepo(
        dsl,
        mapper,
        sortMapper,
        filterConditionMapper,
        dateTimeService,
        insertSetStepSetter,
        updateSetStepSetter,
        applicationProperties
    )

    override fun makeRepoSavingReturning(returning: NewsletterRecord) =
        object : JooqNewsletterRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun doSave(entity: Newsletter, languageCode: String) = returning
        }

    override val sampleId: Int = SAMPLE_ID
    override val table: ch.difty.scipamato.core.db.tables.Newsletter = NEWSLETTER
    override val tableId: TableField<NewsletterRecord, Int> = NEWSLETTER.ID

    override fun makeRepoFindingEntityById(entity: Newsletter): EntityRepository<Newsletter, Int, NewsletterFilter> =
        object : JooqNewsletterRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun findById(id: Int, version: Int): Newsletter? = entity
        }

    override fun expectEntityIdsWithValues() {
        whenever(unpersistedEntity.id).thenReturn(SAMPLE_ID)
        whenever(unpersistedEntity.version).thenReturn(0)
        whenever(persistedRecord.id).thenReturn(SAMPLE_ID)
        whenever(persistedRecord.version).thenReturn(1)
    }

    override fun expectUnpersistedEntityIdNull() {
        whenever(unpersistedEntity.id).thenReturn(null)
    }

    override fun verifyUnpersistedEntityId() {
        verify<Newsletter>(unpersistedEntity).id
    }

    override fun verifyPersistedRecordId() {
        verify<NewsletterRecord>(persistedRecord).id
    }

    override val recordVersion: TableField<NewsletterRecord, Int> = NEWSLETTER.VERSION

    @Test
    fun mergingPaperIntoNewsletter_withInsertAttemptNotSucceeding_returnsEmptyOptional() {
        val repo = object : JooqNewsletterRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun tryInserting(
                newsletterId: Int,
                paperId: Long,
                newsletterTopicId: Int?,
                ts: Timestamp?
            ): Int = 0
        }
        assertThat(repo.mergePaperIntoNewsletter(1, 2L, 3, "en")).isNotPresent
    }

    @Test
    fun handlingInsertedNewsletter_withZeroCount_returnsEmptyOptional() {
        assertThat(repo.handleInsertedNewsletter(0, 1, 2, "de")).isEmpty
    }

    @Test
    fun test() {
        val repo = object : JooqNewsletterRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun fetchMergedNewsletter(newsletterId: Int, paperId: Long, languageCode: String) = null
        }
        assertThat(repo.handleInsertedNewsletter(1, 1, 2, "de")).isEmpty
    }

    companion object {
        private const val SAMPLE_ID = 2
    }
}
