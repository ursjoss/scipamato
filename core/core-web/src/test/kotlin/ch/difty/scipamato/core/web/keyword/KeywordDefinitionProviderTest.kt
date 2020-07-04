package ch.difty.scipamato.core.web.keyword

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordFilter
import ch.difty.scipamato.core.web.AbstractWicketTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.unmockkAll
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeEqualTo
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KeywordDefinitionProviderTest : AbstractWicketTest() {

    private val filterDummy = KeywordFilter().apply { nameMask = "foo" }
    private val entityDummy = KeywordDefinition(null, "en", null)

    private val entities = listOf(entityDummy, entityDummy, entityDummy)

    private lateinit var provider: KeywordDefinitionProvider

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = KeywordDefinitionProvider(filterDummy)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(keywordServiceMock)
        unmockkAll()
    }

    @Test
    fun defaultFilterIsNewKeywordFilter() {
        provider = KeywordDefinitionProvider()
        provider.filterState shouldBeEqualTo KeywordFilter()
    }

    @Test
    fun nullFilterResultsInNewKeywordFilter() {
        val p = KeywordDefinitionProvider(null)
        p.filterState shouldBeEqualTo KeywordFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { keywordServiceMock.countByFilter(filterDummy) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { keywordServiceMock.countByFilter(filterDummy) }
    }

    @Test
    fun gettingModel_wrapsEntity() {
        val model = provider.model(entityDummy)
        model.getObject() shouldBeEqualTo entityDummy
    }

    @Test
    fun gettingFilterState_returnsFilter() {
        provider.filterState shouldBeEqualTo filterDummy
    }

    @Test
    fun settingFilterState() {
        provider = KeywordDefinitionProvider()
        provider.filterState shouldNotBeEqualTo filterDummy
        provider.filterState = filterDummy
        provider.filterState shouldBeEqualTo filterDummy
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        every { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns emptyList<KeywordDefinition>().iterator()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(0, 3, "name: ASC")) }
    }

    @Test
    fun iterating_throughFirst() {
        every { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(0, 3, "name: ASC")) }
    }

    private fun assertRecordsIn(it: Iterator<KeywordDefinition>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondPage() {
        every { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(3, 3, "name: ASC")) }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("name", SortOrder.DESCENDING)
        every { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify { keywordServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(6, 3, "name: DESC")) }
    }
}
