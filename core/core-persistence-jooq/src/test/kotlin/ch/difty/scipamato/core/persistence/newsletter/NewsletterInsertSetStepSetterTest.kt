package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import ch.difty.scipamato.core.persistence.RecordMapperTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.sql.Date

internal class NewsletterInsertSetStepSetterTest : InsertSetStepSetterTest<NewsletterRecord, Newsletter>() {

    override val setter = NewsletterInsertSetStepSetter()

    private val entityMock = mockk<Newsletter>(relaxed = true)
    private val recordMock = mockk<NewsletterRecord>()

    override val entity = entityMock

    override fun specificTearDown() {
        confirmVerified(entityMock, recordMock)
    }

    override fun entityFixture() {
        NewsletterRecordMapperTest.entityFixtureWithoutIdFields(entityMock)
    }

    override fun stepSetFixtureExceptAudit() {
        every { step.set(NEWSLETTER.ISSUE, NewsletterRecordMapperTest.ISSUE) } returns moreStep
        every { moreStep.set(NEWSLETTER.ISSUE_DATE, Date.valueOf(NewsletterRecordMapperTest.ISSUE_DATE)) } returns moreStep
        every { moreStep.set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id) } returns moreStep
    }

    override fun setStepFixtureAudit() {
        every { moreStep.set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) } returns moreStep
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify { entityMock.issue }
        verify { entityMock.issueDate }
        verify { entityMock.publicationStatus }
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify { step.set(NEWSLETTER.ISSUE, NewsletterRecordMapperTest.ISSUE) }
        verify { moreStep.set(NEWSLETTER.ISSUE_DATE, Date.valueOf(NewsletterRecordMapperTest.ISSUE_DATE)) }
        verify { moreStep.set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id) }
    }

    override fun verifySettingAuditFields() {
        verify { moreStep.set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) }
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        every { entity.id } returns null
        setter.considerSettingKeyOf(moreStep, entity)
        verify { entity.id }
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        every { entity.id } returns NewsletterRecordMapperTest.ID
        every { moreStep.set(NEWSLETTER.ID, NewsletterRecordMapperTest.ID) } returns moreStep

        setter.considerSettingKeyOf(moreStep, entity)

        verify { entity.id }
        verify { moreStep.set(NEWSLETTER.ID, NewsletterRecordMapperTest.ID) }
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entityMock, null)
        verify(exactly = 0) { entityMock.id = any() }
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        every { recordMock.id } returns 3
        every { moreStep.set(NEWSLETTER.ID, NewsletterRecordMapperTest.ID) } returns moreStep
        setter.resetIdToEntity(entityMock, recordMock)
        verify { recordMock.id }
        verify { entityMock.id = any() }
    }
}
