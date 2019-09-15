package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("FunctionName")
internal class IntegerSearchTermEvaluatorTest {

    private val evaluator = IntegerSearchTermEvaluator()

    private val stMock = mock<IntegerSearchTerm>()

    private fun expectSearchTerm(type: MatchType, v: Int) {
        expectSearchTerm(type, v, null)
    }

    private fun expectSearchTerm(type: MatchType, v1: Int, v2: Int?) {
        whenever(stMock.type).thenReturn(type)
        whenever(stMock.fieldName).thenReturn("fieldX")
        whenever(stMock.value).thenReturn(v1)
        if (v2 != null)
            whenever(stMock.value2).thenReturn(v2)
    }

    @Test
    fun buildingCondition_withGreaterThanComparison() {
        expectSearchTerm(MatchType.GREATER_THAN, 10)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x > 10")
    }

    @Test
    fun buildingCondition_withGreaterThanOrEqualComparison() {
        expectSearchTerm(MatchType.GREATER_OR_EQUAL, 10)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x >= 10")
    }

    @Test
    fun buildingCondition_withExactValue() {
        expectSearchTerm(MatchType.EXACT, 10)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x = 10")
    }

    @Test
    fun buildingCondition_withLessThanOrEqualComparison() {
        expectSearchTerm(MatchType.LESS_OR_EQUAL, 10)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x <= 10")
    }

    @Test
    fun buildingCondition_withLessThanComparison() {
        expectSearchTerm(MatchType.LESS_THAN, 10)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x < 10")
    }

    @Test
    fun buildingCondition_withRangeComparison() {
        expectSearchTerm(MatchType.RANGE, 10, 15)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x between 10 and 15")
    }

    @Test
    fun buildingCondition_withFieldValueMissing() {
        val any = 0
        expectSearchTerm(MatchType.MISSING, any)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x is null")
    }

    @Test
    fun buildingCondition_withAnyFieldValuePresent() {
        val any = 0
        expectSearchTerm(MatchType.PRESENT, any)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x is not null")
    }

    @Test
    fun buildingCondition_withIncompleteSearchTerm() {
        whenever(stMock.type).thenReturn(MatchType.INCOMPLETE)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("1 = 0")
    }

    @Test
    fun buildingCondition_withFieldId_prependsTable() {
        expectSearchTerm(MatchType.GREATER_THAN, 10)
        whenever(stMock.fieldName).thenReturn("id")
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("paper.id > 10")
    }

}
