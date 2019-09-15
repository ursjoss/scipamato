package ch.difty.scipamato.core.entity.search

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

private const val KEY = "key"
private const val VALUE = "value"


internal class AuditSearchTermsTest {

    private val st1 = AuditSearchTerms()
    private val st2 = AuditSearchTerms()

    @Test
    fun compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1)

        st1[KEY] = SearchTerm.newAuditSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newAuditSearchTerm(KEY, VALUE)
    }

    @Test
    fun compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1[KEY] = SearchTerm.newAuditSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newAuditSearchTerm(KEY, VALUE)
        assertEqualityBetween(st1, st2, 118234894)
    }

    @Test
    fun compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1["key1"] = SearchTerm.newAuditSearchTerm("key1", VALUE)
        st2["key1"] = SearchTerm.newAuditSearchTerm("key1", VALUE)
        st1["key2"] = SearchTerm.newAuditSearchTerm("key2", "value2")
        st2["key2"] = SearchTerm.newAuditSearchTerm("key2", "value2")
        assertEqualityBetween(st1, st2, 266203500)
    }

    private fun assertEqualityBetween(st1: AuditSearchTerms, st2: AuditSearchTerms, hashValue: Int) {
        assertThat(st1 == st2).isTrue()
        assertThat(st2 == st1).isTrue()
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode())
        assertThat(st1.hashCode()).isEqualTo(hashValue)
    }

    @Test
    fun compareEmptySearchTerms_withDifferentSearchTerms_doNotMatch() {
        st1[KEY] = SearchTerm.newAuditSearchTerm(KEY, VALUE)
        assertInequalityBetween(st1, st2, 118234894, 1)
    }

    private fun assertInequalityBetween(st1: AuditSearchTerms, st2: AuditSearchTerms, hashValue1: Int, hashValue2: Int) {
        assertThat(st1 == st2).isFalse()
        assertThat(st2 == st1).isFalse()
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode())
        assertThat(st1.hashCode()).isEqualTo(hashValue1)
        assertThat(st2.hashCode()).isEqualTo(hashValue2)
    }

    @Test
    fun compareEmptySearchTerms_withDifferentSearchTermValues_doNotMatch() {
        st1[KEY] = SearchTerm.newAuditSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newAuditSearchTerm(KEY, "valueX")
        assertInequalityBetween(st1, st2, 118234894, -817550684)
    }

    @Test
    fun compareWithNullSelfOrDifferentClass() {
        assertThat(st1 == null).isFalse()
        assertThat(st1 == st1).isTrue()
    }

}
