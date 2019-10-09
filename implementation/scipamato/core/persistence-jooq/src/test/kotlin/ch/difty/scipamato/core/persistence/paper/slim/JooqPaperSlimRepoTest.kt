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
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.TableField
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class JooqPaperSlimRepoTest :
    JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.scipamato.core.db.tables.Paper,
        PaperSlimRecordMapper, PaperFilter>() {

    override val unpersistedEntity = mock<PaperSlim>()
    override val persistedEntity = mock<PaperSlim>()
    override val unpersistedRecord = mock<PaperRecord>()
    override val persistedRecord = mock<PaperRecord>()
    override val mapper = mock<PaperSlimRecordMapper>()
    override val filter = mock<PaperFilter>()
    private val searchOrderRepositoryMock = mock<PaperSlimBackedSearchOrderRepository>()
    private val searchOrderMock = mock<SearchOrder>()
    private val paperSlimMock = mock<PaperSlim>()
    private val pageableMock = mock<PaginationContext>()
    private val dateTimeServiceMock = mock<DateTimeService>()

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
            override fun findById(id: Long?) = entity
        }

    override fun expectEntityIdsWithValues() {
        whenever(unpersistedEntity.id).thenReturn(SAMPLE_ID)
        whenever(persistedRecord.id).thenReturn(SAMPLE_ID)
    }

    override fun expectUnpersistedEntityIdNull() {
        whenever(unpersistedEntity.id).thenReturn(null)
    }

    override fun verifyUnpersistedEntityId() {
        verify<PaperSlim>(unpersistedEntity).id
    }

    override fun verifyPersistedRecordId() {
        verify<PaperRecord>(persistedRecord).id
    }

    @Test
    fun gettingTableId() {
        assertThat(repo.tableId).isEqualTo(tableId)
    }

    @Test
    fun findingBySearchOrder_delegatesToSearchOrderFinder() {
        whenever(searchOrderRepositoryMock.findBySearchOrder(searchOrderMock)).thenReturn(paperSlims)
        assertThat(repo.findBySearchOrder(searchOrderMock)).containsExactly(paperSlimMock, paperSlimMock)
        verify(searchOrderRepositoryMock).findBySearchOrder(searchOrderMock)
    }

    @Test
    fun countingBySearchOrder_delegatesToSearchOrderFinder() {
        whenever(searchOrderRepositoryMock.countBySearchOrder(searchOrderMock)).thenReturn(2)
        assertThat(repo.countBySearchOrder(searchOrderMock)).isEqualTo(2)
        verify(searchOrderRepositoryMock).countBySearchOrder(searchOrderMock)
    }

    @Test
    fun findingPageBySearchOrder_delegatesToSearchOrderFinder() {
        whenever(searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, pageableMock)).thenReturn(paperSlims)
        assertThat(repo.findPageBySearchOrder(searchOrderMock, pageableMock)).containsExactly(paperSlimMock,
            paperSlimMock)
        verify(searchOrderRepositoryMock).findPageBySearchOrder(searchOrderMock, pageableMock)
    }

    @Test
    fun gettingVersion_returnsPapersVersion() {
        assertThat(repo.recordVersion).isEqualTo(PAPER.VERSION)
    }

    companion object {
        private const val SAMPLE_ID = 3L
    }
}
