package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.RecordMapperTest
import io.mockk.every
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.jooq.RecordMapper

internal class SearchOrderRecordMapperTest : RecordMapperTest<SearchOrderRecord, SearchOrder>() {

    override val mapper: RecordMapper<SearchOrderRecord, SearchOrder> = SearchOrderRecordMapper()

    override fun makeRecord(): SearchOrderRecord {
        return SearchOrderRecord().apply {
            id = ID
            name = NAME
            owner = OWNER
            global = GLOBAL
        }
    }

    override fun setAuditFieldsIn(record: SearchOrderRecord) {
        record.created = CREATED
        record.createdBy = CREATED_BY
        record.lastModified = LAST_MOD
        record.lastModifiedBy = LAST_MOD_BY
        record.version = VERSION
    }

    override fun assertEntity(entity: SearchOrder) {
        entity.id shouldBeEqualTo ID
        entity.name shouldBeEqualTo NAME
        entity.owner shouldBeEqualTo OWNER
        entity.isGlobal shouldBeEqualTo GLOBAL

        entity.searchConditions.shouldBeEmpty()

        // not persisted and therefore always false
        entity.isShowExcluded.shouldBeFalse()
    }

    companion object {
        const val ID = 2L
        const val NAME = "soName"
        const val OWNER = 1
        const val GLOBAL = true

        fun entityFixtureWithoutIdFields(entityMock: SearchOrder) {
            every { entityMock.name } returns NAME
            every { entityMock.owner } returns OWNER
            every { entityMock.isGlobal } returns GLOBAL

            auditFixtureFor(entityMock)
        }
    }
}
