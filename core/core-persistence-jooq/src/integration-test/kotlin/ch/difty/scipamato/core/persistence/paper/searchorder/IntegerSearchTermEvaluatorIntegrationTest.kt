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
open class IntegerSearchTermEvaluatorIntegrationTest : SearchTermEvaluatorIntegrationTest<IntegerSearchTerm>() {

    override val searchTermType: Int = SearchTermType.INTEGER.id

    private val evaluator: IntegerSearchTermEvaluator = IntegerSearchTermEvaluator()

    override fun makeSearchTerm(rawSearchTerm: String) =
        SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as IntegerSearchTerm

    @Suppress("DuplicatedCode")
    @ParameterizedTest(name = "[{index}] {0} -> [{1},{2}] [type {3}] ({4})")
    @MethodSource("integerParameters")
    fun integerTest(rawSearchTerm: String, value: Int, value2: Int, type: MatchType, condition: String) {
        val st = makeSearchTerm(rawSearchTerm)
        assertThat(st.value).isEqualTo(value)
        assertThat(st.value2).isEqualTo(value2)
        assertThat(st.type).isEqualTo(type)

        val ste = evaluator
        val s = ste.evaluate(st)

        assertThat(s.toString()).isEqualTo(condition)
    }

    companion object {
        @JvmStatic
        @Suppress("unused", "MagicNumber")
        private fun integerParameters() = listOf(
            Arguments.of("<2016", 2016, 2016, LESS_THAN, "fn < 2016"),
            Arguments.of("<=2016", 2016, 2016, LESS_OR_EQUAL, "fn <= 2016"),
            Arguments.of("2016", 2016, 2016, EXACT, "fn = 2016"),
            Arguments.of("=2016", 2016, 2016, EXACT, "fn = 2016"),
            Arguments.of(">2016", 2016, 2016, GREATER_THAN, "fn > 2016"),
            Arguments.of(">=2016", 2016, 2016, GREATER_OR_EQUAL, "fn >= 2016"),
            Arguments.of("2016-2018", 2016, 2018, RANGE, "fn between 2016 and 2018")
        )
    }
}
