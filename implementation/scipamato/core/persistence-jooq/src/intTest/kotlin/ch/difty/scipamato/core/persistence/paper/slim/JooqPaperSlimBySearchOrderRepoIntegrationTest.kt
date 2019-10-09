package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import org.assertj.core.api.Assertions.assertThat
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
    fun finding() {
        so.isGlobal = true
        assertThat(repo.findBySearchOrder(so) == null).isFalse()
    }

    @Test
    fun findingPaged_withNonMatchingCondition_findsNoRecords() {
        so.isGlobal = true
        assertThat(repo.findPageBySearchOrder(so, pc)).isEmpty()
    }

    @Test
    fun findingPaged_withMatchingSearchCondition() {
        sc.authors = "Turner"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc)).isNotEmpty
    }

    @Test
    fun counting() {
        so.isGlobal = true
        assertThat(repo.countBySearchOrder(so)).isGreaterThanOrEqualTo(0)
    }

    @Test
    fun findingPaged_withSearchConditionUsingIdRange() {
        sc.id = "10-15"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(6)
    }

    @Test
    fun findingPaged_withSearchConditionUsingIdLessThan() {
        sc.id = "<11"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(5)
    }

    @Test
    fun findingPaged_withSearchConditionUsingNumberGreaterThan() {
        sc.number = "23"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(1)
    }

    @Test
    fun findingPaged_withSearchConditionUsingPublicationYearRange() {
        sc.publicationYear = "<2015"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(6)
    }

    @Test
    fun findingPaged_withSearchConditionUsingMethods_withPositiveConditionOnly() {
        sc.methods = "querschnitt"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc).map { it.number }).containsExactly(15L, 18L, 25L, 29L)
    }

    @Test
    fun findingPaged_withSearchConditionUsingMethods_withPositiveAndNegativeCondition() {
        sc.methods = "querschnitt -regression"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc).map { it.number }).containsExactly(15L)
    }

    @Test
    fun findingPaged_withSearchConditionUsingMethods_withPositiveAndNegativeCondition2() {
        sc.methods = "querschnitt -schweiz"
        so.add(sc)
        assertThat(repo.findPageBySearchOrder(so, pc).map { it.number }).containsExactly(15L, 18L, 25L)
    }
}
