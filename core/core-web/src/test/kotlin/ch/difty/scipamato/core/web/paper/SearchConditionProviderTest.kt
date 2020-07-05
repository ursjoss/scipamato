package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.core.entity.search.SearchCondition
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.apache.wicket.model.Model
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SearchConditionProviderTest {

    private lateinit var provider: SearchConditionProvider

    private val cond1Dummy = SearchCondition(1L)
    private val cond2Dummy = SearchCondition(2L)
    private val cond3Dummy = SearchCondition(3L)
    private val cond4Dummy = SearchCondition(4L)

    private val conditions = listOf(cond1Dummy, cond2Dummy, cond3Dummy, cond4Dummy)

    @BeforeEach
    fun setUp() {
        provider = SearchConditionProvider(Model.ofList(conditions))
    }

    @Test
    fun providerSize_equals_conditionSize() {
        provider.size() shouldBeEqualTo conditions.size.toLong()
    }

    @Test
    fun iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        provider.iterator(0, 100).asSequence() shouldContainSame
            sequenceOf(cond1Dummy, cond2Dummy, cond3Dummy, cond4Dummy)
    }

    @Test
    fun iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        provider.iterator(0, 2).asSequence() shouldContainSame
            sequenceOf(cond1Dummy, cond2Dummy)
    }

    @Test
    fun iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        provider.iterator(1, 2).asSequence() shouldContainSame
            sequenceOf(cond2Dummy, cond3Dummy)
    }

    @Test
    fun gettingModel() {
        val model = provider.model(cond1Dummy)
        model.getObject() shouldBeEqualTo cond1Dummy
    }
}
