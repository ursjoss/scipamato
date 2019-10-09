package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.InsertSetStepSetter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import ch.difty.scipamato.core.persistence.RecordMapperTest
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong

internal class SearchOrderInsertSetStepSetterTest : InsertSetStepSetterTest<SearchOrderRecord, SearchOrder>() {

    override val setter: InsertSetStepSetter<SearchOrderRecord, SearchOrder> = SearchOrderInsertSetStepSetter()
    override val entity = mock<SearchOrder>()
    private val recordMock = mock<SearchOrderRecord>()

    override fun specificTearDown() {
        verifyNoMoreInteractions(entity, recordMock)
    }

    override fun entityFixture() {
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL)
    }

    override fun setStepFixtureAudit() {
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY)
        doReturn(moreStep).whenever(moreStep).set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify(entity).name
        verify(entity).owner
        verify(entity).isGlobal
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify(step).set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME)
        verify(moreStep).set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER)
        verify(moreStep).set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL)
    }

    override fun verifySettingAuditFields() {
        verify(moreStep).set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY)
        verify(moreStep).set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY)
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        whenever(entity.id).thenReturn(null)
        setter.considerSettingKeyOf(moreStep, entity)
        verify(entity).id
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        whenever(entity.id).thenReturn(SearchOrderRecordMapperTest.ID)

        setter.considerSettingKeyOf(moreStep, entity)

        verify(entity).id
        verify(moreStep).set(SEARCH_ORDER.ID, SearchOrderRecordMapperTest.ID)
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entity, null)
        verify(entity, never()).id = anyLong()
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        whenever(recordMock.id).thenReturn(3L)
        setter.resetIdToEntity(entity, recordMock)
        verify(recordMock).id
        verify(entity).id = anyLong()
    }
}
