package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.GLOBAL;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.NAME;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.OWNER;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetterTest;

public class SearchOrderInsertSetStepSetterTest extends InsertSetStepSetterTest<SearchOrderRecord, SearchOrder> {

    private final InsertSetStepSetter<SearchOrderRecord, SearchOrder> setter = new SearchOrderInsertSetStepSetter();

    @Mock
    private SearchOrder entityMock;

    @Mock
    private SearchOrderRecord recordMock;

    @Override
    protected InsertSetStepSetter<SearchOrderRecord, SearchOrder> getSetter() {
        return setter;
    }

    @Override
    protected SearchOrder getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock, recordMock);
    }

    @Override
    protected void entityFixture() {
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixture() {
        when(getStep().set(SEARCH_ORDER.NAME, NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.OWNER, OWNER)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.GLOBAL, GLOBAL)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllNonKeyFields() {
        verify(entityMock).getName();
        verify(entityMock).getOwner();
        verify(entityMock).isGlobal();
    }

    @Override
    protected void verifySetting() {
        verify(getStep()).set(SEARCH_ORDER.NAME, NAME);
        verify(getMoreStep()).set(SEARCH_ORDER.OWNER, OWNER);
        verify(getMoreStep()).set(SEARCH_ORDER.GLOBAL, GLOBAL);
    }

    @Test
    public void consideringSettingKeyOf_withNullId_doesNotSetId() {
        when(getEntity().getId()).thenReturn(null);
        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());
        verify(getEntity()).getId();
    }

    @Test
    public void consideringSettingKeyOf_withNonNullId_doesSetId() {
        when(getEntity().getId()).thenReturn(ID);

        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());

        verify(getEntity()).getId();
        verify(getMoreStep()).set(SEARCH_ORDER.ID, ID);
    }

    @Test
    public void resettingIdToEntity_withNullRecord_doesNothing() {
        getSetter().resetIdToEntity(entityMock, null);
        verify(entityMock, never()).setId(Mockito.anyLong());
    }

    @Test
    public void resettingIdToEntity_withNonNullRecord_setsId() {
        when(recordMock.getId()).thenReturn(3l);
        getSetter().resetIdToEntity(entityMock, recordMock);
        verify(recordMock).getId();
        verify(entityMock).setId(Mockito.anyLong());
    }

}
