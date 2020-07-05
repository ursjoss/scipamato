package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeFalse
import org.jooq.TableField
import org.junit.jupiter.api.Test
import java.sql.Timestamp

internal class JooqNewsletterRepoTest : JooqEntityRepoTest<NewsletterRecord, Newsletter, Int,
    ch.difty.scipamato.core.db.tables.Newsletter, NewsletterRecordMapper, NewsletterFilter>() {

    override val unpersistedEntity = mockk<Newsletter>()
    override val persistedEntity = mockk<Newsletter>()
    override val persistedRecord = mockk<NewsletterRecord>()
    override val unpersistedRecord = mockk<NewsletterRecord>()
    override val mapper = mockk<NewsletterRecordMapper>()
    override val filter = mockk<NewsletterFilter>()
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
        every { unpersistedEntity.id } returns SAMPLE_ID
        every { unpersistedEntity.version } returns 0
        every { persistedRecord.id } returns SAMPLE_ID
        every { persistedRecord.version } returns 1
    }

    override fun expectUnpersistedEntityIdNull() {
        every { unpersistedEntity.id } returns null
    }

    override fun verifyUnpersistedEntityId() {
        verify { unpersistedEntity.id }
    }

    override fun verifyPersistedRecordId() {
        verify { persistedRecord.id }
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
        repo.mergePaperIntoNewsletter(1, 2L, 3, "en").isPresent.shouldBeFalse()
    }

    @Test
    fun handlingInsertedNewsletter_withZeroCount_returnsEmptyOptional() {
        repo.handleInsertedNewsletter(0, 1, 2, "de").isPresent.shouldBeFalse()
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
        repo.handleInsertedNewsletter(1, 1, 2, "de").isPresent.shouldBeFalse()
    }

    companion object {
        private const val SAMPLE_ID = 2
    }
}
