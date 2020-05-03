package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.search.UserFilter
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

internal class UserProviderTest : AbstractWicketTest() {

    @MockK(relaxed = true)
    private lateinit var filterMock: UserFilter

    @MockK
    private lateinit var entityMock: User

    private lateinit var provider: UserProvider
    private lateinit var papers: List<User>

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = UserProvider(filterMock)
        provider.setService(userServiceMock)
        papers = listOf(entityMock, entityMock, entityMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(userServiceMock, entityMock)
    }

    @Test
    fun defaultFilterIsNewUserFilter() {
        provider = UserProvider()
        provider.filterState shouldBeEqualTo UserFilter()
    }

    @Test
    fun nullFilterResultsInNewUserFilter() {
        val p = UserProvider(null)
        p.filterState shouldBeEqualTo UserFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { userServiceMock.countByFilter(filterMock) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { userServiceMock.countByFilter(filterMock) }
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
        provider = UserProvider()
        provider.filterState shouldNotBeEqualTo filterMock
        provider.filterState = filterMock
        provider.filterState shouldBeEqualTo filterMock
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        papers = emptyList()
        every { userServiceMock.findPageByFilter(any(), any()) } returns papers
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(0, 3, "userName: ASC")) }
    }

    @Test
    fun iterating_throughFirst() {
        every { userServiceMock.findPageByFilter(any(), any()) } returns papers
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(0, 3, "userName: ASC")) }
    }

    private fun assertRecordsIn(it: Iterator<User>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondPage() {
        every { userServiceMock.findPageByFilter(any(), any()) } returns papers
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(3, 3, "userName: ASC")) }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING)
        every { userServiceMock.findPageByFilter(any(), any()) } returns papers
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(6, 3, "title: DESC")) }
    }
}
