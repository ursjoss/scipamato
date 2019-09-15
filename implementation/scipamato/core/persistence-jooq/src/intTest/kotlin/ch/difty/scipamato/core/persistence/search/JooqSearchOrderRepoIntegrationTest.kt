@file:Suppress("FunctionName")

package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("DuplicatedCode", "SpellCheckingInspection", "SameParameterValue")
internal open class JooqSearchOrderRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqSearchOrderRepo

    @Test
    fun findingAll() {
        val searchOrders = repo.findAll()
        assertThat(searchOrders).hasSize(RECORD_COUNT_PREPOPULATED)
    }

    @Test
    @Suppress("ConstantConditionIf")
    fun findingById_withExistingId_returnsEntity() {
        val searchOrder = repo.findById(RECORD_COUNT_PREPOPULATED.toLong())
        if (MAX_ID_PREPOPULATED > 0)
            assertThat(searchOrder.id).isEqualTo(MAX_ID_PREPOPULATED)
        else
            assertThat(searchOrder == null).isTrue()
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1L) == null).isTrue()
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val so = makeMinimalSearchOrder()
        val searchCondition = SearchCondition()
        searchCondition.authors = "foo"
        so.add(searchCondition)
        so.addExclusionOfPaperWithId(4L)
        assertThat(so.id == null).isTrue()
        assertThat(so.searchConditions.first().id == null).isTrue()
        assertThat(so.searchConditions.first().stringSearchTerms.first().id == null).isTrue()

        val saved = repo.add(so)

        assertThat(saved.id).isGreaterThan(MAX_ID_PREPOPULATED)
        assertThat(saved.owner).isEqualTo(10)
        assertThat(saved.searchConditions.first().searchConditionId).isGreaterThan(5L)
        assertThat(saved.searchConditions.first().stringSearchTerms.first().id == null).isFalse()

        assertThat(saved.excludedPaperIds).containsOnly(4L)
    }

    private fun makeMinimalSearchOrder(): SearchOrder {
        return SearchOrder().apply {
            name = null
            owner = 10
            isGlobal = true
        }
    }

    @Test
    fun updatingRecord() {
        val searchOrder = repo.add(makeMinimalSearchOrder())
        assertThat(searchOrder == null).isFalse()
        assertThat(searchOrder.id).isGreaterThan(MAX_ID_PREPOPULATED)
        val id = searchOrder.id
        assertThat(searchOrder.owner).isEqualTo(10)
        assertThat(searchOrder.name == null).isTrue()

        searchOrder.owner = 20
        searchOrder.name = "soName"
        repo.update(searchOrder)
        assertThat(searchOrder.id).isEqualTo(id)
        assertThat(searchOrder.name).isEqualTo("soName")

        val newCopy = repo.findById(id)
        assertThat(newCopy).isNotEqualTo(searchOrder)
        assertThat(newCopy.id).isEqualTo(id)
        assertThat(newCopy.owner).isEqualTo(20)
        assertThat(newCopy.name).isEqualTo("soName")
    }

    @Test
    fun deletingRecord() {
        val searchOrder = repo.add(makeMinimalSearchOrder())
        assertThat(searchOrder == null).isFalse()
        assertThat(searchOrder.id).isGreaterThan(MAX_ID_PREPOPULATED)
        val id = searchOrder.id
        assertThat(searchOrder.owner).isEqualTo(10)

        val deleted = repo.delete(id, searchOrder.version)
        assertThat(deleted.id).isEqualTo(id)

        assertThat(repo.findById(id) == null).isTrue()
    }

    @Test
    fun enrichingAssociatedEntities_hasConditionsAndTerms() {
        val so = SearchOrder()
        so.id = 1L
        repo.enrichAssociatedEntitiesOf(so, LC)

        assertThat(so.searchConditions.size).isGreaterThanOrEqualTo(2)

        val sc1 = so.searchConditions[0]
        assertThat(sc1.authors).isEqualTo("kutlar")
        assertThat(sc1.displayValue).isEqualTo("kutlar")

        val sc2 = so.searchConditions[1]
        assertThat(sc2.authors).isEqualTo("turner")
        assertThat(sc2.publicationYear).isEqualTo("2014-2015")
        assertThat(sc2.displayValue).isEqualTo("turner AND 2014-2015")
    }

    @Test
    fun enrichingAssociatedEntities_hasExcludedIds() {
        val so = SearchOrder()
        so.id = 4L
        repo.enrichAssociatedEntitiesOf(so, LC)

        assertThat(so.excludedPaperIds).containsExactly(1L)
    }

    @Test
    fun addAndModifyAndDeleteSearchConditions() {
        // make search order with single condition (string search term)
        val initialSearchOrder = makeMinimalSearchOrder()
        initialSearchOrder.add(newConditionWithAuthors("foo"))
        assertThat(initialSearchOrder.id == null).isTrue()

        val savedSearchOrder = repo.add(initialSearchOrder)
        // saved search order now has a db-generated id, still has single condition.
        val searchOrderId = savedSearchOrder.id
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1)

        // add additional title condition to existing search order
        val titleCondition = newConditionWithTitle("PM2.5")
        val savedCondition = repo.addSearchCondition(titleCondition, searchOrderId, LC)
        assertSearchTermCount(1, 0, 0, 0, savedCondition)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(2)

        // modify the currently savedCondition to also have a publicationYear integer
        // search term
        savedCondition.publicationYear = "2000"
        val modifiedCondition = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertSearchTermCount(1, 1, 0, 0, modifiedCondition)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(3)

        // modify the integer condition
        savedCondition.publicationYear = "2001"
        val modifiedCondition2 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertSearchTermCount(1, 1, 0, 0, modifiedCondition2)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(3)

        // Add boolean condition with Code
        savedCondition.isFirstAuthorOverridden = java.lang.Boolean.TRUE
        savedCondition.addCode(Code("1A", null, null, false, 1, "c1", "", 1))
        val modifiedCondition3 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertSearchTermCount(1, 1, 1, 0, modifiedCondition3)
        assertThat(modifiedCondition3.codes).hasSize(1)
        assertThat(modifiedCondition3.codes.map { it.code }).containsExactly("1A")
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(4)

        // Change the boolean condition
        savedCondition.isFirstAuthorOverridden = java.lang.Boolean.FALSE
        val modifiedCondition4 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertSearchTermCount(1, 1, 1, 0, modifiedCondition4)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(4)

        savedCondition.modifiedDisplayValue = "foo"
        val modifiedCondition5 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertSearchTermCount(1, 1, 1, 2, modifiedCondition5)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(6)

        savedCondition.modifiedDisplayValue = "bar"
        val modifiedCondition6 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertSearchTermCount(1, 1, 1, 2, modifiedCondition6)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(6)

        // modify and verify newsletter fields (newsletterTopicId and newsletterHeadline)
        assertThat(savedCondition.newsletterTopicId == null).isTrue()
        savedCondition.setNewsletterTopic(NewsletterTopic(1, "foo"))
        val modifiedCondition7 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertThat(modifiedCondition7.newsletterTopicId).isEqualTo(1)

        assertThat(savedCondition.newsletterHeadline == null).isTrue()
        savedCondition.newsletterHeadline = "some"
        val modifiedCondition8 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertThat(modifiedCondition8.newsletterHeadline).isEqualTo("some")

        assertThat(savedCondition.newsletterIssue == null).isTrue()
        savedCondition.newsletterIssue = "some"
        val modifiedCondition9 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
        assertThat(modifiedCondition9.newsletterIssue).isEqualTo("some")

        // remove the new search condition
        repo.deleteSearchConditionWithId(savedCondition.searchConditionId)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1)
    }

    private fun newConditionWithAuthors(authors: String): SearchCondition = SearchCondition().apply {
        this.authors = authors
        assertSearchTermCount(1, 0, 0, 0, this)
    }

    private fun assertSearchTermCount(sst: Int, ist: Int, bst: Int, ast: Int, sc: SearchCondition) {
        assertThat(sc.stringSearchTerms).hasSize(sst)
        assertThat(sc.integerSearchTerms).hasSize(ist)
        assertThat(sc.booleanSearchTerms).hasSize(bst)
        assertThat(sc.auditSearchTerms).hasSize(ast)
    }

    private fun newConditionWithTitle(title: String): SearchCondition = SearchCondition().apply {
        this.title = title
        assertSearchTermCount(1, 0, 0, 0, this)
    }

    @Test
    fun addingSearchConditionWithoutIdThatAlreadyExists_canReturnUpdatedNewsletterAttributes() {
        // make search order with single condition (string search term)
        val initialSearchOrder = makeMinimalSearchOrder()
        initialSearchOrder.add(newConditionWithAuthors("foo"))
        assertThat(initialSearchOrder.id == null).isTrue()

        val savedSearchOrder = repo.add(initialSearchOrder)
        // saved search order now has a db-generated id, still has single condition.
        val searchOrderId = savedSearchOrder.id
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1)

        // add additional title condition to existing search order
        val titleCondition = newConditionWithTitle("PM2.5")
        val savedCondition = repo.addSearchCondition(titleCondition, searchOrderId, LC)
        assertSearchTermCount(1, 0, 0, 0, savedCondition)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(2)

        val newButEquivalentCondition = newConditionWithTitle("PM2.5")
        newButEquivalentCondition.newsletterIssue = "2018/02"
        newButEquivalentCondition.newsletterHeadline = "someHeadLine"
        newButEquivalentCondition.setNewsletterTopic(NewsletterTopic(1, "foo"))

        val savedNewCondition = repo.addSearchCondition(newButEquivalentCondition, searchOrderId, LC)
        assertThat(savedNewCondition.newsletterIssue).isEqualTo("2018/02")
        assertThat(savedNewCondition.newsletterHeadline).isEqualTo("someHeadLine")
        assertThat(savedNewCondition.newsletterTopicId).isEqualTo(1)

        // remove the new search condition
        repo.deleteSearchConditionWithId(savedCondition.searchConditionId)
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1)
    }

    @Test
    fun removingObsoleteSearchTerms_withNoRemovedKeys_doesNothing() {
        val sc = Mockito.mock(SearchCondition::class.java)
        whenever(sc.removedKeys).thenReturn(emptySet())
        repo.removeObsoleteSearchTerms(sc, -1L)
        verify(sc, never()).clearRemovedKeys()
    }

    @Test
    fun removingObsoleteSearchTerms_withRemovedKeys_deletesAndClearsKeys() {
        val sc = Mockito.mock(SearchCondition::class.java)
        whenever(sc.removedKeys).thenReturn(setOf("foo", "bar"))
        repo.removeObsoleteSearchTerms(sc, -1L)
        verify(sc).clearRemovedKeys()
    }

    companion object {
        private const val RECORD_COUNT_PREPOPULATED = 4
        private const val MAX_ID_PREPOPULATED = 4L
        private const val LC = "de"
    }
}
