package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AuditSearchTermsTest {

    private final AuditSearchTerms st1 = new AuditSearchTerms();
    private final AuditSearchTerms st2 = new AuditSearchTerms();

    @Test
    public void compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1);

        st1.put("key", new AuditSearchTerm("key", "value"));
        st2.put("key", new AuditSearchTerm("key", "value"));
    }

    @Test
    public void compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1.put("key", new AuditSearchTerm("key", "value"));
        st2.put("key", new AuditSearchTerm("key", "value"));
        assertEqualityBetween(st1, st2, 115262162);
    }

    @Test
    public void compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1.put("key1", new AuditSearchTerm("key1", "value"));
        st2.put("key1", new AuditSearchTerm("key1", "value"));
        st1.put("key2", new AuditSearchTerm("key2", "value2"));
        st2.put("key2", new AuditSearchTerm("key2", "value2"));
        assertEqualityBetween(st1, st2, 1614595920);
    }

    private void assertEqualityBetween(AuditSearchTerms st1, AuditSearchTerms st2, int hashValue) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTerms_dontMatch() {
        st1.put("key", new AuditSearchTerm("key", "value"));
        assertInequalityBetween(st1, st2, 115262162, 1);
    }

    private void assertInequalityBetween(AuditSearchTerms st1, AuditSearchTerms st2, int hashValue1, int hashValue2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue1);
        assertThat(st2.hashCode()).isEqualTo(hashValue2);
    }

    @Test
    public void compareEmptySearchTerms_withDiffernetSearchTermValues_dontMatch() {
        st1.put("key", new AuditSearchTerm("key", "value"));
        st2.put("key", new AuditSearchTerm("key", "valueX"));
        assertInequalityBetween(st1, st2, 115262162, -820523416);
    }

    @Test
    public void compareWithNullSelfOrDifferentClass() {
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals("")).isFalse();
    }

}
