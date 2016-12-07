package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringSearchTermsTest {

    private final StringSearchTerms st1 = new StringSearchTerms();
    private final StringSearchTerms st2 = new StringSearchTerms();

    @Test
    public void compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1);

        st1.put("key", new StringSearchTerm("key", "value"));
        st2.put("key", new StringSearchTerm("key", "value"));
    }

    @Test
    public void compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1.put("key", new StringSearchTerm("key", "value"));
        st2.put("key", new StringSearchTerm("key", "value"));
        assertEqualityBetween(st1, st2, 213944462);
    }

    @Test
    public void compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1.put("key1", new StringSearchTerm("key1", "value"));
        st2.put("key1", new StringSearchTerm("key1", "value"));
        st1.put("key2", new StringSearchTerm("key2", "value2"));
        st2.put("key2", new StringSearchTerm("key2", "value2"));
        assertEqualityBetween(st1, st2, 696972082);
    }

    private void assertEqualityBetween(StringSearchTerms st1, StringSearchTerms st2, int hashValue) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTerms_dontMatch() {
        st1.put("key", new StringSearchTerm("key", "value"));
        assertInequalityBetween(st1, st2, 213944462, 1);
    }

    private void assertInequalityBetween(StringSearchTerms st1, StringSearchTerms st2, int hashValue1, int hashValue2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue1);
        assertThat(st2.hashCode()).isEqualTo(hashValue2);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTermValues_dontMatch() {
        st1.put("key", new StringSearchTerm("key", "value"));
        st2.put("key", new StringSearchTerm("key", "valueX"));
        assertInequalityBetween(st1, st2, 213944462, -721841116);
    }

    @Test
    public void compareWithNullSelfOrDifferentClass() {
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals(new String())).isFalse();
    }

}
