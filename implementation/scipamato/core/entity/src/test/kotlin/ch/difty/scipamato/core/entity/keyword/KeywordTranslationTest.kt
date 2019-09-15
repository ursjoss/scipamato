package ch.difty.scipamato.core.entity.keyword

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class KeywordTranslationTest {

    @Test
    fun displayValue() {
        val ntt = KeywordTranslation(1, "de", "topic1", 1)
        assertThat(ntt.displayValue).isEqualTo("de: topic1")
    }
}