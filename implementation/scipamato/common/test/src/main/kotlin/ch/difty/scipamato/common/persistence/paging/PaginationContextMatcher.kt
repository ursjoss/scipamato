package ch.difty.scipamato.common.persistence.paging

import org.mockito.ArgumentMatcher

class PaginationContextMatcher(
        private val offset: Int,
        private val pageSize: Int,
        private val sort: String
) : ArgumentMatcher<PaginationContext> {

    override fun matches(p: PaginationContext?): Boolean =
            p != null && p.offset == offset && p.pageSize == pageSize && sort == p.sort.toString()

}