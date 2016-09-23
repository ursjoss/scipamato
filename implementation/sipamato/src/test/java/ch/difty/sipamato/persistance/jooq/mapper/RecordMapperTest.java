package ch.difty.sipamato.persistance.jooq.mapper;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;

public abstract class RecordMapperTest<R extends Record, E> {

    private final RecordMapper<R, E> mapper = getMapper();

    protected abstract RecordMapper<R, E> getMapper();

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
