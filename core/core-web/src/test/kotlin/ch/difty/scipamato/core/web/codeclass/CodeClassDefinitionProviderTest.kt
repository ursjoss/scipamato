package ch.difty.scipamato.core.web.codeclass

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter
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

internal class CodeClassDefinitionProviderTest : AbstractWicketTest() {

    private val filterDummy = CodeClassFilter().apply { nameMask = "foo" }
    private val entityDummy = CodeClassDefinition(null, "en", null)

    private val entities = listOf(entityDummy, entityDummy, entityDummy)

    private lateinit var provider: CodeClassDefinitionProvider

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = CodeClassDefinitionProvider(filterDummy)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeClassServiceMock)
    }

    @Test
    fun defaultFilterIsNewCodeClassFilter() {
        provider = CodeClassDefinitionProvider()
        provider.filterState shouldBeEqualTo CodeClassFilter()
    }

    @Test
    fun nullFilterResultsInNewCodeClassFilter() {
        val p = CodeClassDefinitionProvider(null)
        p.filterState shouldBeEqualTo CodeClassFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { codeClassServiceMock.countByFilter(filterDummy) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { codeClassServiceMock.countByFilter(filterDummy) }
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
        provider = CodeClassDefinitionProvider()
        provider.filterState shouldNotBeEqualTo filterDummy
        provider.filterState = filterDummy
        provider.filterState shouldBeEqualTo filterDummy
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        every { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns emptyList<CodeClassDefinition>().iterator()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(0, 3, "id: ASC")) }
    }

    @Test
    fun iterating_throughFirst() {
        every { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(0, 3, "id: ASC")) }
    }

    private fun assertRecordsIn(it: Iterator<CodeClassDefinition>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondPage() {
        every { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(3, 3, "id: ASC")) }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("id", SortOrder.DESCENDING)
        every { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, any()) } returns entities.iterator()
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify { codeClassServiceMock.findPageOfEntityDefinitions(filterDummy, matchPaginationContext(6, 3, "id: DESC")) }
    }
}
