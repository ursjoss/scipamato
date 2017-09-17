package ch.difty.scipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.ScipamatoEntity;

public abstract class RecordMapperTest<R extends Record, E extends ScipamatoEntity> {

    public static final int VERSION = 1;
    public static final Timestamp CREATED = new Timestamp(1469999999999l);
    public static final Integer CREATED_BY = 1;
    public static final Timestamp LAST_MOD = new Timestamp(1479999999999l);
    public static final Integer LAST_MOD_BY = 2;

    private final RecordMapper<R, E> mapper = getMapper();

    protected abstract RecordMapper<R, E> getMapper();

    /**
     * Test fixture for the entity mock audit fields.
     *
     * @param entityMock
     */
    protected static void auditFixtureFor(ScipamatoEntity entityMock) {
        when(entityMock.getCreated()).thenReturn(CREATED.toLocalDateTime());
        when(entityMock.getCreatedBy()).thenReturn(CREATED_BY);
        when(entityMock.getLastModified()).thenReturn(LAST_MOD.toLocalDateTime());
        when(entityMock.getLastModifiedBy()).thenReturn(LAST_MOD_BY);
        when(entityMock.getVersion()).thenReturn(VERSION);
    }

    @Test(expected = NullArgumentException.class)
    public void mappingWithNullEntity_throws() {
        mapper.map(null);
    }

    @Test
    public void mapping_mapsRecordToEntity() {
        R record = makeRecord();
        setAuditFieldsIn(record);
        E entity = mapper.map(record);
        assertEntity(entity);
        assertAuditFieldsOf(entity);
    }

    /**
     * Create the record and set its field (except for the audit fields, which are set separately).
     */
    protected abstract R makeRecord();

    /**
     * @param set the audit fields into the record, typically as such:
     *
     * <code><pre>
     *  record.setCreated(CREATED);
     *  record.setCreatedBy(CREATED_BY);
     *  record.setLastModified(LAST_MOD);
     *  record.setLastModifiedBy(LAST_MOD_BY);
     *  record.setVersion(VERSION);
     *  </pre></code>
     */
    protected abstract void setAuditFieldsIn(R record);

    /**
     * Assert non-audit fields of entity (audit fields are asserted separately)
     * @param entity
     */
    protected abstract void assertEntity(E entity);

    private void assertAuditFieldsOf(E e) {
        assertThat(e.getVersion()).isEqualTo(VERSION);
        assertThat(e.getCreated()).isEqualTo(CREATED.toLocalDateTime());
        assertThat(e.getCreatedBy()).isEqualTo(CREATED_BY);
        assertThat(e.getLastModified()).isEqualTo(LAST_MOD.toLocalDateTime());
        assertThat(e.getLastModifiedBy()).isEqualTo(LAST_MOD_BY);

        // not enriched by service
        assertThat(e.getCreatedByName()).isNull();
        assertThat(e.getCreatedByFullName()).isNull();
        assertThat(e.getLastModifiedByName()).isNull();
    }

}
