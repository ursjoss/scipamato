package ch.difty.scipamato.core.web.code

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeFilter
import ch.difty.scipamato.core.web.AbstractWicketTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

    @MockK(relaxed = true)
    private lateinit var filterMock: CodeFilter

    @MockK
    private lateinit var entityMock: CodeDefinition

    private lateinit var codes: List<CodeDefinition>

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = CodeDefinitionProvider(filterMock)
        codes = listOf(entityMock, entityMock, entityMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeServiceMock, entityMock)
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
        every { codeServiceMock.countByFilter(filterMock) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { codeServiceMock.countByFilter(filterMock) }
    }

    @Test
    fun gettingModel_wrapsEntity() {
        val model = provider.model(entityMock)
        model.getObject() shouldBeEqualTo entityMock
        verify { entityMock == entityMock }
    }

    @Test
    fun gettingFilterState_returnsFilter() {
        provider.filterState shouldBeEqualTo filterMock
    }

    @Test
    fun settingFilterState() {
        provider = CodeDefinitionProvider()
        provider.filterState shouldNotBeEqualTo filterMock
        provider.filterState = filterMock
        provider.filterState shouldBeEqualTo filterMock
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        codes = emptyList()
        every { codeServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns codes.iterator()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(0, 3, "sort: ASC"))
        }
    }

    @Test
    fun iterating_throughFirst() {
        every { codeServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns codes.iterator()
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(0, 3, "sort: ASC"))
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
        every { codeServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns codes.iterator()
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(3, 3, "sort: ASC"))
        }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("sort", SortOrder.DESCENDING)
        every { codeServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns codes.iterator()
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify {
            codeServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(6, 3, "sort: DESC"))
        }
    }
}
