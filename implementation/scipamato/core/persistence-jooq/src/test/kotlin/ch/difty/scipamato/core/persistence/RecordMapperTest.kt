package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.CoreEntity
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.Record
import org.jooq.RecordMapper
import org.junit.jupiter.api.Test
import java.sql.Timestamp

abstract class RecordMapperTest<R : Record, E : CoreEntity> {

    protected abstract val mapper: RecordMapper<R, E>

    @Test
    internal fun mapping_mapsRecordToEntity() {
        val record = makeRecord()
        setAuditFieldsIn(record)
        val entity = mapper.map(record)
        assertEntity(entity)
        assertAuditFieldsOf(entity)
    }

    /**
     * Create the record and set its field (except for the audit fields, which are
     * set separately).
     */
    protected abstract fun makeRecord(): R

    /**
     * `<pre>
     * record.setCreated(CREATED);
     * record.setCreatedBy(CREATED_BY);
     * record.setLastModified(LAST_MOD);
     * record.setLastModifiedBy(LAST_MOD_BY);
     * record.setVersion(VERSION);
    </pre>` *
     *
     * @param record
     * for which the audit fields are set into
     */
    protected abstract fun setAuditFieldsIn(record: R)

    /**
     * Assert non-audit fields of entity (audit fields are asserted separately)
     *
     * @param entity
     * the entity to assert the non-audit fields for
     */
    protected abstract fun assertEntity(entity: E)

    private fun assertAuditFieldsOf(e: E) {
        assertThat(e.version).isEqualTo(VERSION)
        assertThat(e.created).isEqualTo(CREATED.toLocalDateTime())
        assertThat(e.createdBy).isEqualTo(CREATED_BY)
        assertThat(e.lastModified).isEqualTo(LAST_MOD.toLocalDateTime())
        assertThat(e.lastModifiedBy).isEqualTo(LAST_MOD_BY)

        // not enriched by service
        assertThat(e.createdByName).isNull()
        assertThat(e.createdByFullName).isNull()
        assertThat(e.lastModifiedByName).isNull()
    }

    companion object {
        const val VERSION = 1
        val CREATED = Timestamp(1469999999999L)
        const val CREATED_BY = 1
        val LAST_MOD = Timestamp(1479999999999L)
        const val LAST_MOD_BY = 2

        /**
         * Test fixture for the entity mock audit fields.
         *
         * @param entityMock
         * the mocked entity
         */
        fun auditFixtureFor(entityMock: CoreEntity) {
            whenever(entityMock.createdBy).thenReturn(CREATED_BY)
            whenever(entityMock.lastModifiedBy).thenReturn(LAST_MOD_BY)
        }

        /**
         * Test fixture for the entity mock audit fields.
         *
         * @param entityMock
         * the mocked entity
         */
        fun auditExtendedFixtureFor(entityMock: CoreEntity) {
            whenever(entityMock.created).thenReturn(CREATED.toLocalDateTime())
            whenever(entityMock.lastModified).thenReturn(LAST_MOD.toLocalDateTime())
            whenever(entityMock.version).thenReturn(VERSION)
        }
    }

}
