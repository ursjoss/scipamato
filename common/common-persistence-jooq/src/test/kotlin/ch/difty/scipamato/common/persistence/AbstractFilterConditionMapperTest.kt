package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.Condition
import org.junit.jupiter.api.Test

internal class AbstractFilterConditionMapperTest {

    private val condition = mockk<Condition>()

    private val filterConditionMapper = object : AbstractFilterConditionMapper<TestFilter>() {
        override fun internalMap(filter: TestFilter): List<Condition> = listOf(condition)
    }

    @Test
    fun withNullFilter() {
        val condition = filterConditionMapper.map(null)
        condition.toString() shouldBeEqualTo "1 = 1"
    }

    @Test
    fun foo() {
        val cond = "cond"
        every { condition.toString() } returns cond

        val condition = filterConditionMapper.map(TestFilter())

        condition.toString() shouldBeEqualTo cond
    }
}

class TestFilter : ScipamatoFilter()
