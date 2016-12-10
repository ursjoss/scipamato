package ch.difty.sipamato.persistance.jooq;

import java.sql.Timestamp;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;

public abstract class RecordMapperTest<R extends Record, E> {

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
    }

    protected abstract R makeRecord();

    protected abstract void assertEntity(E entity);

}
