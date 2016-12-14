package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringSearchTerms2Test {

    private final StringSearchTerms2 st1 = new StringSearchTerms2();
    private final StringSearchTerms2 st2 = new StringSearchTerms2();

    @Test
    public void compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1);

        st1.put("key", new StringSearchTerm2("key", "value"));
        st2.put("key", new StringSearchTerm2("key", "value"));
    }

    @Test
    public void compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1.put("key", new StringSearchTerm2("key", "value"));
        st2.put("key", new StringSearchTerm2("key", "value"));
        assertEqualityBetween(st1, st2, 115262162);
    }

    @Test
    public void compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1.put("key1", new StringSearchTerm2("key1", "value"));
        st2.put("key1", new StringSearchTerm2("key1", "value"));
        st1.put("key2", new StringSearchTerm2("key2", "value2"));
        st2.put("key2", new StringSearchTerm2("key2", "value2"));
        assertEqualityBetween(st1, st2, 1614595920);
    }

    private void assertEqualityBetween(StringSearchTerms2 st1, StringSearchTerms2 st2, int hashValue) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTerms_dontMatch() {
        st1.put("key", new StringSearchTerm2("key", "value"));
        assertInequalityBetween(st1, st2, 115262162, 1);
    }

    private void assertInequalityBetween(StringSearchTerms2 st1, StringSearchTerms2 st2, int hashValue1, int hashValue2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue1);
        assertThat(st2.hashCode()).isEqualTo(hashValue2);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTermValues_dontMatch() {
        st1.put("key", new StringSearchTerm2("key", "value"));
        st2.put("key", new StringSearchTerm2("key", "valueX"));
        assertInequalityBetween(st1, st2, 115262162, -820523416);
    }

    @Test
    public void compareWithNullSelfOrDifferentClass() {
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals(new String())).isFalse();
    }

}
