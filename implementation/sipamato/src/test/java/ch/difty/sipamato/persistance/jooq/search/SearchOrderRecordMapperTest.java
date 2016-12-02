package ch.difty.sipamato.persistance.jooq.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.RecordMapper;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.RecordMapperTest;

public class SearchOrderRecordMapperTest extends RecordMapperTest<SearchOrderRecord, SearchOrder> {

    static final Long ID = 2l;
    static final Integer OWNER = 1;
    static final boolean GLOBAL = true;

    public static void entityFixtureWithoutIdFields(SearchOrder entityMock) {
        when(entityMock.getId()).thenReturn(ID);
        when(entityMock.getOwner()).thenReturn(OWNER);
        when(entityMock.isGlobal()).thenReturn(GLOBAL);
    }

    @Override
    protected RecordMapper<SearchOrderRecord, SearchOrder> getMapper() {
        return new SearchOrderRecordMapper();
    }

    @Override
    protected SearchOrderRecord makeRecord() {
        SearchOrderRecord record = new SearchOrderRecord();
        record.setId(ID);
        record.setOwner(OWNER);
        record.setGlobal(GLOBAL);
        return record;
    }

    @Override
    protected void assertEntity(SearchOrder entity) {
        assertThat(entity.getId()).isEqualTo(ID);
        assertThat(entity.getOwner()).isEqualTo(OWNER);
        assertThat(entity.isGlobal()).isEqualTo(GLOBAL);
    }

}
