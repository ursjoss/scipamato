package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SearchTermTest {

    @Test
    public void booleanSearchTerm() {
        SearchTerm<?> st = SearchTerm.of(0, 1l, "fn", "true");
        assertThat(st).isInstanceOf(BooleanSearchTerm.class);

        BooleanSearchTerm bst = (BooleanSearchTerm) st;
        assertThat(bst.getSearchTermType()).isEqualTo(SearchTermType.BOOLEAN);
        assertThat(bst.getSearchConditionId()).isEqualTo(1l);
        assertThat(bst.getFieldName()).isEqualTo("fn");
        assertThat(bst.getRawSearchTerm()).isEqualTo("true");
        assertThat(bst.getValue()).isTrue();
    }

    @Test
    public void integerSearchTerm() {
        SearchTerm<?> st = SearchTerm.of(1, 2l, "fn2", "5-7");
        assertThat(st).isInstanceOf(IntegerSearchTerm.class);

        IntegerSearchTerm ist = (IntegerSearchTerm) st;
        assertThat(ist.getSearchTermType()).isEqualTo(SearchTermType.INTEGER);
        assertThat(ist.getSearchConditionId()).isEqualTo(2l);
        assertThat(ist.getFieldName()).isEqualTo("fn2");
        assertThat(ist.getRawSearchTerm()).isEqualTo("5-7");
        assertThat(ist.getValue()).isEqualTo(5);
        assertThat(ist.getValue2()).isEqualTo(7);
    }

    @Test
    public void stringSearchTerm() {
        SearchTerm<?> st = SearchTerm.of(2, 3l, "fn3", "foo*");
        assertThat(st).isInstanceOf(StringSearchTerm.class);

        StringSearchTerm sst = (StringSearchTerm) st;
        assertThat(sst.getSearchTermType()).isEqualTo(SearchTermType.STRING);
        assertThat(sst.getSearchConditionId()).isEqualTo(3l);
        assertThat(sst.getFieldName()).isEqualTo("fn3");
        assertThat(sst.getRawSearchTerm()).isEqualTo("foo*");
        assertThat(sst.getValue()).isEqualTo("foo");
    }
}
