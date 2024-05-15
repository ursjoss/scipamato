package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.CoreEntity
import io.mockk.every
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.jooq.Record
import org.jooq.RecordMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.sql.Timestamp

abstract class RecordMapperTest<R : Record, E : CoreEntity> {

    protected abstract val mapper: RecordMapper<R, E>

    @Test
    internal fun mapping_mapsRecordToEntity() {
        val record = makeRecord()
        setAuditFieldsIn(record)
        val entity = mapper.map(record) ?: fail("Unable to get entity")
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
        e.version shouldBeEqualTo VERSION
        e.created shouldBeEqualTo CREATED.toLocalDateTime()
        e.createdBy shouldBeEqualTo CREATED_BY
        e.lastModified shouldBeEqualTo LAST_MOD.toLocalDateTime()
        e.lastModifiedBy shouldBeEqualTo LAST_MOD_BY

        // not enriched by service
        e.createdByName.shouldBeNull()
        e.createdByFullName.shouldBeNull()
        e.lastModifiedByName.shouldBeNull()
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
            every { entityMock.createdBy } returns CREATED_BY
            every { entityMock.lastModifiedBy } returns LAST_MOD_BY
        }

        /**
         * Test fixture for the entity mock audit fields.
         *
         * @param entityMock
         * the mocked entity
         */
        fun auditExtendedFixtureFor(entityMock: CoreEntity) {
            every { entityMock.created } returns CREATED.toLocalDateTime()
            every { entityMock.lastModified } returns LAST_MOD.toLocalDateTime()
            every { entityMock.version } returns VERSION
        }
    }
}
