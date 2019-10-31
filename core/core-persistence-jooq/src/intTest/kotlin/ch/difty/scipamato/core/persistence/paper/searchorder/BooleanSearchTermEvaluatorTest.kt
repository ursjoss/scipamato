package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.BooleanSearchTerm
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("FunctionName")
internal class BooleanSearchTermEvaluatorTest {

    private val evaluator = BooleanSearchTermEvaluator()

    private val stMock = mock<BooleanSearchTerm>()

    private fun expectSearchTerm(v: Boolean) {
        whenever(stMock.fieldName).thenReturn("fieldX")
        whenever(stMock.value).thenReturn(v)
    }

    @Test
    fun buildingCondition_whenTrue() {
        expectSearchTerm(true)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x = true")
    }

    @Test
    fun buildingCondition_whenFalse() {
        expectSearchTerm(false)
        val c = evaluator.evaluate(stMock)
        assertThat(c.toString()).isEqualTo("field_x = false")
    }
}
