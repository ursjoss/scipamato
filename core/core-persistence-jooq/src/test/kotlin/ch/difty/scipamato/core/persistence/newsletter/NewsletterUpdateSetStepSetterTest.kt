package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRecordMapperTest.Companion.ISSUE
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRecordMapperTest.Companion.ISSUE_DATE
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import java.sql.Date

internal class NewsletterUpdateSetStepSetterTest : UpdateSetStepSetterTest<NewsletterRecord, Newsletter>() {

    private val entityMock = mock<Newsletter>()
    override val entity = entityMock

    override val setter = NewsletterUpdateSetStepSetter()

    override fun specificTearDown() {
        verifyNoMoreInteractions(entityMock)
    }

    override fun entityFixture() {
        NewsletterRecordMapperTest.entityFixtureWithoutIdFields(entityMock)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(NEWSLETTER.ISSUE, ISSUE)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE))
        doReturn(moreStep)
            .whenever(moreStep).set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id)
    }

    override fun stepSetFixtureAudit() {
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.CREATED, RecordMapperTest.CREATED)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.LAST_MODIFIED, RecordMapperTest.LAST_MOD)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.VERSION, RecordMapperTest.VERSION + 1)
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify(entityMock).issue
        verify(entityMock).issueDate
        verify(entityMock).publicationStatus
    }

    override fun verifyStepSettingExceptAudit() {
        verify(step).set(NEWSLETTER.ISSUE, ISSUE)
        verify(moreStep).set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE))
        verify(moreStep).set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id)
    }

    override fun verifyStepSettingAudit() {
        verify(moreStep).set(NEWSLETTER.CREATED, RecordMapperTest.CREATED)
        verify(moreStep).set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY)
        verify(moreStep).set(NEWSLETTER.LAST_MODIFIED, RecordMapperTest.LAST_MOD)
        verify(moreStep).set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
        verify(moreStep).set(NEWSLETTER.VERSION, RecordMapperTest.VERSION + 1)
    }
}
