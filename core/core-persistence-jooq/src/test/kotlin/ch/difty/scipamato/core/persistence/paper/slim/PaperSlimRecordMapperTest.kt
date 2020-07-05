package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.paper.PaperRecordMapperTest
import io.mockk.every
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.RecordMapper

internal class PaperSlimRecordMapperTest : RecordMapperTest<PaperRecord, PaperSlim>() {

    override val mapper: RecordMapper<PaperRecord, PaperSlim> = PaperSlimRecordMapper()

    override fun makeRecord(): PaperRecord =
        PaperRecord().apply {
            id = PaperRecordMapperTest.ID
            number = PaperRecordMapperTest.NUMBER
            firstAuthor = PaperRecordMapperTest.FIRST_AUTHOR
            title = PaperRecordMapperTest.TITLE
            publicationYear = PaperRecordMapperTest.PUBLICATION_YEAR
        }

    override fun setAuditFieldsIn(record: PaperRecord) {
        record.created = CREATED
        record.createdBy = CREATED_BY
        record.lastModified = LAST_MOD
        record.lastModifiedBy = LAST_MOD_BY
        record.version = VERSION
    }

    override fun assertEntity(entity: PaperSlim) {
        entity.id shouldBeEqualTo PaperRecordMapperTest.ID
        entity.number shouldBeEqualTo PaperRecordMapperTest.NUMBER
        entity.firstAuthor shouldBeEqualTo PaperRecordMapperTest.FIRST_AUTHOR
        entity.title shouldBeEqualTo PaperRecordMapperTest.TITLE
        entity.publicationYear shouldBeEqualTo PaperRecordMapperTest.PUBLICATION_YEAR
    }

    companion object {
        fun entityFixtureWithoutIdFields(entity: PaperSlim) {
            every { entity.number } returns PaperRecordMapperTest.NUMBER
            every { entity.firstAuthor } returns PaperRecordMapperTest.FIRST_AUTHOR
            every { entity.title } returns PaperRecordMapperTest.TITLE
            every { entity.publicationYear } returns PaperRecordMapperTest.PUBLICATION_YEAR

            auditFixtureFor(entity)
        }
    }
}
