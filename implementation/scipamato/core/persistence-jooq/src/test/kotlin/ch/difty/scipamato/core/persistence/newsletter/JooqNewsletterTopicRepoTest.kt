package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.DSLContext
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class JooqNewsletterTopicRepoTest {

    private val dslContextMock = mock<DSLContext>()
    private val dateTimeServiceMock = mock<DateTimeService>()

    private var repo = JooqNewsletterTopicRepo(dslContextMock, dateTimeServiceMock)

    @Test
    fun insertingNewsletterTopicDefinition_withEntityWithNonNullId_throws() {
        val ntd = NewsletterTopicDefinition(1, "de", 1)
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
    fun applyingWhereCondition_withNullFilter_returnsTrueCondition() {
        val selectStep: SelectJoinStep<NewsletterTopicRecord> = mock()
        repo.applyWhereCondition<NewsletterTopicRecord>(null, selectStep)
        verify<SelectJoinStep<NewsletterTopicRecord>>(selectStep).where(DSL.trueCondition())
    }

    @Test
    fun applyingWhereCondition_withFilterWithNoTitleMask_returnsTrueCondition() {
        val selectStep: SelectJoinStep<NewsletterTopicRecord> = mock()
        val filter = NewsletterTopicFilter()
        assertThat(filter.titleMask).isNull()

        repo.applyWhereCondition<NewsletterTopicRecord>(filter, selectStep)

        verify<SelectJoinStep<NewsletterTopicRecord>>(selectStep).where(DSL.trueCondition())
    }

    @Test
    fun handlingUpdatedRecord_withNullRecord_throwsOptimisticLockingException() {
        val entity = NewsletterTopicDefinition(10, "de", 100)
        val userId = 5
        try {
            repo.handleUpdatedRecord(null, entity, userId)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java)
                .hasMessage(
                    "Record in table 'newsletter_topic' has been modified prior to the update attempt. " +
                        "Aborting.... [NewsletterTopicDefinition(id=10)]"
                )
        }
    }

    @Test
    fun addingOrThrowing_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.addOrThrow(null, emptyList(), "nttObject")
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java)
                .hasMessage(
                    "Record in table 'newsletter_topic_tr' has been modified prior to the update attempt. " +
                        "Aborting.... [nttObject]"
                )
        }
    }

    @Test
    fun loggingOrThrowing_withDeleteCount0_throws() {
        try {
            repo.logOrThrow(0, 10, "delObj")
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java)
                .hasMessage(
                    "Record in table 'newsletter_topic' has been modified prior to the delete attempt. " +
                        "Aborting.... [delObj]"
                )
        }
    }
}
