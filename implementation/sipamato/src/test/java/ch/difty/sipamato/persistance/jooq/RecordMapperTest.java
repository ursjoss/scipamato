package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.junit.Test;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.lib.NullArgumentException;

public abstract class RecordMapperTest<R extends Record, E extends SipamatoEntity> {

    private final RecordMapper<R, E> mapper = getMapper();

    protected abstract RecordMapper<R, E> getMapper();

    protected static final int VERSION = 1;
    protected static final Timestamp CREATED = new Timestamp(1469999999999l);
    protected static final Integer CREATED_BY = 1;
    protected static final Timestamp LAST_MOD = new Timestamp(1479999999999l);
    protected static final Integer LAST_MOD_BY = 2;

    @Test(expected = NullArgumentException.class)
    public void mappingWithNullEntity_throws() {
        mapper.map(null);
    }

    @Test
    public void mapping_mapsRecordToEntity() {
        R record = makeRecord();
        E entity = mapper.map(record);
        assertEntity(entity);
        assertAuditFieldsOf(entity);
    }

    protected abstract R makeRecord();

    /**
     * Assert non-audit fields of entity
     * @param entity
     */
    protected abstract void assertEntity(E entity);

    protected void assertAuditFieldsOf(E e) {
        assertThat(e.getVersion()).isEqualTo(VERSION);
        assertThat(e.getCreated()).isEqualTo(CREATED.toLocalDateTime());
        assertThat(e.getCreatedBy()).isEqualTo(CREATED_BY);
        assertThat(e.getLastModified()).isEqualTo(LAST_MOD.toLocalDateTime());
        assertThat(e.getLastModifiedBy()).isEqualTo(LAST_MOD_BY);

        // not enriched by service
        assertThat(e.getCreatedByName()).isNull();
        assertThat(e.getLastModifiedByName()).isNull();
    }

}
