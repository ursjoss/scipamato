package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AbstractSearchTermTest {

    @Test
    public void equality_withEqualValuesIncludingNonNullIds_equal() {
        SearchTerm st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        SearchTerm st2 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValues_butDifferingSearchConditionIds_differs() {
        SearchTerm st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        SearchTerm st2 = SearchTerm.newSearchTerm(12, 2, 4L, "fn3", "foo*");
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValuesAndNullIds() {
        SearchTerm st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        ((AbstractSearchTerm) st1).setId(null);
        SearchTerm st2 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        ((AbstractSearchTerm) st2).setId(null);
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValuesAndMixedNullIds() {
        SearchTerm st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        SearchTerm st2 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        ((AbstractSearchTerm) st2).setId(null);
        assertEqualityBetween(st1, st2);

        ((AbstractSearchTerm) st1).setId(null);
        ((AbstractSearchTerm) st2).setId(12L);
        assertEqualityBetween(st1, st2);
    }

    private void assertEqualityBetween(SearchTerm st1, SearchTerm st2) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
    }

    @Test
    public void equality_withNonEqualValuesInNonIds() {
        assertInequalityBetween(SearchTerm.newSearchTerm(12, 2, 3L, "fn4", "foo*"),
            SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*"));
        assertInequalityBetween(SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "bar*"),
            SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*"));
    }

    private void assertInequalityBetween(SearchTerm st1, SearchTerm st2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void equality_withSpecialCases() {
        SearchTerm st1 = SearchTerm.newSearchTerm(12, 2, 3L, "fn3", "foo*");
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals("")).isFalse();
    }

    @Test
    public void displayValueEqualsSearchTerm() {
        SearchTerm st = SearchTerm.newSearchTerm(11, 1, 2L, "fn2", "5-7");
        assertThat(st).isInstanceOf(IntegerSearchTerm.class);
        assertThat(st.getDisplayValue()).isEqualTo(st.getRawSearchTerm());
    }

}
