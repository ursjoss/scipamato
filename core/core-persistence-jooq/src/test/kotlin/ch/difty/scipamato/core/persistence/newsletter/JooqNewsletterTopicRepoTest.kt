package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jooq.DSLContext
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test

internal class JooqNewsletterTopicRepoTest {

    private val dslContextMock = mockk<DSLContext>()
    private val dateTimeServiceMock = mockk<DateTimeService>()

    private var repo = JooqNewsletterTopicRepo(dslContextMock, dateTimeServiceMock)

    @Test
    fun insertingNewsletterTopicDefinition_withEntityWithNonNullId_throws() {
        val ntd = NewsletterTopicDefinition(1, "de", 1)
        invoking { repo.insert(ntd) } shouldThrow IllegalArgumentException::class withMessage "id must be null."
    }

    @Test
    fun applyingWhereCondition_withNullFilter_returnsTrueCondition() {
        val selectStep: SelectJoinStep<NewsletterTopicRecord> = mockk {
            every { where(DSL.noCondition()) } returns mockk()
        }

        repo.applyWhereCondition(null, selectStep)

        verify { selectStep.where(DSL.noCondition()) }
    }

    @Test
    fun applyingWhereCondition_withFilterWithNoTitleMask_returnsTrueCondition() {
        val selectStep: SelectJoinStep<NewsletterTopicRecord> = mockk {
            every { where(DSL.noCondition()) } returns mockk()
        }

        val filter = NewsletterTopicFilter()
        filter.titleMask.shouldBeNull()

        repo.applyWhereCondition(filter, selectStep)

        verify { selectStep.where(DSL.noCondition()) }
    }

    @Test
    fun updating_withNullEntityId_throws() {
        val entity = NewsletterTopicDefinition(10, "de", 100)
        entity.id = null
        invoking {
            repo.update(entity)
        } shouldThrow java.lang.IllegalArgumentException::class withMessage "entity.id must not be null"
    }

    @Test
    fun handlingUpdatedRecord_withNullRecord_throwsOptimisticLockingException() {
        val entity = NewsletterTopicDefinition(10, "de", 100)
        val userId = 5
        invoking { repo.handleUpdatedRecord(null, entity, userId) } shouldThrow
            OptimisticLockingException::class withMessage
            "Record in table 'newsletter_topic' has been modified prior to the update attempt. " +
            "Aborting.... [NewsletterTopicDefinition(id=10)]"
    }

    @Test
    fun addingOrThrowing_withNullRecord_throwsOptimisticLockingException() {
        invoking { repo.addOrThrow(null, mutableListOf(), "nttObject") } shouldThrow
            OptimisticLockingException::class withMessage
            "Record in table 'newsletter_topic_tr' has been modified prior to the update attempt. " +
            "Aborting.... [nttObject]"
    }

    @Test
    fun loggingOrThrowing_withDeleteCount0_throws() {
        invoking { repo.logOrThrow(0, 10, "delObj") } shouldThrow
            OptimisticLockingException::class withMessage
            "Record in table 'newsletter_topic' has been modified prior to the delete attempt. " +
            "Aborting.... [delObj]"
    }
}
