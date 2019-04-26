package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.scipamato.core.persistence.search.SearchOrderRecordMapperTest.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
class SearchOrderUpdateSetStepSetterTest extends UpdateSetStepSetterTest<SearchOrderRecord, SearchOrder> {

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
        doReturn(getMoreStep())
            .when(getStep())
            .set(SEARCH_ORDER.NAME, NAME);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.OWNER, OWNER);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.GLOBAL, GLOBAL);
    }

    @Override
    protected void stepSetFixtureAudit() {
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.CREATED, CREATED);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.CREATED_BY, CREATED_BY);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.LAST_MODIFIED, LAST_MOD);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.LAST_MODIFIED_BY, LAST_MOD_BY);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.VERSION, VERSION + 1);
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
