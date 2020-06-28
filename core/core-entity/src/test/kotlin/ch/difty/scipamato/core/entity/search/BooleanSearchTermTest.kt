package ch.difty.scipamato.core.entity.search

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val CONDITION_ID: Long = 3
private const val FIELD_NAME = "fn"

internal class BooleanSearchTermTest {

    private fun assertTerm(st: BooleanSearchTerm, value: Boolean, raw: String) {
        assertThat(st.searchTermType).isEqualTo(SearchTermType.BOOLEAN)
        assertThat(st.searchConditionId).isEqualTo(CONDITION_ID)
        assertThat(st.fieldName).isEqualTo(FIELD_NAME)
        assertThat(st.value).isEqualTo(value)
        assertThat(st.rawSearchTerm).isEqualTo(raw)
        assertThat(st.displayValue).isEqualTo((if (!value) "-" else "") + FIELD_NAME)
    }

    @Test
    fun ifTrue() {
        val raw = "true"
        val st = BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, true, raw)
    }

    @Test
    fun ifTrue_withSpaces() {
        val raw = " true   "
        val st = BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, true, raw)
    }

    @Test
    fun ifFalse() {
        val raw = "false"
        val st = BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, false, raw)
    }

    @Test
    fun ifFalse_withSpaces() {
        val raw = " false  "
        val st = BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw)
        assertTerm(st, false, raw)
    }
}
