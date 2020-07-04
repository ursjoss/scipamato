package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.search.UserFilter
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

internal class UserProviderTest : AbstractWicketTest() {

    private val filter = UserFilter().apply { nameMask = "foo" }
    private val entity = User()

    private val entities = listOf(entity, entity, entity)

    private lateinit var provider: UserProvider

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = UserProvider(filter)
        provider.setService(userServiceMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(userServiceMock)
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
        every { userServiceMock.countByFilter(filter) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { userServiceMock.countByFilter(filter) }
    }

    @Test
    fun gettingModel_wrapsEntity() {
        val model = provider.model(entity)
        model.getObject() shouldBeEqualTo entity
    }

    @Test
    fun gettingFilterState_returnsFilter() {
        provider.filterState shouldBeEqualTo filter
    }

    @Test
    fun settingFilterState() {
        provider = UserProvider()
        provider.filterState shouldNotBeEqualTo filter
        provider.filterState = filter
        provider.filterState shouldBeEqualTo filter
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        every { userServiceMock.findPageByFilter(any(), any()) } returns emptyList()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(0, 3, "userName: ASC")) }
    }

    @Test
    fun iterating_throughFirst() {
        every { userServiceMock.findPageByFilter(any(), any()) } returns entities
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
        every { userServiceMock.findPageByFilter(any(), any()) } returns entities
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(3, 3, "userName: ASC")) }
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING)
        every { userServiceMock.findPageByFilter(any(), any()) } returns entities
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verify { userServiceMock.findPageByFilter(any(), matchPaginationContext(6, 3, "title: DESC")) }
    }
}
