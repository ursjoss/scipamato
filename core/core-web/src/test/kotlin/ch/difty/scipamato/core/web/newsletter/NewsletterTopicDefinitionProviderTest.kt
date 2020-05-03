package ch.difty.scipamato.core.web.newsletter

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
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

internal class NewsletterTopicDefinitionProviderTest : AbstractWicketTest() {

    private lateinit var provider: NewsletterTopicDefinitionProvider

    @MockK(relaxed = true)
    private lateinit var filterMock: NewsletterTopicFilter

    @MockK
    private lateinit var entityMock: NewsletterTopicDefinition

    private lateinit var papers: List<NewsletterTopicDefinition>

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = NewsletterTopicDefinitionProvider(filterMock)
        papers = listOf(entityMock, entityMock, entityMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(newsletterTopicServiceMock, entityMock)
    }

    @Test
    fun defaultFilterIsNewNewsletterTopicFilter() {
        provider = NewsletterTopicDefinitionProvider()
        provider.filterState shouldBeEqualTo NewsletterTopicFilter()
    }

    @Test
    fun nullFilterResultsInNewNewsletterTopicFilter() {
        val p = NewsletterTopicDefinitionProvider(null)
        p.filterState shouldBeEqualTo NewsletterTopicFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { newsletterTopicServiceMock.countByFilter(filterMock) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { newsletterTopicServiceMock.countByFilter(filterMock) }
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
        provider = NewsletterTopicDefinitionProvider()
        provider.filterState shouldNotBeEqualTo filterMock
        provider.filterState = filterMock
        provider.filterState shouldBeEqualTo filterMock
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        papers = emptyList()
        every { newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns papers.iterator()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify {
            newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(0, 3, "title: ASC"))
        }
    }

    @Test
    fun iterating_throughFirst() {
        every { newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns papers.iterator()
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify {
            newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(0, 3, "title: ASC"))
        }
    }

    private fun assertRecordsIn(it: Iterator<NewsletterTopicDefinition>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondPage() {
        every { newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns papers.iterator()
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify {
            newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(3, 3, "title: ASC"))
        }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING)
        every { newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, any()) } returns papers.iterator()
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify {
            newsletterTopicServiceMock.findPageOfEntityDefinitions(filterMock, matchPaginationContext(6, 3, "title: DESC"))
        }
    }
}
