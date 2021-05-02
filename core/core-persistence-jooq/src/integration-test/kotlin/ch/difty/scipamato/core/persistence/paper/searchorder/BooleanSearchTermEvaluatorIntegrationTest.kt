package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.BooleanSearchTerm
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm
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
open class BooleanSearchTermEvaluatorIntegrationTest : SearchTermEvaluatorIntegrationTest<BooleanSearchTerm>() {

    override val searchTermType = SearchTermType.BOOLEAN.id

    private val evaluator = BooleanSearchTermEvaluator()

    override fun makeSearchTerm(rawSearchTerm: String) =
        SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as BooleanSearchTerm

    @TestFactory
    fun booleanTests() : List<DynamicTest> = mapOf(
        "true" to BooleanExp(true, "fn = true"),
        "false" to BooleanExp(false, "fn = false"),
    ).map { (rawSearchTerm, exp) ->
        DynamicTest.dynamicTest("$rawSearchTerm -> $exp") {
            val st = makeSearchTerm(rawSearchTerm)
            st.value shouldBeEqualTo exp.value

            val ste = evaluator
            val s = ste.evaluate(st)

            s.toString() shouldBeEqualTo exp.condition
        }
    }
}

private data class BooleanExp(
    val value: Boolean,
    val condition: String,
)
