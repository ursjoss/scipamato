package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.persistence.RecordMapperTest
import io.mockk.every
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.jooq.RecordMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
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

    override fun setAuditFieldsIn(rcd: NewsletterRecord) {
        rcd.created = CREATED
        rcd.createdBy = CREATED_BY
        rcd.lastModified = LAST_MOD
        rcd.lastModifiedBy = LAST_MOD_BY
        rcd.version = VERSION
    }

    override fun assertEntity(entity: Newsletter) {
        entity.id shouldBeEqualTo ID
        entity.issue shouldBeEqualTo ISSUE
        entity.issueDate shouldBeEqualTo LocalDate.parse(ISSUE_DATE)
        entity.publicationStatus shouldBeEqualTo PUBLICATION_STATUS
        entity.topics.shouldBeEmpty()
    }

    @Test
    fun mapping_withIssueDateInRecordNull_mapsRecordToEntity_withIssueDateNull() {
        val rcd = makeRecord()
        setAuditFieldsIn(rcd)
        rcd.issueDate = null
        val entity = mapper.map(rcd) ?: fail("unable to get entity")
        entity.issueDate.shouldBeNull()
    }

    companion object {
        const val ID = 1
        const val ISSUE = "issue"
        const val ISSUE_DATE = "2018-04-24"
        val PUBLICATION_STATUS = PublicationStatus.WIP

        fun entityFixtureWithoutIdFields(entityMock: Newsletter) {
            every { entityMock.issue } returns ISSUE
            every { entityMock.issueDate } returns LocalDate.parse(ISSUE_DATE)
            every { entityMock.publicationStatus } returns PUBLICATION_STATUS
        }
    }
}
