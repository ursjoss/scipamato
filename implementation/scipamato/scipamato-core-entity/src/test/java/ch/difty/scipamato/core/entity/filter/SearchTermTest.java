package ch.difty.scipamato.core.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SearchTermTest {

    @Test
    public void equality_withEqualValuesIncludingNonNullIds_equal() {
        SearchTerm st1 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        SearchTerm st2 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValues_butDifferingSearchConditionIds_differs() {
        SearchTerm st1 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        SearchTerm st2 = SearchTerms.newSearchTerm(12, 2, 4l, "fn3", "foo*");
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValuesAndNullIds() {
        SearchTerm st1 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        st1.setId(null);
        SearchTerm st2 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        st2.setId(null);
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValuesAndMixedNullIds() {
        SearchTerm st1 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        SearchTerm st2 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        st2.setId(null);
        assertEqualityBetween(st1, st2);

        st1.setId(null);
        st2.setId(12l);
        assertEqualityBetween(st1, st2);
    }

    private void assertEqualityBetween(SearchTerm st1, SearchTerm st2) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
    }

    @Test
    public void equality_withNonEqualValuesInNonIds() {
        assertInequalityBetween(SearchTerms.newSearchTerm(12, 2, 3l, "fn4", "foo*"), SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*"));
        assertInequalityBetween(SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "bar*"), SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*"));
    }

    private void assertInequalityBetween(SearchTerm st1, SearchTerm st2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void equality_withSpecialCases() {
        SearchTerm st1 = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals("")).isFalse();
    }

    @Test
    public void displayValueEqualsSearchTerm() {
        SearchTerm st = SearchTerms.newSearchTerm(11, 1, 2l, "fn2", "5-7");
        assertThat(st).isInstanceOf(IntegerSearchTerm.class);
        assertThat(st.getDisplayValue()).isEqualTo(st.getRawSearchTerm());
    }

}
