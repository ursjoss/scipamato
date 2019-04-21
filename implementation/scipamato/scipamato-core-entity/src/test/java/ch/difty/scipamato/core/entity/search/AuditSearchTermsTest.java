package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("SameParameterValue")
public class AuditSearchTermsTest {

    private static final String KEY   = "key";
    private static final String VALUE = "value";

    private final AuditSearchTerms st1 = new AuditSearchTerms();
    private final AuditSearchTerms st2 = new AuditSearchTerms();

    @Test
    public void compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1);

        st1.put(KEY, SearchTerm.newAuditSearchTerm(KEY, VALUE));
        st2.put(KEY, SearchTerm.newAuditSearchTerm(KEY, VALUE));
    }

    @Test
    public void compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1.put(KEY, SearchTerm.newAuditSearchTerm(KEY, VALUE));
        st2.put(KEY, SearchTerm.newAuditSearchTerm(KEY, VALUE));
        assertEqualityBetween(st1, st2, 118234894);
    }

    @Test
    public void compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1.put("key1", SearchTerm.newAuditSearchTerm("key1", VALUE));
        st2.put("key1", SearchTerm.newAuditSearchTerm("key1", VALUE));
        st1.put("key2", SearchTerm.newAuditSearchTerm("key2", "value2"));
        st2.put("key2", SearchTerm.newAuditSearchTerm("key2", "value2"));
        assertEqualityBetween(st1, st2, 266203500);
    }

    private void assertEqualityBetween(AuditSearchTerms st1, AuditSearchTerms st2, int hashValue) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue);
    }

    @Test
    public void compareEmptySearchTerms_withDifferentSearchTerms_doNotMatch() {
        st1.put(KEY, SearchTerm.newAuditSearchTerm(KEY, VALUE));
        assertInequalityBetween(st1, st2, 118234894, 1);
    }

    private void assertInequalityBetween(AuditSearchTerms st1, AuditSearchTerms st2, int hashValue1, int hashValue2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue1);
        assertThat(st2.hashCode()).isEqualTo(hashValue2);
    }

    @Test
    public void compareEmptySearchTerms_withDifferentSearchTermValues_doNotMatch() {
        st1.put(KEY, SearchTerm.newAuditSearchTerm(KEY, VALUE));
        st2.put(KEY, SearchTerm.newAuditSearchTerm(KEY, "valueX"));
        assertInequalityBetween(st1, st2, 118234894, -817550684);
    }

    @SuppressWarnings({ "unlikely-arg-type", "EqualsWithItself", "ConstantConditions",
        "EqualsBetweenInconvertibleTypes" })
    @Test
    public void compareWithNullSelfOrDifferentClass() {
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals("")).isFalse();
    }

}
