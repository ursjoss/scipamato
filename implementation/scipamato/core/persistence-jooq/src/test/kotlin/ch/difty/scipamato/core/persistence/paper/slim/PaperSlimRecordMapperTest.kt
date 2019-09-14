package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.paper.PaperRecordMapperTest
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(entity.id).isEqualTo(PaperRecordMapperTest.ID)
        assertThat(entity.number).isEqualTo(PaperRecordMapperTest.NUMBER)
        assertThat(entity.firstAuthor).isEqualTo(PaperRecordMapperTest.FIRST_AUTHOR)
        assertThat(entity.title).isEqualTo(PaperRecordMapperTest.TITLE)
        assertThat(entity.publicationYear).isEqualTo(PaperRecordMapperTest.PUBLICATION_YEAR)
    }

    companion object {
        fun entityFixtureWithoutIdFields(entity: PaperSlim) {
            whenever(entity.number).thenReturn(PaperRecordMapperTest.NUMBER)
            whenever(entity.firstAuthor).thenReturn(PaperRecordMapperTest.FIRST_AUTHOR)
            whenever(entity.title).thenReturn(PaperRecordMapperTest.TITLE)
            whenever(entity.publicationYear).thenReturn(PaperRecordMapperTest.PUBLICATION_YEAR)

            auditFixtureFor(entity)
        }
    }

}
