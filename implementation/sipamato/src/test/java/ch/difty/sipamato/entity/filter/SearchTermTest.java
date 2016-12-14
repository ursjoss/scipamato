package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SearchTermTest {

    @Test
    public void booleanSearchTerm() {
        SearchTerm<?> st = SearchTerm.of(10, 0, 1l, "fn", "true");
        assertThat(st).isInstanceOf(BooleanSearchTerm.class);

        BooleanSearchTerm bst = (BooleanSearchTerm) st;
        assertThat(bst.getId()).isEqualTo(10);
        assertThat(bst.getSearchTermType()).isEqualTo(SearchTermType.BOOLEAN);
        assertThat(bst.getSearchConditionId()).isEqualTo(1l);
        assertThat(bst.getFieldName()).isEqualTo("fn");
        assertThat(bst.getRawSearchTerm()).isEqualTo("true");
        assertThat(bst.getValue()).isTrue();
    }

    @Test
    public void integerSearchTerm() {
        SearchTerm<?> st = SearchTerm.of(11, 1, 2l, "fn2", "5-7");
        assertThat(st).isInstanceOf(IntegerSearchTerm.class);

        IntegerSearchTerm ist = (IntegerSearchTerm) st;
        assertThat(ist.getId()).isEqualTo(11);
        assertThat(ist.getSearchTermType()).isEqualTo(SearchTermType.INTEGER);
        assertThat(ist.getSearchConditionId()).isEqualTo(2l);
        assertThat(ist.getFieldName()).isEqualTo("fn2");
        assertThat(ist.getRawSearchTerm()).isEqualTo("5-7");
        assertThat(ist.getValue()).isEqualTo(5);
        assertThat(ist.getValue2()).isEqualTo(7);
    }

    @Test
    public void stringSearchTerm() {
        SearchTerm<?> st = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        assertThat(st).isInstanceOf(StringSearchTerm.class);

        StringSearchTerm sst = (StringSearchTerm) st;
        assertThat(sst.getId()).isEqualTo(12);
        assertThat(sst.getSearchTermType()).isEqualTo(SearchTermType.STRING);
        assertThat(sst.getSearchConditionId()).isEqualTo(3l);
        assertThat(sst.getFieldName()).isEqualTo("fn3");
        assertThat(sst.getRawSearchTerm()).isEqualTo("foo*");
        assertThat(sst.getTokens()).hasSize(1);
        assertThat(sst.getTokens().get(0).sqlData).isEqualTo("foo%");
        assertThat(sst.getTokens().get(0).negate).isFalse();
    }

    @Test
    public void equality_withEqualValuesIncludingNonNullIds_equal() {
        SearchTerm<?> st1 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        SearchTerm<?> st2 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValues_butDifferingSearchConditionIds_differs() {
        SearchTerm<?> st1 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        SearchTerm<?> st2 = SearchTerm.of(12, 2, 4l, "fn3", "foo*");
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValuesAndNullIds() {
        SearchTerm<?> st1 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        st1.setId(null);
        SearchTerm<?> st2 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        st2.setId(null);
        assertEqualityBetween(st1, st2);
    }

    @Test
    public void equality_withEqualValuesAndMixedNullIds() {
        SearchTerm<?> st1 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        SearchTerm<?> st2 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        st2.setId(null);
        assertEqualityBetween(st1, st2);

        st1.setId(null);
        st2.setId(12l);
        assertEqualityBetween(st1, st2);
    }

    private void assertEqualityBetween(SearchTerm<?> st1, SearchTerm<?> st2) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
    }

    @Test
    public void equality_withNonEqualValuesInNonIds() {
        assertInequalityBetween(SearchTerm.of(12, 2, 3l, "fn4", "foo*"), SearchTerm.of(12, 2, 3l, "fn3", "foo*"));
        assertInequalityBetween(SearchTerm.of(12, 2, 3l, "fn3", "bar*"), SearchTerm.of(12, 2, 3l, "fn3", "foo*"));
    }

    private void assertInequalityBetween(SearchTerm<?> st1, SearchTerm<?> st2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
    }

    @Test
    public void equality_withSpecialCases() {
        SearchTerm<?> st1 = SearchTerm.of(12, 2, 3l, "fn3", "foo*");
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(new String())).isFalse();
    }
}
