package ch.difty.scipamato.core.web.newsletter

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
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

@Suppress("SpellCheckingInspection")
internal class NewsletterProviderTest : AbstractWicketTest() {

    private lateinit var provider: NewsletterProvider

    @MockK(relaxed = true)
    private lateinit var filterMock: NewsletterFilter

    @MockK
    private lateinit var entityMock: Newsletter
    private lateinit var papers: List<Newsletter>

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = NewsletterProvider(filterMock)
        provider.setService(newsletterServiceMock)
        papers = listOf(entityMock, entityMock, entityMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(newsletterServiceMock, entityMock)
    }

    @Test
    fun defaultFilterIsNewNewsletterFilter() {
        provider = NewsletterProvider()
        provider.filterState shouldBeEqualTo NewsletterFilter()
    }

    @Test
    fun nullFilterResultsInNewNewsletterFilter() {
        val p = NewsletterProvider(null)
        p.filterState shouldBeEqualTo NewsletterFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { newsletterServiceMock.countByFilter(filterMock) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { newsletterServiceMock.countByFilter(filterMock) }
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
        provider = NewsletterProvider()
        provider.filterState shouldNotBeEqualTo filterMock
        provider.filterState = filterMock
        provider.filterState shouldBeEqualTo filterMock
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        papers = emptyList()
        every { newsletterServiceMock.findPageByFilter(filterMock, any()) } returns papers
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify { newsletterServiceMock.findPageByFilter(filterMock, matchPaginationContext(0, 3, "issue: DESC")) }
    }

    @Test
    fun iterating_throughFirst() {
        every { newsletterServiceMock.findPageByFilter(filterMock, any()) } returns papers
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify { newsletterServiceMock.findPageByFilter(filterMock, matchPaginationContext(0, 3, "issue: DESC")) }
    }

    private fun assertRecordsIn(it: Iterator<Newsletter>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondPage() {
        every { newsletterServiceMock.findPageByFilter(filterMock, any()) } returns papers
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify { newsletterServiceMock.findPageByFilter(filterMock, matchPaginationContext(3, 3, "issue: DESC")) }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING)
        every { newsletterServiceMock.findPageByFilter(filterMock, any()) } returns papers
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify { newsletterServiceMock.findPageByFilter(filterMock, matchPaginationContext(6, 3, "title: DESC")) }
    }

    @Test
    fun iterating_throughThirdPage_ascendingly() {
        provider.setSort("title", SortOrder.ASCENDING)
        every { newsletterServiceMock.findPageByFilter(filterMock, any()) } returns papers
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify { newsletterServiceMock.findPageByFilter(filterMock, matchPaginationContext(6, 3, "title: ASC")) }
    }
}
