package ch.difty.scipamato.core.entity.search

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class StringSearchTermsTest {

    private val st1 = StringSearchTerms()
    private val st2 = StringSearchTerms()

    @Test
    fun compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1)

        st1[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
    }

    @Test
    fun compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        assertEqualityBetween(st1, st2, 118234894)
    }

    @Test
    fun compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1["key1"] = SearchTerm.newStringSearchTerm("key1", VALUE)
        st2["key1"] = SearchTerm.newStringSearchTerm("key1", VALUE)
        st1["key2"] = SearchTerm.newStringSearchTerm("key2", "value2")
        st2["key2"] = SearchTerm.newStringSearchTerm("key2", "value2")
        assertEqualityBetween(st1, st2, 266203500)
    }

    private fun assertEqualityBetween(st1: StringSearchTerms, st2: StringSearchTerms, hashValue: Int) {
        assertThat(st1 == st2).isTrue()
        assertThat(st2 == st1).isTrue()
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode())
        assertThat(st1.hashCode()).isEqualTo(hashValue)
    }

    @Test
    fun compareEmptySearchTerms_withDifferentSearchTerms_dontMatch() {
        st1[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        assertInequalityBetween(st1, st2, 118234894, 1)
    }

    private fun assertInequalityBetween(st1: StringSearchTerms, st2: StringSearchTerms, hashValue1: Int, hashValue2: Int) {
        assertThat(st1 == st2).isFalse()
        assertThat(st2 == st1).isFalse()
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode())
        assertThat(st1.hashCode()).isEqualTo(hashValue1)
        assertThat(st2.hashCode()).isEqualTo(hashValue2)
    }

    @Test
    fun compareEmptySearchTerms_withDifferentSearchTermValues_dontMatch() {
        st1[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newStringSearchTerm(KEY, "valueX")
        assertInequalityBetween(st1, st2, 118234894, -817550684)
    }

    @Test
    fun compareWithNullSelfOrDifferentClass() {
        assertThat(st1 == null).isFalse()
        assertThat(st1 == st1).isTrue()
    }

    companion object {
        private val KEY = "key"
        private val VALUE = "value"
    }

}
