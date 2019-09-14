package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.Mockito.*

internal class SearchOrderUpdateSetStepSetterTest : UpdateSetStepSetterTest<SearchOrderRecord, SearchOrder>() {

    override val setter: UpdateSetStepSetter<SearchOrderRecord, SearchOrder> = SearchOrderUpdateSetStepSetter()

    override val entity = mock<SearchOrder>()

    override fun specificTearDown() {
        verifyNoMoreInteractions(entity)
    }

    override fun entityFixture() {
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL)
    }

    override fun stepSetFixtureAudit() {
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.CREATED, RecordMapperTest.CREATED)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.LAST_MODIFIED, RecordMapperTest.LAST_MOD)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.VERSION, RecordMapperTest.VERSION + 1)
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify<SearchOrder>(entity).name
        verify<SearchOrder>(entity).owner
        verify<SearchOrder>(entity).isGlobal
    }

    override fun verifyStepSettingExceptAudit() {
        verify(step).set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME)
        verify(moreStep).set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER)
        verify(moreStep).set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL)
    }

    override fun verifyStepSettingAudit() {
        verify(moreStep).set(SEARCH_ORDER.CREATED, RecordMapperTest.CREATED)
        verify(moreStep).set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY)
        verify(moreStep).set(SEARCH_ORDER.LAST_MODIFIED, RecordMapperTest.LAST_MOD)
        verify(moreStep).set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
        verify(moreStep).set(SEARCH_ORDER.VERSION, RecordMapperTest.VERSION + 1)
    }

}
