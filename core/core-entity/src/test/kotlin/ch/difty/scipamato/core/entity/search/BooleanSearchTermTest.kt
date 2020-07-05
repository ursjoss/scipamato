package ch.difty.scipamato.core.entity.search

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

private const val CONDITION_ID: Long = 3
private const val FIELD_NAME = "fn"

internal class BooleanSearchTermTest {

    private fun assertTerm(st: BooleanSearchTerm, value: Boolean, raw: String) {
        st.searchTermType shouldBeEqualTo SearchTermType.BOOLEAN
        st.searchConditionId shouldBeEqualTo CONDITION_ID
        st.fieldName shouldBeEqualTo FIELD_NAME
        st.value shouldBeEqualTo value
        st.rawSearchTerm shouldBeEqualTo raw
        st.displayValue shouldBeEqualTo (if (!value) "-" else "") + FIELD_NAME
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
