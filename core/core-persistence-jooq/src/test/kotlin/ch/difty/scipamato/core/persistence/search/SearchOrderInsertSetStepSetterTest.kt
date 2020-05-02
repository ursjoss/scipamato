package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.InsertSetStepSetter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import ch.difty.scipamato.core.persistence.RecordMapperTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class SearchOrderInsertSetStepSetterTest : InsertSetStepSetterTest<SearchOrderRecord, SearchOrder>() {

    override val setter: InsertSetStepSetter<SearchOrderRecord, SearchOrder> = SearchOrderInsertSetStepSetter()
    override val entity = mockk<SearchOrder>(relaxed = true)
    private val recordMock = mockk<SearchOrderRecord>()

    override fun specificTearDown() {
        confirmVerified(entity, recordMock)
    }

    override fun entityFixture() {
        SearchOrderRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        every { step.set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL) } returns moreStep
    }

    override fun setStepFixtureAudit() {
        every { moreStep.set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) } returns moreStep
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify { entity.name }
        verify { entity.owner }
        verify { entity.isGlobal }
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify { step.set(SEARCH_ORDER.NAME, SearchOrderRecordMapperTest.NAME) }
        verify { moreStep.set(SEARCH_ORDER.OWNER, SearchOrderRecordMapperTest.OWNER) }
        verify { moreStep.set(SEARCH_ORDER.GLOBAL, SearchOrderRecordMapperTest.GLOBAL) }
    }

    override fun verifySettingAuditFields() {
        verify { moreStep.set(SEARCH_ORDER.CREATED_BY, RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(SEARCH_ORDER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) }
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        every { entity.id } returns null
        setter.considerSettingKeyOf(moreStep, entity)
        verify { entity.id }
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        every { entity.id } returns SearchOrderRecordMapperTest.ID
        every { moreStep.set(SEARCH_ORDER.ID, SearchOrderRecordMapperTest.ID) } returns moreStep

        setter.considerSettingKeyOf(moreStep, entity)

        verify { entity.id }
        verify { moreStep.set(SEARCH_ORDER.ID, SearchOrderRecordMapperTest.ID) }
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entity, null)
        verify(exactly = 0) { entity.id = any() }
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        every { recordMock.id } returns 3L
        every { moreStep.set(SEARCH_ORDER.ID, SearchOrderRecordMapperTest.ID) } returns moreStep
        setter.resetIdToEntity(entity, recordMock)
        verify { recordMock.id }
        verify { entity.id = any() }
    }
}
