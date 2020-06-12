package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.core.entity.search.SearchCondition
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldThrow
import org.apache.wicket.model.Model
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.ArrayList

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class SearchConditionProviderTest {

    private lateinit var provider: SearchConditionProvider

    @MockK
    private lateinit var mockCondition1: SearchCondition

    @MockK
    private lateinit var mockCondition2: SearchCondition

    @MockK
    private lateinit var mockCondition3: SearchCondition

    @MockK
    private lateinit var mockCondition4: SearchCondition

    private val conditions: MutableList<SearchCondition> = ArrayList()

    @BeforeEach
    fun setUp() {
        conditions.addAll(listOf(mockCondition1, mockCondition2, mockCondition3, mockCondition4))
        provider = SearchConditionProvider(Model.ofList(conditions))
    }

    @Test
    fun degenerateConstruction_withNullSearchOrderModel1() {
        invoking { SearchConditionProvider(Model.ofList(null)) } shouldThrow NullPointerException::class
    }

    @Test
    fun providerSize_equals_conditionSize() {
        provider.size() shouldBeEqualTo conditions.size.toLong()
    }

    @Test
    fun iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        provider.iterator(0, 100).asSequence() shouldContainSame
            sequenceOf(mockCondition1, mockCondition2, mockCondition3, mockCondition4)
    }

    @Test
    fun iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        provider.iterator(0, 2).asSequence() shouldContainSame
            sequenceOf(mockCondition1, mockCondition2)
    }

    @Test
    fun iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        provider.iterator(1, 2).asSequence() shouldContainSame
            sequenceOf(mockCondition2, mockCondition3)
    }

    @Test
    fun gettingModel() {
        val model = provider.model(mockCondition1)
        model.getObject() shouldBeEqualTo mockCondition1
    }
}
