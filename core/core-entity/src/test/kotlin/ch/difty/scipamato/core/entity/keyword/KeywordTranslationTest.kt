package ch.difty.scipamato.core.entity.keyword

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class KeywordTranslationTest {

    @Test
    fun displayValue() {
        val ntt = KeywordTranslation(1, "de", "topic1", 1)
        ntt.displayValue shouldBeEqualTo "de: topic1"
    }
}
