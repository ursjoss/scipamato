package ch.difty.scipamato.core.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringSearchTermsTest {

    private static final String KEY   = "key";
    private static final String VALUE = "value";

    private final StringSearchTerms st1 = new StringSearchTerms();
    private final StringSearchTerms st2 = new StringSearchTerms();

    @Test
    public void compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1);

        st1.put(KEY, new StringSearchTerm(KEY, VALUE));
        st2.put(KEY, new StringSearchTerm(KEY, VALUE));
    }

    @Test
    public void compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1.put(KEY, new StringSearchTerm(KEY, VALUE));
        st2.put(KEY, new StringSearchTerm(KEY, VALUE));
        assertEqualityBetween(st1, st2, 118234894);
    }

    @Test
    public void compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1.put("key1", new StringSearchTerm("key1", VALUE));
        st2.put("key1", new StringSearchTerm("key1", VALUE));
        st1.put("key2", new StringSearchTerm("key2", "value2"));
        st2.put("key2", new StringSearchTerm("key2", "value2"));
        assertEqualityBetween(st1, st2, 266203500);
    }

    private void assertEqualityBetween(StringSearchTerms st1, StringSearchTerms st2, int hashValue) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTerms_dontMatch() {
        st1.put(KEY, new StringSearchTerm(KEY, VALUE));
        assertInequalityBetween(st1, st2, 118234894, 1);
    }

    private void assertInequalityBetween(StringSearchTerms st1, StringSearchTerms st2, int hashValue1, int hashValue2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue1);
        assertThat(st2.hashCode()).isEqualTo(hashValue2);
    }

    @Test
    public void compareEmptySearchTerms_withDifferentSearchTermValues_dontMatch() {
        st1.put(KEY, new StringSearchTerm(KEY, VALUE));
        st2.put(KEY, new StringSearchTerm(KEY, "valueX"));
        assertInequalityBetween(st1, st2, 118234894, -817550684);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void compareWithNullSelfOrDifferentClass() {
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals("")).isFalse();
    }

}
