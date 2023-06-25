package ch.difty.scipamato.core.pubmed

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldStartWith
import org.junit.jupiter.api.Test

class GreekLetterTranslatorTest {

    private val translator = GreekLetterTranslator

    @Test
    fun canParsePmId35469927_resulting_in_validA() {
        val authorsStringPmId35469927 = "Markozannes G, Pantavou K, Rizos EC, Sindosi OΑ, Tagkas C, " +
            "Seyfried M, Saldanha IJ, Hatzianastassiou N, Nikolopoulos GK, Ntzani E."

        // Ensure the fourth author has a capital greek letter alpha in it (visually nearly identical to "A")
        val fourthAuthor = authorsStringPmId35469927.split(", ")[3]
        fourthAuthor shouldStartWith "Sindosi"
        val capitalGreekAlpha = Char(913) // Capital Greek Letter Alpha
        fourthAuthor shouldContain capitalGreekAlpha

        val result = translator.replaceGreekLetters(authorsStringPmId35469927)

        result shouldNotContain capitalGreekAlpha
        result shouldContain Char(65) // Regular Capital A
    }

    @Test
    fun replacesSeveralGreekLetters_with_visuallySimilarLetters() {
        val wGreeks = "ΑΒΕΖΗΙΚΜΝΟΡΤΥΧ Α."
        val woGreek = "ABEZHIKMNOPTYX A."

        assertLettersAreNotSame(wGreeks, woGreek)
        @Suppress("KotlinConstantConditions")
        (wGreeks == woGreek).shouldBeFalse()

        translator.replaceGreekLetters(wGreeks) shouldBeEqualTo woGreek
    }

    @Suppress("SameParameterValue")
    private fun assertLettersAreNotSame(one: String, two: String) {
        val toIgnore = setOf(' ', '.')
        val first = one.filterNot { it in toIgnore }
        val second = two.filterNot { it in toIgnore }
        first.length shouldBeEqualTo second.length
        first.forEachIndexed { index, char -> char shouldNotBeEqualTo second[index] }
    }
}
