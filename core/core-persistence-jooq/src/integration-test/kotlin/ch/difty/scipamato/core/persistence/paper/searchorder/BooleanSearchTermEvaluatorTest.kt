package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.BooleanSearchTerm
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("FunctionName")
internal class BooleanSearchTermEvaluatorTest {

    private val evaluator = BooleanSearchTermEvaluator()

    private val stMock = mockk<BooleanSearchTerm>()

    private fun expectSearchTerm(v: Boolean) {
        every { stMock.fieldName } returns "fieldX"
        every { stMock.value } returns v
    }

    @Test
    fun buildingCondition_whenTrue() {
        expectSearchTerm(true)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x = true"
    }

    @Test
    fun buildingCondition_whenFalse() {
        expectSearchTerm(false)
        val c = evaluator.evaluate(stMock)
        c.toString() shouldBeEqualTo "field_x = false"
    }
}
