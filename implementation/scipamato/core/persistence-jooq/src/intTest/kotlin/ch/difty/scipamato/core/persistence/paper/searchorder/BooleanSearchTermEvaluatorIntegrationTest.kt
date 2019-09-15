package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.BooleanSearchTerm
import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Test class to integration test the search term and the search term evaluator.
 */
@JooqTest
@Testcontainers
internal open class BooleanSearchTermEvaluatorIntegrationTest : SearchTermEvaluatorIntegrationTest<BooleanSearchTerm>() {

    override val searchTermType = SearchTermType.BOOLEAN.id

    private val evaluator = BooleanSearchTermEvaluator()

    override fun makeSearchTerm(rawSearchTerm: String) = SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as BooleanSearchTerm

    @ParameterizedTest(name = "[{index}] {0} -> {1} ({4})")
    @MethodSource("booleanParameters")
    fun booleanTest(rawSearchTerm: String, value: Boolean?, condition: String) {
        val st = makeSearchTerm(rawSearchTerm)
        assertThat(st.value).isEqualTo(value)

        val ste = evaluator
        val s = ste.evaluate(st)

        assertThat(s.toString()).isEqualTo(condition)
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        private fun booleanParameters() = listOf(
                Arguments.of("true", true, "fn = true"),
                Arguments.of("false", false, "fn = false")
        )
    }
}
