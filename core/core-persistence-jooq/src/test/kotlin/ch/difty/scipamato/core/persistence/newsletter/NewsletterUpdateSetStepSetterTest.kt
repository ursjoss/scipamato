package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRecordMapperTest.Companion.ISSUE
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRecordMapperTest.Companion.ISSUE_DATE
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.sql.Date

internal class NewsletterUpdateSetStepSetterTest : UpdateSetStepSetterTest<NewsletterRecord, Newsletter>() {

    private val entityMock = mockk<Newsletter>()
    override val entity = entityMock

    override val setter = NewsletterUpdateSetStepSetter()

    override fun specificTearDown() {
        confirmVerified(entityMock)
    }

    override fun entityFixture() {
        NewsletterRecordMapperTest.entityFixtureWithoutIdFields(entityMock)
    }

    override fun stepSetFixtureExceptAudit() {
        every { step.set(NEWSLETTER.ISSUE, ISSUE) } returns moreStep
        every { moreStep.set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE)) } returns moreStep
        every { moreStep.set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id) } returns moreStep
    }

    override fun stepSetFixtureAudit() {
        every { moreStep.set(NEWSLETTER.CREATED, RecordMapperTest.CREATED) } returns moreStep
        every { moreStep.set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(NEWSLETTER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) } returns moreStep
        every { moreStep.set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) } returns moreStep
        every { moreStep.set(NEWSLETTER.VERSION, RecordMapperTest.VERSION + 1) } returns moreStep
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify { entityMock.issue }
        verify { entityMock.issueDate }
        verify { entityMock.publicationStatus }
    }

    override fun verifyStepSettingExceptAudit() {
        verify { step.set(NEWSLETTER.ISSUE, ISSUE) }
        verify { moreStep.set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE)) }
        verify { moreStep.set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id) }
    }

    override fun verifyStepSettingAudit() {
        verify { moreStep.set(NEWSLETTER.CREATED, RecordMapperTest.CREATED) }
        verify { moreStep.set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(NEWSLETTER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) }
        verify { moreStep.set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) }
        verify { moreStep.set(NEWSLETTER.VERSION, RecordMapperTest.VERSION + 1) }
    }
}
