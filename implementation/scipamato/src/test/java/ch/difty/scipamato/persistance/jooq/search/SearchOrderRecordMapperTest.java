package ch.difty.scipamato.persistance.jooq.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.RecordMapper;

import ch.difty.scipamato.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.persistance.jooq.RecordMapperTest;

public class SearchOrderRecordMapperTest extends RecordMapperTest<SearchOrderRecord, SearchOrder> {

    static final Long ID = 2l;
    static final String NAME = "soName";
    static final Integer OWNER = 1;
    static final boolean GLOBAL = true;

    public static void entityFixtureWithoutIdFields(SearchOrder entityMock) {
        when(entityMock.getId()).thenReturn(ID);
        when(entityMock.getName()).thenReturn(NAME);
        when(entityMock.getOwner()).thenReturn(OWNER);
        when(entityMock.isGlobal()).thenReturn(GLOBAL);

        auditFixtureFor(entityMock);
    }

    @Override
    protected RecordMapper<SearchOrderRecord, SearchOrder> getMapper() {
        return new SearchOrderRecordMapper();
    }

    @Override
    protected SearchOrderRecord makeRecord() {
        SearchOrderRecord record = new SearchOrderRecord();
        record.setId(ID);
        record.setName(NAME);
        record.setOwner(OWNER);
        record.setGlobal(GLOBAL);
        return record;
    }

    @Override
    protected void setAuditFieldsIn(SearchOrderRecord record) {
        record.setCreated(CREATED);
        record.setCreatedBy(CREATED_BY);
        record.setLastModified(LAST_MOD);
        record.setLastModifiedBy(LAST_MOD_BY);
        record.setVersion(VERSION);
    }

    @Override
    protected void assertEntity(SearchOrder entity) {
        assertThat(entity.getId()).isEqualTo(ID);
        assertThat(entity.getName()).isEqualTo(NAME);
        assertThat(entity.getOwner()).isEqualTo(OWNER);
        assertThat(entity.isGlobal()).isEqualTo(GLOBAL);

        assertThat(entity.getSearchConditions()).isEmpty();

        // not persisted and therefore always false
        assertThat(entity.isInvertExclusions()).isFalse();
    }

}
