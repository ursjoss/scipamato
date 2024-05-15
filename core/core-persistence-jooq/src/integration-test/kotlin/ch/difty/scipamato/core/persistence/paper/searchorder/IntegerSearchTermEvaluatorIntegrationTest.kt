package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.EXACT
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.GREATER_OR_EQUAL
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.GREATER_THAN
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.LESS_OR_EQUAL
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.LESS_THAN
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.RANGE
import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Test class to integration test the search term and the search term evaluator.
 */
@JooqTest
@Testcontainers
@Suppress("MagicNumber")
open class IntegerSearchTermEvaluatorIntegrationTest : SearchTermEvaluatorIntegrationTest<IntegerSearchTerm>() {

    override val searchTermType: Int = SearchTermType.INTEGER.id

    private val evaluator: IntegerSearchTermEvaluator = IntegerSearchTermEvaluator()

    override fun makeSearchTerm(rawSearchTerm: String) =
        SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as IntegerSearchTerm

    @TestFactory
    fun integerTests(): List<DynamicTest> = mapOf(
        "<2016" to IntegerExp(2016, 2016, LESS_THAN, "fn < 2016"),
        "<=2016" to IntegerExp(2016, 2016, LESS_OR_EQUAL, "fn <= 2016"),
        "2016" to IntegerExp(2016, 2016, EXACT, "fn = 2016"),
        "=2016" to IntegerExp(2016, 2016, EXACT, "fn = 2016"),
        ">2016" to IntegerExp(2016, 2016, GREATER_THAN, "fn > 2016"),
        ">=2016" to IntegerExp(2016, 2016, GREATER_OR_EQUAL, "fn >= 2016"),
        "2016-2018" to IntegerExp(2016, 2018, RANGE, "fn between 2016 and 2018"),
    ).map { (rawSearchTerm, exp) ->
        DynamicTest.dynamicTest("$rawSearchTerm -> $exp") {
            val st = makeSearchTerm(rawSearchTerm)
            st.value shouldBeEqualTo exp.value
            st.value2 shouldBeEqualTo exp.value2
            st.type shouldBeEqualTo exp.type

            val ste = evaluator
            val s = ste.evaluate(st)

            s.toString() shouldBeEqualTo exp.condition
        }
    }
}

private data class IntegerExp(
    val value: Int,
    val value2: Int,
    val type: MatchType,
    val condition: String,
)
