package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class JooqSearchOrderRepoTest :
    JooqEntityRepoTest<SearchOrderRecord, SearchOrder, Long, ch.difty.scipamato.core.db.tables.SearchOrder,
        SearchOrderRecordMapper, SearchOrderFilter>() {

    override val unpersistedEntity = mockk<SearchOrder>()
    override val persistedEntity = mockk<SearchOrder>()
    override val persistedRecord = mockk<SearchOrderRecord>()
    override val unpersistedRecord = mockk<SearchOrderRecord>()
    override val mapper = mockk<SearchOrderRecordMapper>()
    override val filter = mockk<SearchOrderFilter>()

    private val sc1 = SearchCondition()
    private val sc2 = SearchCondition()

    override val sampleId = SAMPLE_ID

    override val table = SEARCH_ORDER!!

    override val tableId = SEARCH_ORDER.ID!!

    override val recordVersion = SEARCH_ORDER.VERSION!!

    override val repo = JooqSearchOrderRepo(
        dsl,
        mapper,
        sortMapper,
        filterConditionMapper,
        dateTimeService,
        insertSetStepSetter,
        updateSetStepSetter,
        applicationProperties
    )

    override fun makeRepoFindingEntityById(
        entity: SearchOrder
    ): EntityRepository<SearchOrder, Long, SearchOrderFilter> =
        object : JooqSearchOrderRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun findById(id: Long, version: Int): SearchOrder = entity
        }

    override fun makeRepoSavingReturning(
        returning: SearchOrderRecord
    ): EntityRepository<SearchOrder, Long, SearchOrderFilter> =
        object : JooqSearchOrderRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun doSave(entity: SearchOrder, languageCode: String): SearchOrderRecord = returning
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
    fun enrichingAssociatedEntities_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, LC)
    }

    @Test
    fun enrichingAssociatedEntities_withEntityWithNullId_doesNothing() {
        val so = SearchOrder()
        so.id.shouldBeNull()
        repo.enrichAssociatedEntitiesOf(so, LC)
        so.searchConditions.shouldBeEmpty()
    }

    private fun makeRepoFindingNestedEntities(): JooqSearchOrderRepo =
        object : JooqSearchOrderRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {

            val st1 = SearchTerm.newSearchTerm(
                1,
                SearchTermType.STRING.id,
                3,
                Paper.PaperFields.AUTHORS.fieldName,
                "joss"
            )
            val st2 = SearchTerm.newSearchTerm(
                2,
                SearchTermType.INTEGER.id,
                3,
                Paper.PaperFields.PUBL_YEAR.fieldName,
                "2014"
            )
            val st3 = SearchTerm.newSearchTerm(
                3,
                SearchTermType.INTEGER.id,
                4,
                Paper.PaperFields.PUBL_YEAR.fieldName,
                "2014-2016"
            )
            val st4 = SearchTerm.newSearchTerm(
                4,
                SearchTermType.AUDIT.id,
                5,
                Paper.PaperFields.CREATED_BY.fieldName,
                "mkj"
            )

            override fun fetchSearchTermsForSearchOrderWithId(searchOrderId: Long): List<SearchTerm> {
                return if (searchOrderId == SAMPLE_ID) {
                    listOf(st1, st2, st3, st4)
                } else {
                    ArrayList()
                }
            }

            override fun fetchExcludedPaperIdsForSearchOrderWithId(searchOrderId: Long): List<Long> {
                return if (searchOrderId == SAMPLE_ID) {
                    listOf(17L, 33L, 42L)
                } else {
                    ArrayList()
                }
            }

            override fun fetchCodesForSearchConditionWithId(
                searchCondition: SearchCondition,
                languageCode: String
            ): List<Code> =
                listOf(Code("1F", "Code 1F", "", false, 1, "CC 1", "", 0))

            override fun fetchSearchConditionWithId(scId: Long): SearchCondition? = SearchCondition(scId)
            override fun findConditionIdsWithSearchTerms(searchOrderId: Long): List<Long> = ArrayList()
            override fun findConditionsOf(searchOrderId: Long): List<SearchCondition> = ArrayList()
        }

    @Test
    fun enrichingAssociatedEntities_withEntityId_fillsTheSearchConditionsAndTerms() {
        val repoSpy = makeRepoFindingNestedEntities()
        val so = SearchOrder()
        so.id = SAMPLE_ID
        so.searchConditions.shouldBeEmpty()

        repoSpy.enrichAssociatedEntitiesOf(so, LC)

        so.searchConditions shouldHaveSize 3

        val so1 = so
            .searchConditions[0]
        so1.authors shouldBeEqualTo "joss"
        so1.publicationYear shouldBeEqualTo "2014"
        so1.displayValue shouldBeEqualTo "joss AND 2014 AND 1F"

        val so2 = so.searchConditions[1]
        so2.publicationYear shouldBeEqualTo "2014-2016"
        so2.displayValue shouldBeEqualTo "2014-2016 AND 1F"

        val so3 = so.searchConditions[2]
        so3.createdBy shouldBeEqualTo "mkj"
        so3.displayValue shouldBeEqualTo "mkj AND 1F"
    }

    @Test
    fun enrichingAssociatedEntities_withEntityId_fillsTheExcludedPaperIds() {
        val repoSpy = makeRepoFindingNestedEntities()
        val so = SearchOrder()
        so.id = SAMPLE_ID
        so.excludedPaperIds.shouldBeEmpty()
        repoSpy.enrichAssociatedEntitiesOf(so, LC)
        so.excludedPaperIds shouldHaveSize 3
    }

    @Test
    fun hasDirtyNewsletterFields_withTwoEmptySearchConditions_isNotDirty() {
        sc1.newsletterTopicId.shouldBeNull()
        sc2.newsletterTopicId.shouldBeNull()
        sc1.newsletterHeadline.shouldBeNull()
        sc2.newsletterHeadline.shouldBeNull()
        sc1.newsletterIssue.shouldBeNull()
        sc2.newsletterIssue.shouldBeNull()

        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeFalse()
    }

    @Test
    fun hasDirtyNewsletterFields_withSingleNewsletterTopic_isDirty() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "1"))
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withDifferentNewsletterTopic_isDirty() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "1"))
        sc2.setNewsletterTopic(NewsletterTopic(2, "2"))
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withIdenticalNewsletterTopicIds_isNotDirty() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "foo"))
        sc2.setNewsletterTopic(NewsletterTopic(1, "bar"))
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeFalse()
    }

    @Test
    fun hasDirtyNewsletterFields_withSingleNewsletterHeadline_isDirty() {
        sc1.newsletterHeadline = "foo"
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withDifferentNewsletterHeadlines_isDirty() {
        sc1.newsletterHeadline = "foo"
        sc2.newsletterHeadline = "bar"
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withIdenticalNewsletterHeadlines_isNotDirty() {
        sc1.newsletterHeadline = "foo"
        sc2.newsletterHeadline = "foo"
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeFalse()
    }

    @Test
    fun hasDirtyNewsletterFields_withSingleNewsletterIssue_isDirty() {
        sc1.newsletterIssue = "foo"
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withDifferentNewsletterIssue_isDirty() {
        sc2.newsletterIssue = "bar"
        sc1.newsletterIssue = "foo"
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withIdenticalNewsletterIssue_isNotDirty() {
        sc1.newsletterIssue = "foo"
        sc2.newsletterIssue = "foo"
        repo.hasDirtyNewsletterFields(sc1, sc2).shouldBeFalse()
    }

    @Test
    fun addingSearchCondition_nonDirty_returnsPersistedEquivalentSearchCondition() {
        val equivalentPersistedSearchCondition = mockk<SearchCondition>(relaxed = true)
        val repo = object : JooqSearchOrderRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun findEquivalentPersisted(
                searchCondition: SearchCondition,
                searchOrderId: Long,
                languageCode: String
            ): java.util.Optional<SearchCondition> = java.util.Optional.of(equivalentPersistedSearchCondition)

            override fun hasDirtyNewsletterFields(
                searchCondition: SearchCondition,
                psc: SearchCondition
            ): Boolean = false

            override fun hasDirtyAttachmentFields(
                searchCondition: SearchCondition,
                psc: SearchCondition
            ): Boolean = false
        }
        repo.addSearchCondition(SearchCondition(), 1, "en") shouldBeEqualTo equivalentPersistedSearchCondition
    }

    @Test
    fun findingTermLessConditions() {
        val idToSc = HashMap<Long?, SearchCondition>()

        // sc without id - should be filtered out
        val sc1 = SearchCondition()
        sc1.searchConditionId.shouldBeNull()
        idToSc[1L] = sc1

        // sc with id - which is also contained in the conditionId list - should be filtered out
        val sc2 = SearchCondition()
        sc2.searchConditionId = 2L
        idToSc[sc2.searchConditionId] = sc2

        // sc with id -> should be returned
        val sc3 = SearchCondition()
        sc3.searchConditionId = 3L
        idToSc[sc3.searchConditionId] = sc3

        val conditionIdsWithSearchTerms = listOf(sc2.searchConditionId)

        repo.findTermLessConditions(idToSc, conditionIdsWithSearchTerms) shouldContainAll listOf(sc3)
    }

    @Test
    fun storingExistingConditionsOf_withSearchConditionsWithIds_callsUpdateSearchConditionForEach() {
        val so = SearchOrder()
        so.id = 1L
        val sc1 = SearchCondition(10L)

        val sc2 = SearchCondition(20L)
        so.add(sc1)
        so.add(sc2)

        val updateCalled = longArrayOf(0)
        val repo = object : JooqSearchOrderRepo(
            dsl,
            mapper,
            sortMapper,
            filterConditionMapper,
            dateTimeService,
            insertSetStepSetter,
            updateSetStepSetter,
            applicationProperties
        ) {
            override fun updateSearchCondition(
                searchCondition: SearchCondition,
                searchOrderId: Long,
                languageCode: String
            ): SearchCondition? {
                updateCalled[0] = updateCalled[0] + (searchCondition.searchConditionId ?: 0)
                return null
            }
        }
        repo.storeExistingConditionsOf(so, "de")

        updateCalled[0] shouldBeEqualTo 30L
    }

    companion object {
        private const val SAMPLE_ID = 3L
        private const val LC = "de"
    }
}
