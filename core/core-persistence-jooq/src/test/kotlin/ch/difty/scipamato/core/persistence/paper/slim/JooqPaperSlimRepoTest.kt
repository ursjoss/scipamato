package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.JooqReadOnlyRepoTest
import ch.difty.scipamato.core.persistence.ReadOnlyRepository
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.jooq.TableField
import org.junit.jupiter.api.Test

internal class JooqPaperSlimRepoTest :
    JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.scipamato.core.db.tables.Paper,
        PaperSlimRecordMapper, PaperFilter>() {

    override val unpersistedEntity = mockk<PaperSlim>()
    override val persistedEntity = mockk<PaperSlim>()
    override val unpersistedRecord = mockk<PaperRecord>()
    override val persistedRecord = mockk<PaperRecord>()
    override val mapper = mockk<PaperSlimRecordMapper>()
    override val filter = mockk<PaperFilter>()
    private val searchOrderRepositoryMock = mockk<PaperSlimBackedSearchOrderRepository>()
    private val searchOrderMock = mockk<SearchOrder>()
    private val paperSlimMock = mockk<PaperSlim>()
    private val pageableMock = mockk<PaginationContext>()
    private val dateTimeServiceMock = mockk<DateTimeService>()

    override val repo = JooqPaperSlimRepo(
        dsl,
        mapper,
        sortMapper,
        filterConditionMapper,
        searchOrderRepositoryMock,
        dateTimeServiceMock,
        applicationProperties
    )

    private val paperSlims = listOf(paperSlimMock, paperSlimMock)

    override val table: ch.difty.scipamato.core.db.tables.Paper = PAPER

    override val tableId: TableField<PaperRecord, Long> = PAPER.ID

    override fun makeRepoFindingEntityById(entity: PaperSlim): ReadOnlyRepository<PaperSlim, Long, PaperFilter> =
        object : JooqPaperSlimRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            searchOrderRepositoryMock,
            dateTimeServiceMock,
            applicationProperties
        ) {
            override fun findById(id: Long) = entity
        }

    override fun expectEntityIdsWithValues() {
        every { unpersistedEntity.id } returns SAMPLE_ID
        every { persistedRecord.id } returns SAMPLE_ID
    }

    override fun expectUnpersistedEntityIdNull() {
        every { unpersistedEntity.id } returns null
    }

    override fun verifyUnpersistedEntityId() {
        verify { unpersistedEntity.id }
    }

    override fun verifyPersistedRecordId() {
        verify { persistedRecord.id }
    }

    @Test
    fun gettingTableId() {
        repo.tableId shouldBeEqualTo tableId
    }

    @Test
    fun findingBySearchOrder_delegatesToSearchOrderFinder() {
        every { searchOrderRepositoryMock.findBySearchOrder(searchOrderMock) } returns paperSlims
        repo.findBySearchOrder(searchOrderMock) shouldContainAll listOf(paperSlimMock, paperSlimMock)
        verify { searchOrderRepositoryMock.findBySearchOrder(searchOrderMock) }
    }

    @Test
    fun countingBySearchOrder_delegatesToSearchOrderFinder() {
        every { searchOrderRepositoryMock.countBySearchOrder(searchOrderMock) } returns 2
        repo.countBySearchOrder(searchOrderMock) shouldBeEqualTo 2
        verify { searchOrderRepositoryMock.countBySearchOrder(searchOrderMock) }
    }

    @Test
    fun findingPageBySearchOrder_delegatesToSearchOrderFinder() {
        every { searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, pageableMock) } returns paperSlims
        repo.findPageBySearchOrder(searchOrderMock, pageableMock) shouldContainSame listOf(paperSlimMock,
            paperSlimMock)
        verify { searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, pageableMock) }
    }

    @Test
    fun gettingVersion_returnsPapersVersion() {
        repo.recordVersion shouldBeEqualTo PAPER.VERSION
    }

    companion object {
        private const val SAMPLE_ID = 3L
    }
}
