package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

private const val CONDITION_ID: Long = 7
private const val FIELD_NAME = "fn"

internal class IntegerSearchTermTest {

    @Suppress("SameParameterValue")
    private fun assertTerm(st: IntegerSearchTerm, type: MatchType, value: Int, raw: String) {
        assertTerm(st, type, value, value, raw)
    }

    private fun assertTerm(st: IntegerSearchTerm, type: MatchType, value: Int, value2: Int, raw: String) {
        assertThat(st.searchTermType).isEqualTo(SearchTermType.INTEGER)
        assertThat(st.searchConditionId).isEqualTo(CONDITION_ID)
        assertThat(st.fieldName).isEqualTo(FIELD_NAME)
        assertThat(st.type).isEqualTo(type)
        assertThat(st.value).isEqualTo(value)
        assertThat(st.value2).isEqualTo(value2)
        assertThat(st.rawSearchTerm).isEqualTo(raw)
        assertThat(st.displayValue).isEqualTo(raw)
    }

    @Test
    fun exactSearch() {
        val raw = "2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.EXACT, 2016, raw)
    }

    @Test
    fun exactSearch_withSpaces() {
        val raw = "   2016 "
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.EXACT, 2016, raw)
    }

    @Test
    fun exactSearch_withEqual() {
        val raw = "=2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.EXACT, 2016, raw)
    }

    @Test
    fun exactSearch_withEqualAndSpaces() {
        val raw = "=    2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.EXACT, 2016, raw)
    }

    @Test
    fun greaterThanOrEqualSearch() {
        val raw = ">=2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.GREATER_OR_EQUAL, 2016, raw)
    }

    @Test
    fun greaterThanOrEqualSearch_WithSpaces() {
        val raw = "   >=    2016 "
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.GREATER_OR_EQUAL, 2016, raw)
    }

    @Test
    fun greaterThanSearch() {
        val raw = ">2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.GREATER_THAN, 2016, raw)
    }

    @Test
    fun lessThanOrEqualSearch() {
        val raw = "<=2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.LESS_OR_EQUAL, 2016, raw)
    }

    @Test
    fun lessThanSearch() {
        val raw = "<2016"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.LESS_THAN, 2016, raw)
    }

    @Test
    fun rangeSearch() {
        val raw = "2016-2018"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.RANGE, 2016, 2018, raw)
    }

    @Test
    fun rangeSearch_withSpaces() {
        val raw = "    2016     -    2018 "
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.RANGE, 2016, 2018, raw)
    }

    @Test
    fun hasNoValue_usingEquals() {
        val raw = "=\"\""
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.MISSING, 0, 0, raw)
    }

    @Test
    fun hasNoValue_skippingEquals() {
        val raw = "\"\""
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.MISSING, 0, 0, raw)
    }

    @Test
    fun hasAnyValue() {
        val raw = ">\"\""
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.PRESENT, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermEqual() {
        val raw = "="
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermRange() {
        val raw = "-"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermRangeRightOpen() {
        val raw = "2017-"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermRangeLeftOpen() {
        val raw = "-2019"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermGreaterThan() {
        val raw = ">"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermGreaterThanOrEqual() {
        val raw = ">="
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermLessThan() {
        val raw = "<"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun incompleteSearchTermLessThanOrEqual() {
        val raw = "<="
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 0, 0, raw)
    }

    @Test
    fun invalidSearch_withNonNumericCharacters() {
        val raw = "2014a"
        val st = IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, MatchType.INCOMPLETE, 1, 1, raw)
    }

    @Test
    fun invalidSearch_withInvalidPattern() {
        try {
            IntegerSearchTerm(CONDITION_ID, FIELD_NAME, ">>2014")
            fail<Any>("Should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("For input string: \">2014\"")
        }

    }

}
