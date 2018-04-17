package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY;
import static ch.difty.scipamato.core.persistence.RecordMapperTest.VERSION;
import static ch.difty.scipamato.core.persistence.search.SearchOrderRecordMapperTest.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest;

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
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixtureExceptAudit() {
        when(getStep().set(SEARCH_ORDER.NAME, NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.OWNER, OWNER)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.GLOBAL, GLOBAL)).thenReturn(getMoreStep());
    }

    @Override
    protected void stepSetFixtureAudit() {
        when(getMoreStep().set(SEARCH_ORDER.CREATED, CREATED)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(SEARCH_ORDER.VERSION, VERSION + 1)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllFieldsExceptAudit() {
        verify(entityMock).getName();
        verify(entityMock).getOwner();
        verify(entityMock).isGlobal();
    }

    @Override
    protected void verifyStepSettingExceptAudit() {
        verify(getStep()).set(SEARCH_ORDER.NAME, NAME);
        verify(getMoreStep()).set(SEARCH_ORDER.OWNER, OWNER);
        verify(getMoreStep()).set(SEARCH_ORDER.GLOBAL, GLOBAL);
    }

    @Override
    protected void verifyStepSettingAudit() {
        verify(getMoreStep()).set(SEARCH_ORDER.CREATED, CREATED);
        verify(getMoreStep()).set(SEARCH_ORDER.CREATED_BY, CREATED_BY);
        verify(getMoreStep()).set(SEARCH_ORDER.LAST_MODIFIED, LAST_MOD);
        verify(getMoreStep()).set(SEARCH_ORDER.LAST_MODIFIED_BY, LAST_MOD_BY);
        verify(getMoreStep()).set(SEARCH_ORDER.VERSION, VERSION + 1);
    }

}
