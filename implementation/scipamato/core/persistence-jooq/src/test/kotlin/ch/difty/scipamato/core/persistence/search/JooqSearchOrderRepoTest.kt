package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.search.*
import ch.difty.scipamato.core.persistence.EntityRepository
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

internal class JooqSearchOrderRepoTest :
    JooqEntityRepoTest<SearchOrderRecord, SearchOrder, Long, ch.difty.scipamato.core.db.tables.SearchOrder,
        SearchOrderRecordMapper, SearchOrderFilter>() {

    override val unpersistedEntity = mock<SearchOrder>()
    override val persistedEntity = mock<SearchOrder>()
    override val persistedRecord = mock<SearchOrderRecord>()
    override val unpersistedRecord = mock<SearchOrderRecord>()
    override val mapper = mock<SearchOrderRecordMapper>()
    override val filter = mock<SearchOrderFilter>()

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

    override fun makeRepoFindingEntityById(entity: SearchOrder):
        EntityRepository<SearchOrder, Long, SearchOrderFilter> = object : JooqSearchOrderRepo(
        dsl,
        mapper,
        sortMapper,
        filterConditionMapper,
        dateTimeService,
        insertSetStepSetter,
        updateSetStepSetter,
        applicationProperties
    ) {
        override fun findById(id: Long?, version: Int): SearchOrder = entity
    }

    override fun makeRepoSavingReturning(returning: SearchOrderRecord):
        EntityRepository<SearchOrder, Long, SearchOrderFilter> = object : JooqSearchOrderRepo(
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
        whenever(unpersistedEntity.id).thenReturn(SAMPLE_ID)
        whenever(persistedRecord.id).thenReturn(SAMPLE_ID)
    }

    override fun expectUnpersistedEntityIdNull() {
        whenever(unpersistedEntity.id).thenReturn(null)
    }

    override fun verifyUnpersistedEntityId() {
        verify<SearchOrder>(unpersistedEntity).id
    }

    override fun verifyPersistedRecordId() {
        verify<SearchOrderRecord>(persistedRecord).id
    }

    @Test
    fun enrichingAssociatedEntities_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, LC)
    }

    @Test
    fun enrichingAssociatedEntities_withEntityWithNullId_doesNothing() {
        val so = SearchOrder()
        assertThat(so.id == null).isTrue()
        repo.enrichAssociatedEntitiesOf(so, LC)
        assertThat(so.searchConditions).isEmpty()
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

            override fun fetchSearchConditionWithId(scId: Long?): SearchCondition = SearchCondition(scId)
            override fun findConditionIdsWithSearchTerms(searchOrderId: Long?): List<Long> = ArrayList()
            override fun findConditionsOf(searchOrderId: Long?): List<SearchCondition> = ArrayList()
        }

    @Test
    fun enrichingAssociatedEntities_withEntityId_fillsTheSearchConditionsAndTerms() {
        val repoSpy = makeRepoFindingNestedEntities()
        val so = SearchOrder()
        so.id = SAMPLE_ID
        assertThat(so.searchConditions).isEmpty()

        repoSpy.enrichAssociatedEntitiesOf(so, LC)

        assertThat(so.searchConditions).hasSize(3)

        val so1 = so
            .searchConditions[0]
        assertThat(so1.authors).isEqualTo("joss")
        assertThat(so1.publicationYear).isEqualTo("2014")
        assertThat(so1.displayValue).isEqualTo("joss AND 2014 AND 1F")

        val so2 = so.searchConditions[1]
        assertThat(so2.publicationYear).isEqualTo("2014-2016")
        assertThat(so2.displayValue).isEqualTo("2014-2016 AND 1F")

        val so3 = so.searchConditions[2]
        assertThat(so3.createdBy).isEqualTo("mkj")
        assertThat(so3.displayValue).isEqualTo("mkj AND 1F")
    }

    @Test
    fun enrichingAssociatedEntities_withEntityId_fillsTheExcludedPaperIds() {
        val repoSpy = makeRepoFindingNestedEntities()
        val so = SearchOrder()
        so.id = SAMPLE_ID
        assertThat(so.excludedPaperIds).isEmpty()
        repoSpy.enrichAssociatedEntitiesOf(so, LC)
        assertThat(so.excludedPaperIds).hasSize(3).containsExactly(17L, 33L, 42L)
    }

    @Test
    fun hasDirtyNewsletterFields_withTwoEmptySearchConditions_isNotDirty() {
        assertThat(sc1.newsletterTopicId == null).isTrue()
        assertThat(sc2.newsletterTopicId == null).isTrue()
        assertThat(sc1.newsletterHeadline == null).isTrue()
        assertThat(sc2.newsletterHeadline == null).isTrue()
        assertThat(sc1.newsletterIssue == null).isTrue()
        assertThat(sc2.newsletterIssue == null).isTrue()

        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse()
    }

    @Test
    fun hasDirtyNewsletterFields_withSingleNewsletterTopic_isDirty() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "1"))
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withDifferentNewsletterTopic_isDirty() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "1"))
        sc2.setNewsletterTopic(NewsletterTopic(2, "2"))
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withIdenticalNewsletterTopicIds_isNotDirty() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "foo"))
        sc2.setNewsletterTopic(NewsletterTopic(1, "bar"))
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse()
    }

    @Test
    fun hasDirtyNewsletterFields_withSingleNewsletterHeadline_isDirty() {
        sc1.newsletterHeadline = "foo"
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withDifferentNewsletterHeadlines_isDirty() {
        sc1.newsletterHeadline = "foo"
        sc2.newsletterHeadline = "bar"
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withIdenticalNewsletterHeadlines_isNotDirty() {
        sc1.newsletterHeadline = "foo"
        sc2.newsletterHeadline = "foo"
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse()
    }

    @Test
    fun hasDirtyNewsletterFields_withSingleNewsletterIssue_isDirty() {
        sc1.newsletterIssue = "foo"
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withDifferentNewsletterIssue_isDirty() {
        sc2.newsletterIssue = "bar"
        sc1.newsletterIssue = "foo"
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue()
    }

    @Test
    fun hasDirtyNewsletterFields_withIdenticalNewsletterIssue_isNotDirty() {
        sc1.newsletterIssue = "foo"
        sc2.newsletterIssue = "foo"
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse()
    }

    @Test
    fun addingSearchCondition_nonDirty_returnsPersistedEquivalentSearchCondition() {
        val equivalentPersistedSearchCondition = mock(SearchCondition::class.java)
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
        }
        assertThat(repo.addSearchCondition(SearchCondition(), 1, "en")).isEqualTo(equivalentPersistedSearchCondition)
    }

    @Test
    fun findingTermLessConditions() {
        val idToSc = HashMap<Long, SearchCondition>()

        // sc without id - should be filtered out
        val sc1 = SearchCondition()
        assertThat(sc1.searchConditionId == null).isTrue()
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

        assertThat(repo.findTermLessConditions(idToSc, conditionIdsWithSearchTerms)).containsExactly(sc3)
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
                updateCalled[0] = updateCalled[0] + searchCondition.searchConditionId
                return null
            }
        }
        repo.storeExistingConditionsOf(so, "de")

        assertThat(updateCalled[0]).isEqualTo(30L)
    }

    companion object {
        private const val SAMPLE_ID = 3L
        private const val LC = "de"
    }
}
