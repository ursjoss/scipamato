package ch.difty.scipamato.common.persistence.paging

import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PaginationRequestTest {

    private lateinit var pr: PaginationRequest
    private lateinit var sort: String

    @Test
    fun degenerateConstruction_withInvalidOffset() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { PaginationRequest(-1, 1) }
    }

    @Test
    fun degenerateConstruction_withInvalidPageSize() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { PaginationRequest(0, 0) }
    }

    private fun assertPaginationRequest(pc: PaginationContext, offSet: Int, pageSize: Int, fooSort: String?) {
        assertThat(pc.offset).isEqualTo(offSet)
        assertThat(pc.pageSize).isEqualTo(pageSize)
        if (fooSort != null) {
            val s = pc.sort
            assertThat(s.getSortPropertyFor("foo").toString()).isEqualTo(fooSort)
            assertThat(s.getSortPropertyFor("bar")).isNull()
        } else {
            assertThat(pc.sort).isNull()
        }
    }

    @Test
    fun paginationContextWithSortButNoPaging() {
        sort = "foo: DESC"
        pr = PaginationRequest(Direction.DESC, "foo")

        assertPaginationRequest(pr, 0, Integer.MAX_VALUE, sort)
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 0, size 2147483647, sort: foo: DESC]")
    }

    @Test
    fun paginationContextWithSort_withOffset0_andPageSize10() {
        sort = "foo: DESC"
        pr = PaginationRequest(0, 10, Direction.DESC, "foo")

        assertPaginationRequest(pr, 0, 10, sort)
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 0, size 10, sort: foo: DESC]")
    }

    @Test
    fun paginationContextWithSort_withOffset24_andPageSize12() {
        sort = "foo: ASC"
        pr = PaginationRequest(24, 12, Direction.ASC, "foo")

        assertPaginationRequest(pr, 24, 12, sort)
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 24, size 12, sort: foo: ASC]")
    }

    @Test
    fun paginationContextWithoutSort_withOffset6_andPageSize2() {
        pr = PaginationRequest(6, 2)

        assertPaginationRequest(pr, 6, 2, null)
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 6, size 2, sort: null]")
    }

    @Test
    fun equality_ofSameObjectInstance() {
        pr = PaginationRequest(5, 5)
        assertEquality(pr, pr)
    }

    private fun assertEquality(pr1: PaginationRequest, pr2: PaginationRequest) {
        assertThat(pr1 == pr2).isTrue()
        assertThat(pr1.hashCode()).isEqualTo(pr2.hashCode())
    }

    @Test
    fun inequality_ofPaginationRequestWithDifferentSorts() {
        pr = PaginationRequest(5, 5)
        assertInequality(pr, PaginationRequest(5, 5, Direction.ASC, "foo"))
    }

    private fun assertInequality(pr1: PaginationRequest, pr2: PaginationRequest) {
        assertThat(pr1 == pr2).isFalse()
        assertThat(pr1.hashCode()).isNotEqualTo(pr2.hashCode())
    }

    @Test
    fun inequality_ofPaginationRequestWithDifferentSorts2() {
        pr = PaginationRequest(5, 6, Direction.ASC, "bar")
        assertInequality(pr, PaginationRequest(5, 6, Direction.ASC, "foo"))
    }

    @Test
    fun inequality_ofPaginationRequestWithDifferentSorts3() {
        pr = PaginationRequest(5, 6, Direction.DESC, "foo")
        assertInequality(pr, PaginationRequest(5, 6, Direction.ASC, "foo"))
    }

    @Test
    fun inequality_ofPaginationRequestWithNonSortAttributes1() {
        pr = PaginationRequest(5, 6, Direction.ASC, "foo")
        assertInequality(pr, PaginationRequest(6, 6, Direction.ASC, "foo"))
    }

    @Test
    fun inequality_ofPaginationRequestWithNonSortAttributes2() {
        pr = PaginationRequest(5, 6, Direction.ASC, "foo")
        assertInequality(pr, PaginationRequest(5, 7, Direction.ASC, "foo"))
    }

    @Test
    fun equality_ofPaginationRequestWithSameAttributes_withSort() {
        pr = PaginationRequest(5, 6, Direction.ASC, "foo")
        assertEquality(pr, PaginationRequest(5, 6, Direction.ASC, "foo"))
    }

    @Test
    fun equality_ofPaginationRequestWithSameAttributes_withoutSort() {
        pr = PaginationRequest(5, 6)
        val pr2 = PaginationRequest(5, 6)
        assertEquality(pr, pr2)
    }

    @Test
    fun inequality_ofPaginationRequestWithNonPaginationRequest() {
        pr = PaginationRequest(5, 6)
        val pr2 = ""
        assertThat(pr.hashCode()).isNotEqualTo(pr2.hashCode())
    }
}
