package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.scipamato.core.persistence.search.SearchOrderRecordMapperTest.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
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
    protected void setStepFixtureAudit() {
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.CREATED_BY, SearchOrderRecordMapperTest.CREATED_BY);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(SEARCH_ORDER.LAST_MODIFIED_BY, SearchOrderRecordMapperTest.LAST_MOD_BY);
    }

    @Override
    protected void verifyCallToFieldsExceptKeyAndAudit() {
        verify(entityMock).getName();
        verify(entityMock).getOwner();
        verify(entityMock).isGlobal();
    }

    @Override
    protected void verifySettingFieldsExceptKeyAndAudit() {
        verify(getStep()).set(SEARCH_ORDER.NAME, NAME);
        verify(getMoreStep()).set(SEARCH_ORDER.OWNER, OWNER);
        verify(getMoreStep()).set(SEARCH_ORDER.GLOBAL, GLOBAL);
    }

    @Override
    protected void verifySettingAuditFields() {
        verify(getMoreStep()).set(SEARCH_ORDER.CREATED_BY, SearchOrderRecordMapperTest.CREATED_BY);
        verify(getMoreStep()).set(SEARCH_ORDER.LAST_MODIFIED_BY, SearchOrderRecordMapperTest.LAST_MOD_BY);
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
        verify(entityMock, never()).setId(anyLong());
    }

    @Test
    public void resettingIdToEntity_withNonNullRecord_setsId() {
        when(recordMock.getId()).thenReturn(3L);
        getSetter().resetIdToEntity(entityMock, recordMock);
        verify(recordMock).getId();
        verify(entityMock).setId(anyLong());
    }

}
