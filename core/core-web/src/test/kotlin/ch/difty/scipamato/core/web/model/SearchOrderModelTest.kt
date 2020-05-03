package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import io.mockk.Matcher
import io.mockk.MockKMatcherScope
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class SearchOrderModelTest : ModelTest() {

    @MockK
    private lateinit var searchOrderMock: SearchOrder

    @AfterEach
    fun tearDown() {
        confirmVerified(searchOrderServiceMock, searchOrderMock)
    }

    @Test
    fun test() {
        val owner = 1
        val maxRows = 10
        every { searchOrderServiceMock.findPageByFilter(any(), any()) } returns listOf(searchOrderMock, searchOrderMock)
        val m = SearchOrderModel(owner, maxRows)
        m.load() shouldContainSame listOf(searchOrderMock, searchOrderMock)
        verify {
            searchOrderServiceMock.findPageByFilter(matchSearchOrderFilter(owner), matchPagRequestWithMaxRows(maxRows))
        }
        verify { searchOrderMock == searchOrderMock }
    }

    private inline fun <reified T : SearchOrderFilter> MockKMatcherScope.matchSearchOrderFilter(
        ownerIncludingGlobal: Int
    ): T = match(SearchOrderFilterMatcher(ownerIncludingGlobal))

    private data class SearchOrderFilterMatcher(val ownerIncludingGlobal: Int) : Matcher<SearchOrderFilter> {
        override fun match(arg: SearchOrderFilter?): Boolean =
            arg != null &&
                arg.ownerIncludingGlobal != null &&
                arg.ownerIncludingGlobal == ownerIncludingGlobal &&
                arg.nameMask == null &&
                arg.owner == null &&
                arg.global == null
    }

    private fun MockKMatcherScope.matchPagRequestWithMaxRows(maxRows: Int): PaginationRequest =
        match(PaginationRequestWithMaxRows(maxRows))

    private data class PaginationRequestWithMaxRows(val maxRows: Int) : Matcher<PaginationRequest> {
        override fun match(arg: PaginationRequest?): Boolean {
            if (arg != null && arg.offset == 0 && arg.pageSize == maxRows) {
                val it: Iterator<SortProperty> = arg.sort.iterator()
                if (it.hasNext()) {
                    val (name, direction) = it.next()
                    return SORT == name && !direction.isAscending()
                }
            }
            return false
        }

        companion object {
            private const val SORT = "id"
        }
    }
}
