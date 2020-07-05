package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class SearchOrderUpdateSetStepSetterTest : UpdateSetStepSetterTest<SearchOrderRecord, SearchOrder>() {

    override val setter: UpdateSetStepSetter<SearchOrderRecord, SearchOrder> = SearchOrderUpdateSetStepSetter()

    override val entity = mockk<SearchOrder>()

    override fun specificTearDown() {
        confirmVerified(entity)
    }

    override fun entityFixture() {
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        every { step.set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL) } returns moreStep
    }

    override fun stepSetFixtureAudit() {
        every { moreStep.set(SEARCH_ORDER.CREATED, RecordMapperTest.CREATED) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.VERSION, RecordMapperTest.VERSION + 1) } returns moreStep
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify { entity.name }
        verify { entity.owner }
        verify { entity.isGlobal }
    }

    override fun verifyStepSettingExceptAudit() {
        verify { step.set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME) }
        verify { moreStep.set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER) }
        verify { moreStep.set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL) }
    }

    override fun verifyStepSettingAudit() {
        verify { moreStep.set(SEARCH_ORDER.CREATED, RecordMapperTest.CREATED) }
        verify { moreStep.set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(SEARCH_ORDER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) }
        verify { moreStep.set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) }
        verify { moreStep.set(SEARCH_ORDER.VERSION, RecordMapperTest.VERSION + 1) }
    }
}
