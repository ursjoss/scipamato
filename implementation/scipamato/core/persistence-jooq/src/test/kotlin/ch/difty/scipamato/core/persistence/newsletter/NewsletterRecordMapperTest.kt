package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.persistence.RecordMapperTest
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.RecordMapper
import org.junit.jupiter.api.Test
import java.sql.Date
import java.time.LocalDate

internal class NewsletterRecordMapperTest : RecordMapperTest<NewsletterRecord, Newsletter>() {

    override val mapper: RecordMapper<NewsletterRecord, Newsletter>
        get() = NewsletterRecordMapper()

    override fun makeRecord(): NewsletterRecord = NewsletterRecord().apply {
        id = ID
        issue = ISSUE
        issueDate = Date.valueOf(ISSUE_DATE)
        publicationStatus = PUBLICATION_STATUS.id
    }

    override fun setAuditFieldsIn(record: NewsletterRecord) {
        record.created = CREATED
        record.createdBy = CREATED_BY
        record.lastModified = LAST_MOD
        record.lastModifiedBy = LAST_MOD_BY
        record.version = VERSION
    }

    override fun assertEntity(entity: Newsletter) {
        assertThat(entity.id).isEqualTo(ID)
        assertThat(entity.issue).isEqualTo(ISSUE)
        assertThat(entity.issueDate).isEqualTo(LocalDate.parse(ISSUE_DATE))
        assertThat(entity.publicationStatus).isEqualTo(PUBLICATION_STATUS)
        assertThat(entity.topics).isEmpty()
    }

    @Test
    fun mapping_withIssueDateInRecordNull_mapsRecordToEntity_withIssueDateNull() {
        val record = makeRecord()
        setAuditFieldsIn(record)
        record.issueDate = null
        val entity = mapper.map(record)
        assertThat(entity.issueDate).isNull()
    }

    companion object {
        const val ID = 1
        const val ISSUE = "issue"
        const val ISSUE_DATE = "2018-04-24"
        val PUBLICATION_STATUS = PublicationStatus.WIP

        fun entityFixtureWithoutIdFields(entityMock: Newsletter) {
            whenever(entityMock.issue).thenReturn(ISSUE)
            whenever(entityMock.issueDate).thenReturn(LocalDate.parse(ISSUE_DATE))
            whenever(entityMock.publicationStatus).thenReturn(PUBLICATION_STATUS)
        }
    }
}