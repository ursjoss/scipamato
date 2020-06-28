package ch.difty.scipamato.common.persistence.paging

import io.mockk.Matcher
import io.mockk.MockKMatcherScope

fun MockKMatcherScope.matchPaginationContext(
    offset: Int,
    pageSize: Int,
    sort: String
): PaginationContext = match(PaginationContextMatcher(offset, pageSize, sort))

data class PaginationContextMatcher(
    val offset: Int,
    val pageSize: Int,
    val sort: String
) : Matcher<PaginationContext> {
    override fun match(arg: PaginationContext?): Boolean =
        arg != null && arg.offset == offset && arg.pageSize == pageSize && sort == arg.sort.toString()
}
