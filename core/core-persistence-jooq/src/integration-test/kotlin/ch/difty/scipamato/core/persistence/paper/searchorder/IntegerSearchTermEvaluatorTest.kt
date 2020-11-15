package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("FunctionName", "TooManyFunctions", "MagicNumber")
internal class IntegerSearchTermEvaluatorTest {

    private val evaluator = IntegerSearchTermEvaluator()

    private val stMock = mockk<IntegerSearchTerm>()

    private fun expectSearchTerm(type: MatchType, v: Int) {
        expectSearchTerm(type, v, null)
    }

    private fun expectSearchTerm(type: MatchType, v1: Int, v2: Int?) {
        every { stMock.type } returns type
        every { stMock.fieldName } returns "fieldX"
        every { stMock.value } returns v1
        v2?.let {
            every { stMock.value2 } returns v2
        }
    }

    @Test
    fun buildingCondition_withGreaterThanComparison() {
        expectSearchTerm(MatchType.GREATER_THAN, 10)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x > 10"
    }

    @Test
    fun buildingCondition_withGreaterThanOrEqualComparison() {
        expectSearchTerm(MatchType.GREATER_OR_EQUAL, 10)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x >= 10"
    }

    @Test
    fun buildingCondition_withExactValue() {
        expectSearchTerm(MatchType.EXACT, 10)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x = 10"
    }

    @Test
    fun buildingCondition_withLessThanOrEqualComparison() {
        expectSearchTerm(MatchType.LESS_OR_EQUAL, 10)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x <= 10"
    }

    @Test
    fun buildingCondition_withLessThanComparison() {
        expectSearchTerm(MatchType.LESS_THAN, 10)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x < 10"
    }

    @Test
    fun buildingCondition_withRangeComparison() {
        expectSearchTerm(MatchType.RANGE, 10, 15)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x between 10 and 15"
    }

    @Test
    fun buildingCondition_withFieldValueMissing() {
        val any = 0
        expectSearchTerm(MatchType.MISSING, any)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x is null"
    }

    @Test
    fun buildingCondition_withAnyFieldValuePresent() {
        val any = 0
        expectSearchTerm(MatchType.PRESENT, any)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x is not null"
    }

    @Test
    fun buildingCondition_withIncompleteSearchTerm() {
        val any = 0
        expectSearchTerm(MatchType.INCOMPLETE, any)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "false"
    }

    @Test
    fun buildingCondition_withFieldId_prependsTable() {
        expectSearchTerm(MatchType.GREATER_THAN, 10)
        every { stMock.fieldName } returns "id"
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "paper.id > 10"
    }
}
