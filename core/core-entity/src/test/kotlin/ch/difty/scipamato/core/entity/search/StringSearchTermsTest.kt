package ch.difty.scipamato.core.entity.search

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeEqualTo
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
        (st1 == st2).shouldBeTrue()
        (st2 == st1).shouldBeTrue()
        st1.hashCode() shouldBeEqualTo st2.hashCode()
        st1.hashCode() shouldBeEqualTo hashValue
    }

    @Test
    fun compareEmptySearchTerms_withDifferentSearchTerms_dontMatch() {
        st1[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        assertInequalityBetween(st1, st2, 118234894, 1)
    }

    @Suppress("SameParameterValue")
    private fun assertInequalityBetween(
        st1: StringSearchTerms,
        st2: StringSearchTerms,
        hashValue1: Int,
        hashValue2: Int
    ) {
        (st1 == st2).shouldBeFalse()
        (st2 == st1).shouldBeFalse()
        st1.hashCode() shouldNotBeEqualTo st2.hashCode()
        st1.hashCode() shouldBeEqualTo hashValue1
        st2.hashCode() shouldBeEqualTo hashValue2
    }

    @Test
    fun compareEmptySearchTerms_withDifferentSearchTermValues_dontMatch() {
        st1[KEY] = SearchTerm.newStringSearchTerm(KEY, VALUE)
        st2[KEY] = SearchTerm.newStringSearchTerm(KEY, "valueX")
        assertInequalityBetween(st1, st2, 118234894, -817550684)
    }

    @Test
    fun compareWithNullSelfOrDifferentClass() {
        (st1 == st1).shouldBeTrue()
    }

    companion object {
        private const val KEY = "key"
        private const val VALUE = "value"
    }
}
