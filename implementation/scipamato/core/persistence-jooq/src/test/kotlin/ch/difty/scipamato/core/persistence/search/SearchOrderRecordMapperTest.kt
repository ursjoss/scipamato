package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.RecordMapperTest
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(entity.id).isEqualTo(ID)
        assertThat(entity.name).isEqualTo(NAME)
        assertThat(entity.owner).isEqualTo(OWNER)
        assertThat(entity.isGlobal).isEqualTo(GLOBAL)

        assertThat(entity.searchConditions).isEmpty()

        // not persisted and therefore always false
        assertThat(entity.isShowExcluded).isFalse()
    }

    companion object {
        const val ID = 2L
        const val NAME = "soName"
        const val OWNER = 1
        const val GLOBAL = true

        fun entityFixtureWithoutIdFields(entityMock: SearchOrder) {
            whenever(entityMock.name).thenReturn(NAME)
            whenever(entityMock.owner).thenReturn(OWNER)
            whenever(entityMock.isGlobal).thenReturn(GLOBAL)

            auditFixtureFor(entityMock)
        }
    }

}
