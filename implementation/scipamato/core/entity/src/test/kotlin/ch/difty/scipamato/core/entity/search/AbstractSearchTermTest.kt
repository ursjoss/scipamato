package ch.difty.scipamato.core.entity.search

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class AbstractSearchTermTest {

    @Test
    fun equality_withEqualValuesIncludingNonNullIds_equal() {
        val st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        val st2 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        assertEqualityBetween(st1, st2)
    }

    @Test
    fun equality_withEqualValues_butDifferingSearchConditionIds_differs() {
        val st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        val st2 = SearchTerm.newSearchTerm(12, 2, 4L, "fn3", "foo*")
        assertEqualityBetween(st1, st2)
    }

    @Test
    fun equality_withEqualValuesAndNullIds() {
        val st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        (st1 as AbstractSearchTerm).id = null
        val st2 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        (st2 as AbstractSearchTerm).id = null
        assertEqualityBetween(st1, st2)
    }

    @Test
    fun equality_withEqualValuesAndMixedNullIds() {
        val st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        val st2 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        (st2 as AbstractSearchTerm).id = null
        assertEqualityBetween(st1, st2)

        (st1 as AbstractSearchTerm).id = null
        st2.id = 12L
        assertEqualityBetween(st1, st2)
    }

    private fun assertEqualityBetween(st1: SearchTerm, st2: SearchTerm) {
        assertThat(st1 == st2).isTrue()
        assertThat(st2 == st1).isTrue()
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode())
    }

    @Test
    fun equality_withNonEqualValuesInNonIds() {
        assertInequalityBetween(SearchTerm.newSearchTerm(12, 2, 3L, "fn4", "foo*"),
                SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*"))
        assertInequalityBetween(SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "bar*"),
                SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*"))
    }

    private fun assertInequalityBetween(st1: SearchTerm, st2: SearchTerm) {
        assertThat(st1 == st2).isFalse()
        assertThat(st2 == st1).isFalse()
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode())
    }

    @Test
    fun equality_withSpecialCases() {
        val st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*")
        assertThat(st1 == st1).isTrue()
        assertThat(st1 == null).isFalse()
    }

    @Test
    fun displayValueEqualsSearchTerm() {
        val st = SearchTerm.newSearchTerm(11, 1, 2L, "fn2", "5-7")
        assertThat(st).isInstanceOf(IntegerSearchTerm::class.java)
        assertThat(st.displayValue).isEqualTo(st.rawSearchTerm)
    }

}
