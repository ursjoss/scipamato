package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("FunctionName", "TooManyFunctions", "SpellCheckingInspection", "MagicNumber")
internal open class JooqPaperSlimBySearchOrderRepoIntegrationTest {

    private val so = SearchOrder()
    private val sc = SearchCondition()
    private val pc = PaginationRequest(0, 10)

    @Autowired
    private lateinit var repo: JooqPaperSlimBySearchOrderRepo

    @Test
    fun findingPaged_withNonMatchingCondition_findsNoRecords() {
        so.isGlobal = true
        repo.findPageBySearchOrder(so, pc).shouldBeEmpty()
    }

    @Test
    fun findingPaged_withMatchingSearchCondition() {
        sc.authors = "Turner"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).isNotEmpty().shouldBeTrue()
    }

    @Test
    fun counting() {
        so.isGlobal = true
        repo.countBySearchOrder(so) shouldBeGreaterOrEqualTo 0
    }

    @Test
    fun findingPaged_withSearchConditionUsingIdRange() {
        sc.id = "10-15"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc) shouldHaveSize 6
    }

    @Test
    fun findingPaged_withSearchConditionUsingIdLessThan() {
        sc.id = "<11"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc) shouldHaveSize 5
    }

    @Test
    fun findingPaged_withSearchConditionUsingNumberGreaterThan() {
        sc.number = "23"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc) shouldHaveSize 1
    }

    @Test
    fun findingPaged_withSearchConditionUsingPublicationYearRange() {
        sc.publicationYear = "<2015"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc) shouldHaveSize 6
    }

    @Test
    fun findingPaged_withSearchConditionUsingMethods_withPositiveConditionOnly() {
        sc.methods = "querschnitt"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).map { it.number } shouldContainSame listOf(15L, 18L, 25L, 29L)
    }

    @Test
    fun findingPaged_withSearchConditionUsingMethods_withPositiveAndNegativeCondition() {
        sc.methods = "querschnitt -regression"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).map { it.number } shouldContainSame listOf(15L)
    }

    @Test
    fun findingPaged_withSearchConditionUsingMethods_withPositiveAndNegativeCondition2() {
        sc.methods = "querschnitt -schweiz"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).map { it.number } shouldContainSame listOf(15L, 18L, 25L)
    }

    @Test
    fun findingPaged_withSearchCondition_withNoAttachments() {
        sc.hasAttachments = false
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).map { it.number } shouldContainSame listOf(1L, 2L, 3L, 4L, 10L, 11L, 12L, 13L, 14L, 15L)
    }

    @Test
    fun findingPaged_withSearchCondition_withAttachments() {
        sc.hasAttachments = true
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).shouldBeEmpty()
    }

    @Test
    fun findingPaged_withSearchCondition_withNotExistingAttachmentName() {
        sc.attachmentNameMask = "foobarbaz"
        so.add(sc)
        repo.findPageBySearchOrder(so, pc).shouldBeEmpty()
    }
}
