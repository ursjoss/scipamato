package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperAttachment
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperBackedSearchOrderRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

internal class JooqPaperRepoTest :
    JooqEntityRepoTest<PaperRecord, Paper, Long, ch.difty.scipamato.core.db.tables.Paper,
        PaperRecordMapper, PaperFilter>() {

    override val unpersistedEntity = mock<Paper>()
    override val persistedEntity = mock<Paper>()
    override val persistedRecord = mock<PaperRecord>()
    override val unpersistedRecord = mock<PaperRecord>()
    override val mapper = mock<PaperRecordMapper>()
    override val filter = mock<PaperFilter>()
    private val searchOrderRepositoryMock = mock<PaperBackedSearchOrderRepository>()
    private val searchOrderMock = mock<SearchOrder>()
    private val paperMock = mock<Paper>()
    private val paginationContextMock = mock<PaginationContext>()
    private val deleteConditionStepMock = mock<DeleteConditionStep<PaperRecord>>()
    private val paperAttachmentMock = mock<PaperAttachment>()

    private val papers = listOf(paperMock, paperMock)

    private val enrichedEntities = ArrayList<Paper?>()

    override val sampleId: Long = SAMPLE_ID

    override val table: ch.difty.scipamato.core.db.tables.Paper = PAPER

    override val tableId: TableField<PaperRecord, Long> = PAPER.ID

    override val recordVersion: TableField<PaperRecord, Int> = PAPER.VERSION

    override val repo = JooqPaperRepo(dsl, mapper, sortMapper, filterConditionMapper,
        dateTimeService, insertSetStepSetter, updateSetStepSetter, searchOrderRepositoryMock,
        applicationProperties)

    override fun makeRepoSavingReturning(returning: PaperRecord): EntityRepository<Paper, Long, PaperFilter> =
        object : JooqPaperRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            searchOrderRepositoryMock,
            applicationProperties
        ) {
            override fun doSave(entity: Paper, languageCode: String): PaperRecord = returning
        }

    override fun makeRepoFindingEntityById(entity: Paper): EntityRepository<Paper, Long, PaperFilter> =
        object : JooqPaperRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            searchOrderRepositoryMock,
            applicationProperties
        ) {
            override fun findById(id: Long, version: Int): Paper = entity
        }

    private fun makeRepoStubbingEnriching(): PaperRepository =
        object : JooqPaperRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            searchOrderRepositoryMock,
            applicationProperties
        ) {
            override fun enrichAssociatedEntitiesOf(entity: Paper?, language: String?) {
                enrichedEntities.add(entity)
            }
        }

    override fun expectEntityIdsWithValues() {
        whenever(unpersistedEntity.id).thenReturn(SAMPLE_ID)
        whenever(unpersistedEntity.version).thenReturn(0)
        whenever(persistedRecord.id).thenReturn(SAMPLE_ID)
        whenever(persistedRecord.version).thenReturn(1)
    }

    override fun expectUnpersistedEntityIdNull() {
        whenever(unpersistedEntity.id).thenReturn(null)
    }

    override fun verifyUnpersistedEntityId() {
        verify<Paper>(unpersistedEntity).id
    }

    override fun verifyPersistedRecordId() {
        verify<PaperRecord>(persistedRecord).id
    }

    @Test
    fun gettingTableId() {
        assertThat(repo.tableId).isEqualTo(tableId)
    }

    @Test
    fun gettingRecordVersion() {
        assertThat(repo.recordVersion).isEqualTo(PAPER.VERSION)
    }

    @Test
    fun gettingIdFromPaper() {
        whenever(paperMock.id).thenReturn(17L)
        assertThat(repo.getIdFrom(paperMock)).isEqualTo(17L)
        verify(paperMock).id
    }

    @Test
    fun gettingIdFromPaperRecord() {
        whenever(persistedRecord.id).thenReturn(17L)
        assertThat(repo.getIdFrom(persistedRecord)).isEqualTo(17L)
        verify(persistedRecord).id
    }

    @Test
    fun findingBySearchOrder_delegatesToSearchOrderFinder() {
        whenever(searchOrderRepositoryMock.findBySearchOrder(searchOrderMock)).thenReturn(papers)
        assertThat(makeRepoStubbingEnriching().findBySearchOrder(searchOrderMock, LC)).containsExactly(paperMock,
            paperMock)
        assertThat(enrichedEntities).containsExactly(paperMock, paperMock)
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
        whenever(searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(
            papers)
        assertThat(makeRepoStubbingEnriching().findPageBySearchOrder(searchOrderMock, paginationContextMock,
            LC)).containsExactly(paperMock, paperMock)
        assertThat(enrichedEntities).containsExactly(paperMock, paperMock)
        verify(searchOrderRepositoryMock).findPageBySearchOrder(searchOrderMock, paginationContextMock)
    }

    @Test
    fun gettingPapersByPmIds_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(ArrayList(), LC)).isEmpty()
    }

    @Test
    fun findingByNumbers_withNoNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(ArrayList(), LC)).isEmpty()
    }

    @Test
    fun findingExistingPmIdsOutOf_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(ArrayList())).isEmpty()
    }

    @Test
    fun findingPageByFilter() {
        val sortFields = ArrayList<SortField<Paper>>()
        val sort = Sort(Direction.DESC, "id")
        whenever(paginationContextMock.sort).thenReturn(sort)
        whenever(paginationContextMock.pageSize).thenReturn(20)
        whenever(paginationContextMock.offset).thenReturn(0)
        whenever(filterConditionMapper.map(filter)).thenReturn(conditionMock)
        whenever(sortMapper.map(sort, table)).thenReturn(sortFields)

        val selectWhereStepMock: SelectWhereStep<PaperRecord> = mock()
        whenever(dsl.selectFrom(table)).thenReturn(selectWhereStepMock)
        val selectConditionStepMock: SelectConditionStep<PaperRecord> = mock()
        whenever(selectWhereStepMock.where(conditionMock)).thenReturn(selectConditionStepMock)
        val selectSeekStepNMock: SelectSeekStepN<PaperRecord> = mock()
        whenever(selectConditionStepMock.orderBy(sortFields)).thenReturn(selectSeekStepNMock)
        val selectLimitPercentStepMock: SelectLimitPercentStep<PaperRecord> = mock()
        whenever(selectSeekStepNMock.limit(20)).thenReturn(selectLimitPercentStepMock)
        val selectForUpdateStepMock: SelectForUpdateStep<PaperRecord> = mock()
        whenever(selectLimitPercentStepMock.offset(0)).thenReturn(selectForUpdateStepMock)
        // don't want to go into the enrichment test fixture, thus returning empty list
        whenever(selectForUpdateStepMock.fetch<Paper>(mapper)).thenReturn(emptyList())

        val papers = repo.findPageByFilter(filter, paginationContextMock, LC)
        assertThat(papers).isEmpty()

        verify(filterConditionMapper).map(filter)
        verify(paginationContextMock).sort
        verify(paginationContextMock).pageSize
        verify(paginationContextMock).offset
        verify(sortMapper).map(sort, table)

        verify(dsl).selectFrom(table)
        verify(selectWhereStepMock).where(conditionMock)
        verify(selectConditionStepMock).orderBy(sortFields)
        verify(selectSeekStepNMock).limit(20)
        verify(selectLimitPercentStepMock).offset(0)
        verify(selectForUpdateStepMock).fetch(mapper)
    }

    @Test
    fun findingPageByFilter_withNoExplicitLanguageCode() {
        whenever(applicationProperties.defaultLocalization).thenReturn(LC)

        val sortFields = ArrayList<SortField<Paper>>()
        val sort = Sort(Direction.DESC, "id")
        whenever(paginationContextMock.sort).thenReturn(sort)
        whenever(paginationContextMock.pageSize).thenReturn(20)
        whenever(paginationContextMock.offset).thenReturn(0)
        whenever(filterConditionMapper.map(filter)).thenReturn(conditionMock)
        whenever(sortMapper.map(sort, table)).thenReturn(sortFields)

        val selectWhereStepMock: SelectWhereStep<PaperRecord> = mock()
        whenever(dsl.selectFrom(table)).thenReturn(selectWhereStepMock)
        val selectConditionStepMock: SelectConditionStep<PaperRecord> = mock()
        whenever(selectWhereStepMock.where(conditionMock)).thenReturn(selectConditionStepMock)
        val selectSeekStepNMock: SelectSeekStepN<PaperRecord> = mock()
        whenever(selectConditionStepMock.orderBy(sortFields)).thenReturn(selectSeekStepNMock)
        val selectLimitPercentStepMock: SelectLimitPercentStep<PaperRecord> = mock()
        whenever(selectSeekStepNMock.limit(20)).thenReturn(selectLimitPercentStepMock)
        val selectForUpdateStepMock: SelectForUpdateStep<PaperRecord> = mock()
        whenever(selectLimitPercentStepMock.offset(0)).thenReturn(selectForUpdateStepMock)
        // don't want to go into the enrichment test fixture, thus returning empty list
        whenever(selectForUpdateStepMock.fetch<Paper>(mapper)).thenReturn(emptyList())

        val papers = repo.findPageByFilter(filter, paginationContextMock)
        assertThat(papers).isEmpty()

        verify(applicationProperties).defaultLocalization
        verify(filterConditionMapper).map(filter)
        verify(paginationContextMock).sort
        verify(paginationContextMock).pageSize
        verify(paginationContextMock).offset
        verify(sortMapper).map(sort, table)

        verify(dsl).selectFrom(table)
        verify<SelectWhereStep<PaperRecord>>(selectWhereStepMock).where(conditionMock)
        verify<SelectConditionStep<PaperRecord>>(selectConditionStepMock).orderBy(sortFields)
        verify<SelectSeekStepN<PaperRecord>>(selectSeekStepNMock).limit(20)
        verify<SelectLimitPercentStep<PaperRecord>>(selectLimitPercentStepMock).offset(0)
        verify<SelectForUpdateStep<PaperRecord>>(selectForUpdateStepMock).fetch(mapper)
    }

    @Test
    fun findingPageOfIdsBySearchOrder() {
        whenever(searchOrderRepositoryMock
            .findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(listOf(17L, 3L, 5L))
        assertThat(repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).containsExactly(17L, 3L, 5L)
        verify(searchOrderRepositoryMock).findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)
    }

    @Test
    fun deletingIds() {
        val ids = listOf(3L, 5L, 7L)
        whenever(dsl.deleteFrom(table)).thenReturn(deleteUsingStep)
        whenever(deleteUsingStep.where(PAPER.ID.`in`(ids))).thenReturn(deleteConditionStepMock)

        repo.delete(ids)

        verify(dsl).deleteFrom(table)
        verify(deleteUsingStep).where(PAPER.ID.`in`(ids))
        verify<DeleteConditionStep<PaperRecord>>(deleteConditionStepMock).execute()
    }

    @Test
    fun enrichingAssociatedEntitiesOf_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, "de")
    }

    @Test
    fun enrichingAssociatedEntitiesOf_withNullLanguageCode_withNullPaperId_doesNotCallRepo() {
        whenever(paperMock.id).thenReturn(null)
        val repo = makeRepoStubbingAttachmentEnriching()
        repo.enrichAssociatedEntitiesOf(paperMock, null)
        verify(paperMock).id
        verify(paperMock, never()).attachments = Mockito.anyList()
    }

    @Test
    fun enrichingAssociatedEntitiesOf_withNullLanguageCode_withPaperWithId_enrichesAttachments() {
        whenever(paperMock.id).thenReturn(17L)
        val repo = makeRepoStubbingAttachmentEnriching()
        repo.enrichAssociatedEntitiesOf(paperMock, null)
        verify(paperMock).id
        verify(paperMock).attachments = listOf(paperAttachmentMock)
    }

    private fun makeRepoStubbingAttachmentEnriching(): JooqPaperRepo =
        object : JooqPaperRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            searchOrderRepositoryMock,
            applicationProperties
        ) {
            override fun loadSlimAttachment(paperId: Long): List<PaperAttachment> = listOf(paperAttachmentMock)
        }

    @Test
    fun evaluatingNumbers_withNullRecord_returnsEmpty() {
        assertThat(repo.evaluateNumbers(null)).isEmpty
    }

    @Test
    fun evaluatingNumbers_withRecordWithNullValue1_returnsEmpty() {
        val numbers: Record1<Array<Long>> = mock()
        whenever(numbers.value1()).thenReturn(null)
        assertThat(repo.evaluateNumbers(numbers)).isEmpty
    }

    @Test
    fun evaluatingNumbers_withRecordWithEmptyValue1_returnsEmpty() {
        val numbers: Record1<Array<Long>> = mock()
        whenever(numbers.value1()).thenReturn(arrayOf())
        assertThat(repo.evaluateNumbers(numbers)).isEmpty
    }

    companion object {
        private const val SAMPLE_ID = 3L
        private const val LC = "de"
    }
}
