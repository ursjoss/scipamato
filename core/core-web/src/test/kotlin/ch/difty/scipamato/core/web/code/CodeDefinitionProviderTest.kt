package ch.difty.scipamato.core.web.code

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeFilter
import ch.difty.scipamato.core.web.AbstractWicketTest
import io.mockk.confirmVerified
import io.mockk.every
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

internal class CodeDefinitionProviderTest : AbstractWicketTest() {

    private lateinit var provider: CodeDefinitionProvider

    private val filterDummy = CodeFilter().apply { nameMask = "foo" }
    private val entityDummy = CodeDefinition(null, "en", null, 1, false, 1)

    private val entities = listOf(entityDummy, entityDummy, entityDummy)

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = CodeDefinitionProvider(filterDummy)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeServiceMock)
    }

    @Test
    fun defaultFilterIsNewCodeFilter() {
        provider = CodeDefinitionProvider()
        provider.filterState shouldBeEqualTo CodeFilter()
    }

    @Test
    fun nullFilterResultsInNewCodeFilter() {
        val p = CodeDefinitionProvider(null)
        p.filterState shouldBeEqualTo CodeFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { codeServiceMock.countByFilter(filterDummy) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { codeServiceMock.countByFilter(filterDummy) }
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
        provider = CodeDefinitionProvider()
        provider.filterState shouldNotBeEqualTo filterDummy
        provider.filterState = filterDummy
        provider.filterState shouldBeEqualTo filterDummy
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        every { codeServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns
            emptyList<CodeDefinition>().iterator()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(0, 3, "sort: ASC"))
        }
    }

    @Test
    fun iterating_throughFirst() {
        every { codeServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(0, 3, "sort: ASC"))
        }
    }

    private fun assertRecordsIn(it: Iterator<CodeDefinition>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondPage() {
        every { codeServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(3, 3, "sort: ASC"))
        }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("sort", SortOrder.DESCENDING)
        every { codeServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(6, 3, "sort: DESC"))
        }
    }
}
