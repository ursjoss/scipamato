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
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.jooq.DeleteConditionStep
import org.jooq.Record1
import org.jooq.SortField
import org.jooq.TableField
import org.junit.jupiter.api.Test

internal class JooqPaperRepoTest :
    JooqEntityRepoTest<PaperRecord, Paper, Long, ch.difty.scipamato.core.db.tables.Paper,
        PaperRecordMapper, PaperFilter>() {

    override val unpersistedEntity = mockk<Paper>()
    override val persistedEntity = mockk<Paper>()
    override val persistedRecord = mockk<PaperRecord>()
    override val unpersistedRecord = mockk<PaperRecord>()
    override val mapper = mockk<PaperRecordMapper>()
    override val filter = mockk<PaperFilter>()
    private val searchOrderRepositoryMock = mockk<PaperBackedSearchOrderRepository>()
    private val searchOrderMock = mockk<SearchOrder>()
    private val paperMock = mockk<Paper>(relaxed = true)
    private val paginationContextMock = mockk<PaginationContext>()
    private val deleteConditionStepMock = mockk<DeleteConditionStep<PaperRecord>>(relaxed = true)
    private val paperAttachmentMock = mockk<PaperAttachment>()

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
        every { unpersistedEntity.id } returns SAMPLE_ID
        every { unpersistedEntity.version } returns 0
        every { persistedRecord.id } returns SAMPLE_ID
        every { persistedRecord.version } returns 1
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
    fun gettingRecordVersion() {
        repo.recordVersion shouldBeEqualTo PAPER.VERSION
    }

    @Test
    fun gettingIdFromPaper() {
        every { paperMock.id } returns 17L
        repo.getIdFrom(paperMock) shouldBeEqualTo 17L
        verify { paperMock.id }
    }

    @Test
    fun gettingIdFromPaperRecord() {
        every { persistedRecord.id } returns 17L
        repo.getIdFrom(persistedRecord) shouldBeEqualTo 17L
        verify { persistedRecord.id }
    }

    @Test
    fun findingBySearchOrder_delegatesToSearchOrderFinder() {
        every { searchOrderRepositoryMock.findBySearchOrder(searchOrderMock) } returns papers
        makeRepoStubbingEnriching().findBySearchOrder(searchOrderMock, LC) shouldContainSame listOf(paperMock,
            paperMock)
        enrichedEntities shouldContainAll listOf(paperMock, paperMock)
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
        every { searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, paginationContextMock) } returns papers
        makeRepoStubbingEnriching().findPageBySearchOrder(searchOrderMock, paginationContextMock,
            LC) shouldContainSame listOf(paperMock, paperMock)
        enrichedEntities shouldContainAll listOf(paperMock, paperMock)
        verify { searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, paginationContextMock) }
    }

    @Test
    fun gettingPapersByPmIds_withNoPmIds_returnsEmptyList() {
        repo.findByPmIds(ArrayList(), LC).shouldBeEmpty()
    }

    @Test
    fun findingByNumbers_withNoNumbers_returnsEmptyList() {
        repo.findByNumbers(ArrayList(), LC).shouldBeEmpty()
    }

    @Test
    fun findingExistingPmIdsOutOf_withNoPmIds_returnsEmptyList() {
        repo.findExistingPmIdsOutOf(ArrayList()).shouldBeEmpty()
    }

    @Test
    fun findingPageByFilter() {
        val sortFields = ArrayList<SortField<Paper>>()
        val sort = Sort(Direction.DESC, "id")
        every { paginationContextMock.sort } returns sort
        every { paginationContextMock.pageSize } returns 20
        every { paginationContextMock.offset } returns 0
        every { filterConditionMapper.map(filter) } returns conditionMock
        every { sortMapper.map(sort, table) } returns sortFields

        every { dsl.selectFrom(table) } returns mockk() {
            every { where(conditionMock) } returns mockk() {
                every { orderBy(sortFields) } returns mockk() {
                    every { limit(20) } returns mockk() {
                        every { offset(0) } returns mockk() {
                            // don't want to go into the enrichment test fixture, thus returning empty list
                            every { fetch(mapper) } returns emptyList()
                        }
                    }
                }
            }
        }

        val papers = repo.findPageByFilter(filter, paginationContextMock, LC)
        papers.shouldBeEmpty()

        verify { filterConditionMapper.map(filter) }
        verify { paginationContextMock.sort }
        verify { paginationContextMock.pageSize }
        verify { paginationContextMock.offset }
        verify { sortMapper.map(sort, table) }

        verify { dsl.selectFrom(table) }
    }

    @Test
    fun findingPageByFilter_withNoExplicitLanguageCode() {
        every { applicationProperties.defaultLocalization } returns LC

        val sortFields = ArrayList<SortField<Paper>>()
        val sort = Sort(Direction.DESC, "id")
        every { paginationContextMock.sort } returns sort
        every { paginationContextMock.pageSize } returns 20
        every { paginationContextMock.offset } returns 0
        every { filterConditionMapper.map(filter) } returns conditionMock
        every { sortMapper.map(sort, table) } returns sortFields

        every { dsl.selectFrom(table) } returns mockk() {
            every { where(conditionMock) } returns mockk() {
                every { orderBy(sortFields) } returns mockk() {
                    every { limit(20) } returns mockk() {
                        every { offset(0) } returns mockk() {
                            // don't want to go into the enrichment test fixture, thus returning empty list
                            every { fetch(mapper) } returns emptyList()
                        }
                    }
                }
            }
        }


        val papers = repo.findPageByFilter(filter, paginationContextMock)
        papers.shouldBeEmpty()

        verify { applicationProperties.defaultLocalization }
        verify { filterConditionMapper.map(filter) }
        verify { paginationContextMock.sort }
        verify { paginationContextMock.pageSize }
        verify { paginationContextMock.offset }
        verify { sortMapper.map(sort, table) }

        verify { dsl.selectFrom(table) }
    }

    @Test
    fun findingPageOfIdsBySearchOrder() {
        every {
            searchOrderRepositoryMock.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)
        } returns listOf(17L, 3L, 5L)
        repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock) shouldContainAll listOf(17L, 3L, 5L)
        verify { searchOrderRepositoryMock.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock) }
    }

    @Test
    fun deletingIds() {
        val ids = listOf(3L, 5L, 7L)
        every { dsl.deleteFrom(table) } returns deleteWhereStepMock
        every { deleteWhereStepMock.where(PAPER.ID.`in`(ids)) } returns deleteConditionStepMock

        repo.delete(ids)

        verify { dsl.deleteFrom(table) }
        verify { deleteWhereStepMock.where(PAPER.ID.`in`(ids)) }
        verify { deleteConditionStepMock.execute() }
    }

    @Test
    fun enrichingAssociatedEntitiesOf_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, "de")
    }

    @Test
    fun enrichingAssociatedEntitiesOf_withNullLanguageCode_withNullPaperId_doesNotCallRepo() {
        every { paperMock.id } returns null
        val repo = makeRepoStubbingAttachmentEnriching()
        repo.enrichAssociatedEntitiesOf(paperMock, null)
        verify { paperMock.id }
        verify(exactly = 0) { paperMock.attachments = any() }
    }

    @Test
    fun enrichingAssociatedEntitiesOf_withNullLanguageCode_withPaperWithId_enrichesAttachments() {
        every { paperMock.id } returns 17L
        val repo = makeRepoStubbingAttachmentEnriching()
        repo.enrichAssociatedEntitiesOf(paperMock, null)
        verify { paperMock.id }
        verify { paperMock.attachments = listOf(paperAttachmentMock) }
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
        repo.evaluateNumbers(null).isPresent.shouldBeFalse()
    }

    @Test
    fun evaluatingNumbers_withRecordWithNullValue1_returnsEmpty() {
        val numbers: Record1<Array<Long>> = mockk {
            every { value1() } returns null
        }
        repo.evaluateNumbers(numbers).isPresent.shouldBeFalse()
    }

    @Test
    fun evaluatingNumbers_withRecordWithEmptyValue1_returnsEmpty() {
        val numbers: Record1<Array<Long>> = mockk {
            every { value1() } returns arrayOf()
        }
        repo.evaluateNumbers(numbers).isPresent.shouldBeFalse()
    }

    companion object {
        private const val SAMPLE_ID = 3L
        private const val LC = "de"
    }
}
