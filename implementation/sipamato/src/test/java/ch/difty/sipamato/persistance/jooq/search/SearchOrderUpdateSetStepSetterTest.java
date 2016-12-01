package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.GLOBAL;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.search.SearchOrderRecordMapperTest.OWNER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetterTest;

public class SearchOrderUpdateSetStepSetterTest extends UpdateSetStepSetterTest<SearchOrderRecord, SearchOrder> {

    private final UpdateSetStepSetter<SearchOrderRecord, SearchOrder> setter = new SearchOrderUpdateSetStepSetter();

    @Mock
    private SearchOrder entityMock;

    @Override
    protected UpdateSetStepSetter<SearchOrderRecord, SearchOrder> getSetter() {
        return setter;
    }

    @Override
    protected SearchOrder getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock);
    }

    @Override
    protected void entityFixture() {
        when(entityMock.getId()).thenReturn(ID);
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixture() {
        when(getStep().set(SEARCH_ORDER.OWNER, OWNER)).thenReturn(getMoreStep());

        when(getMoreStep().set(SEARCH_ORDER.GLOBAL, GLOBAL)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllFields() {
        verify(entityMock).getOwner();
        verify(entityMock).isGlobal();
    }

    @Override
    protected void verifySetting() {
        verify(getStep()).set(SEARCH_ORDER.OWNER, OWNER);
        verify(getMoreStep()).set(SEARCH_ORDER.GLOBAL, GLOBAL);
    }

}
