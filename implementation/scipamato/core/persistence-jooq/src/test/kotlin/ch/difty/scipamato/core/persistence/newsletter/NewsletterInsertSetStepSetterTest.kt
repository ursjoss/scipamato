package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import ch.difty.scipamato.core.persistence.RecordMapperTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.sql.Date

internal class NewsletterInsertSetStepSetterTest : InsertSetStepSetterTest<NewsletterRecord, Newsletter>() {

    override val setter = NewsletterInsertSetStepSetter()

    private val entityMock = mock<Newsletter>()
    private val recordMock = mock<NewsletterRecord>()

    override val entity = entityMock

    override fun specificTearDown() {
        verifyNoMoreInteractions(entityMock, recordMock)
    }

    override fun entityFixture() {
        NewsletterRecordMapperTest.entityFixtureWithoutIdFields(entityMock)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(NEWSLETTER.ISSUE, NewsletterRecordMapperTest.ISSUE)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.ISSUE_DATE, Date.valueOf(NewsletterRecordMapperTest.ISSUE_DATE))
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id)
    }

    override fun setStepFixtureAudit() {
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY)
        doReturn(moreStep).whenever(moreStep).set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify(entityMock).issue
        verify(entityMock).issueDate
        verify(entityMock).publicationStatus
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify(step).set(NEWSLETTER.ISSUE, NewsletterRecordMapperTest.ISSUE)
        verify(moreStep).set(NEWSLETTER.ISSUE_DATE, Date.valueOf(NewsletterRecordMapperTest.ISSUE_DATE))
        verify(moreStep).set(NEWSLETTER.PUBLICATION_STATUS, NewsletterRecordMapperTest.PUBLICATION_STATUS.id)
    }

    override fun verifySettingAuditFields() {
        verify(moreStep).set(NEWSLETTER.CREATED_BY, RecordMapperTest.CREATED_BY)
        verify(moreStep).set(NEWSLETTER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        whenever(entity.id).thenReturn(null)
        setter.considerSettingKeyOf(moreStep, entity)
        verify<Newsletter>(entity).id
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        whenever(entity.id).thenReturn(NewsletterRecordMapperTest.ID)

        setter.considerSettingKeyOf(moreStep, entity)

        verify<Newsletter>(entity).id
        verify(moreStep).set(NEWSLETTER.ID, NewsletterRecordMapperTest.ID)
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entityMock, null)
        verify(entityMock, never()).id = anyInt()
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        whenever(recordMock.id).thenReturn(3)
        setter.resetIdToEntity(entityMock, recordMock)
        verify(recordMock).id
        verify<Newsletter>(entityMock).id = anyInt()
    }

}