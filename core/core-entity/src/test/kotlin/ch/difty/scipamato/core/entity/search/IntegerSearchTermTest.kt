package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

private const val CONDITION_ID: Long = 7
private const val FIELD_NAME = "fn"

internal class IntegerSearchTermTest {

    @Suppress("SameParameterValue")
    private fun assertTerm(st: IntegerSearchTerm, type: MatchType, value: Int, raw: String) {
        assertTerm(st, type, value, value, raw)
    }

    private fun assertTerm(st: IntegerSearchTerm, type: MatchType, value: Int, value2: Int, raw: String) {
        st.searchTermType shouldBeEqualTo SearchTermType.INTEGER
        st.searchConditionId shouldBeEqualTo CONDITION_ID
        st.fieldName shouldBeEqualTo FIELD_NAME
        st.type shouldBeEqualTo type
        st.value shouldBeEqualTo value
        st.value2 shouldBeEqualTo value2
        st.rawSearchTerm shouldBeEqualTo raw
        st.displayValue shouldBeEqualTo raw
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
        invoking { IntegerSearchTerm(CONDITION_ID, FIELD_NAME, ">>2014") } shouldThrow
            IllegalArgumentException::class withMessage """For input string: ">2014""""
    }
}
