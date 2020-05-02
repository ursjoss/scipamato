@file:Suppress("FunctionName")

package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "DuplicatedCode", "SpellCheckingInspection", "MagicNumber", "SameParameterValue")
internal open class JooqSearchOrderRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqSearchOrderRepo

    @Test
    fun findingAll() {
        val searchOrders = repo.findAll()
        searchOrders shouldHaveSize 4
    }

    @Test
    @Suppress("ConstantConditionIf")
    fun findingById_withExistingId_returnsEntity() {
        val searchOrder = repo.findById(RECORD_COUNT_PREPOPULATED.toLong())
            ?: fail { "Unable to load search order" }
        if (MAX_ID_PREPOPULATED > 0)
            searchOrder.id shouldBeEqualTo MAX_ID_PREPOPULATED
        else
            searchOrder.shouldBeNull()
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        repo.findById(-1L).shouldBeNull()
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val so = makeMinimalSearchOrder()
        val searchCondition = SearchCondition()
        searchCondition.authors = "foo"
        so.add(searchCondition)
        so.addExclusionOfPaperWithId(4L)
        so.id.shouldBeNull()
        so.searchConditions.first().id.shouldBeNull()
        so.searchConditions.first().stringSearchTerms.first().id.shouldBeNull()

        val saved = repo.add(so) ?: fail { "Unable to save search order" }

        saved.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        saved.owner shouldBeEqualTo 10
        saved.searchConditions.first().searchConditionId?.shouldBeGreaterThan(5L)
        saved.searchConditions.first().stringSearchTerms.first().id.shouldNotBeNull()

        saved.excludedPaperIds shouldContainSame listOf(4L)
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
        val searchOrder = repo.add(makeMinimalSearchOrder()) ?: fail { "Unable to load search order" }
        searchOrder.shouldNotBeNull()
        searchOrder.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = searchOrder.id ?: error("id must no be null now")
        searchOrder.owner shouldBeEqualTo 10
        searchOrder.name.shouldBeNull()

        searchOrder.owner = 20
        searchOrder.name = "soName"
        repo.update(searchOrder)
        searchOrder.id as Long shouldBeEqualTo id
        searchOrder.name shouldBeEqualTo "soName"

        val newCopy = repo.findById(id) ?: fail { "Unable to load search order" }
        newCopy shouldNotBeEqualTo searchOrder
        newCopy.id shouldBeEqualTo id
        newCopy.owner shouldBeEqualTo 20
        newCopy.name shouldBeEqualTo "soName"
    }

    @Test
    fun deletingRecord() {
        val searchOrder = repo.add(makeMinimalSearchOrder()) ?: fail { "Unable to add search order" }
        searchOrder.shouldNotBeNull()
        searchOrder.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = searchOrder.id ?: error("id must no be null now")
        searchOrder.owner shouldBeEqualTo 10

        val deleted = repo.delete(id, searchOrder.version)
        deleted.id shouldBeEqualTo id

        repo.findById(id).shouldBeNull()
    }

    @Test
    fun enrichingAssociatedEntities_hasConditionsAndTerms() {
        val so = SearchOrder()
        so.id = 1L
        repo.enrichAssociatedEntitiesOf(so, LC)

        so.searchConditions.size shouldBeGreaterOrEqualTo 2

        val sc1 = so.searchConditions[0]
        sc1.authors shouldBeEqualTo "kutlar"
        sc1.displayValue shouldBeEqualTo "kutlar"

        val sc2 = so.searchConditions[1]
        sc2.authors shouldBeEqualTo "turner"
        sc2.publicationYear shouldBeEqualTo "2014-2015"
        sc2.displayValue shouldBeEqualTo "turner AND 2014-2015"
    }

    @Test
    fun enrichingAssociatedEntities_hasExcludedIds() {
        val so = SearchOrder()
        so.id = 4L
        repo.enrichAssociatedEntitiesOf(so, LC)

        so.excludedPaperIds shouldContainAll listOf(1L)
    }

    @Suppress("LongMethod")
    @Test
    fun addAndModifyAndDeleteSearchConditions() {
        // make search order with single condition (string search term)
        val initialSearchOrder = makeMinimalSearchOrder()
        initialSearchOrder.add(newConditionWithAuthors("foo"))
        initialSearchOrder.id.shouldBeNull()

        val savedSearchOrder = repo.add(initialSearchOrder) ?: fail { "Unable to add search order" }
        // saved search order now has a db-generated id, still has single condition.
        val searchOrderId = savedSearchOrder.id ?: error("id must no be null now")
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 1

        // add additional title condition to existing search order
        val titleCondition = newConditionWithTitle("PM2.5")
        val savedCondition = repo.addSearchCondition(titleCondition, searchOrderId, LC)
            ?: fail { "should have returned saved condtion" }
        assertSearchTermCount(1, 0, 0, 0, savedCondition)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 2

        // modify the currently savedCondition to also have a publicationYear integer
        // search term
        savedCondition.publicationYear = "2000"
        val modifiedCondition = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search order" }
        assertSearchTermCount(1, 1, 0, 0, modifiedCondition)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 3

        // modify the integer condition
        savedCondition.publicationYear = "2001"
        val modifiedCondition2 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        assertSearchTermCount(1, 1, 0, 0, modifiedCondition2)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 3

        // Add boolean condition with Code
        savedCondition.isFirstAuthorOverridden = java.lang.Boolean.TRUE
        savedCondition.addCode(Code("1A", "Code 1A", null, false, 1, "c1", "", 1))
        val modifiedCondition3 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        assertSearchTermCount(1, 1, 1, 0, modifiedCondition3)
        modifiedCondition3.codes shouldHaveSize 1
        modifiedCondition3.codes.map { it.code } shouldContainAll listOf("1A")
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 4

        // Change the boolean condition
        savedCondition.isFirstAuthorOverridden = java.lang.Boolean.FALSE
        val modifiedCondition4 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        assertSearchTermCount(1, 1, 1, 0, modifiedCondition4)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 4

        savedCondition.modifiedDisplayValue = "foo"
        val modifiedCondition5 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        assertSearchTermCount(1, 1, 1, 2, modifiedCondition5)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 6

        savedCondition.modifiedDisplayValue = "bar"
        val modifiedCondition6 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        assertSearchTermCount(1, 1, 1, 2, modifiedCondition6)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 6

        // modify and verify newsletter fields (newsletterTopicId and newsletterHeadline)
        savedCondition.newsletterTopicId.shouldBeNull()
        savedCondition.setNewsletterTopic(NewsletterTopic(1, "foo"))
        val modifiedCondition7 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        modifiedCondition7.newsletterTopicId shouldBeEqualTo 1

        savedCondition.newsletterHeadline.shouldBeNull()
        savedCondition.newsletterHeadline = "some"
        val modifiedCondition8 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        modifiedCondition8.newsletterHeadline shouldBeEqualTo "some"

        savedCondition.newsletterIssue.shouldBeNull()
        savedCondition.newsletterIssue = "some"
        val modifiedCondition9 = repo.updateSearchCondition(savedCondition, searchOrderId, LC)
            ?: fail { "Unable to update search condition" }
        modifiedCondition9.newsletterIssue shouldBeEqualTo "some"

        // remove the new search condition
        savedCondition.searchConditionId?.let {
            repo.deleteSearchConditionWithId(it)
            repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 1
        } ?: fail { "Unexpected condition with savedCondition.searchConditionId being null" }
    }

    private fun newConditionWithAuthors(authors: String): SearchCondition = SearchCondition().apply {
        this.authors = authors
        assertSearchTermCount(1, 0, 0, 0, this)
    }

    private fun assertSearchTermCount(sst: Int, ist: Int, bst: Int, ast: Int, sc: SearchCondition) {
        sc.stringSearchTerms shouldHaveSize sst
        sc.integerSearchTerms shouldHaveSize ist
        sc.booleanSearchTerms shouldHaveSize bst
        sc.auditSearchTerms shouldHaveSize ast
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
        initialSearchOrder.id.shouldBeNull()

        val savedSearchOrder = repo.add(initialSearchOrder) ?: fail { "Unable to add search order" }
        // saved search order now has a db-generated id, still has single condition.
        val searchOrderId = savedSearchOrder.id ?: error("id must no be null now")
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 1

        // add additional title condition to existing search order
        val titleCondition = newConditionWithTitle("PM2.5")
        val savedCondition = repo.addSearchCondition(titleCondition, searchOrderId, LC)
            ?: fail { "should have returned added condition" }
        assertSearchTermCount(1, 0, 0, 0, savedCondition)
        repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 2

        val newButEquivalentCondition = newConditionWithTitle("PM2.5")
        newButEquivalentCondition.newsletterIssue = "2018/02"
        newButEquivalentCondition.newsletterHeadline = "someHeadLine"
        newButEquivalentCondition.setNewsletterTopic(NewsletterTopic(1, "foo"))

        val savedNewCondition = repo.addSearchCondition(newButEquivalentCondition, searchOrderId, LC)
            ?: fail { "should have added search condition" }
        savedNewCondition.newsletterIssue shouldBeEqualTo "2018/02"
        savedNewCondition.newsletterHeadline shouldBeEqualTo "someHeadLine"
        savedNewCondition.newsletterTopicId shouldBeEqualTo 1

        // remove the new search condition
        savedCondition.searchConditionId?.let {
            repo.deleteSearchConditionWithId(it)
            repo.findConditionIdsWithSearchTerms(searchOrderId) shouldHaveSize 1
        } ?: fail { "Unexpected condition with savedCondition.searchConditionId being null" }
    }

    @Test
    fun removingObsoleteSearchTerms_withNoRemovedKeys_doesNothing() {
        val sc = mockk<SearchCondition> {
            every { removedKeys } returns emptySet()
        }
        repo.removeObsoleteSearchTerms(sc, -1L)
        verify(exactly = 0) { sc.clearRemovedKeys() }
    }

    @Test
    fun removingObsoleteSearchTerms_withRemovedKeys_deletesAndClearsKeys() {
        val sc = mockk<SearchCondition> {
            every { removedKeys } returns setOf("foo", "bar")
            every { clearRemovedKeys() } returns Unit
        }
        repo.removeObsoleteSearchTerms(sc, -1L)
        verify { sc.clearRemovedKeys() }
    }

    companion object {
        private const val RECORD_COUNT_PREPOPULATED = 4
        private const val MAX_ID_PREPOPULATED = 4L
        private const val LC = "de"
    }
}
