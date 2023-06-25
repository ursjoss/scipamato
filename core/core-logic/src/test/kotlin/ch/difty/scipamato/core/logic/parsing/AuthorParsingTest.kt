@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.parsing

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldStartWith
import org.junit.jupiter.api.Test

internal class PubmedAuthorParserTest {

    private lateinit var p: PubmedAuthorParser

    @Test
    fun withNoAuthor_firstAuthorIsNull() {
        p = PubmedAuthorParser("")
        p.firstAuthor.shouldBeNull()
    }

    @Test
    fun canReturnOriginalAuthorsString() {
        val authorsString = "Bond J."
        p = PubmedAuthorParser(authorsString)
        p.authorsString shouldBeEqualTo authorsString
    }

    private fun assertFirstAuthorOf(input: String, expected: String) {
        p = PubmedAuthorParser(input)
        p.firstAuthor shouldBeEqualTo expected
    }

    @Test
    fun canReturnFirstAuthor_withSingleAuthorOnly() {
        // proper format
        assertFirstAuthorOf("Bond J.", "Bond")
        // unclean format
        assertFirstAuthorOf("Bond   J.", "Bond")
        assertFirstAuthorOf("Bond J", "Bond")
        assertFirstAuthorOf("Bond ", "Bond")
        assertFirstAuthorOf("  Bond ", "Bond")
        assertFirstAuthorOf("  Bond.", "Bond")
    }

    @Test
    fun canReturnFirstAuthor_withMultipleAuthors() {
        // proper format
        assertFirstAuthorOf(
            """Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D
                |, Beckermann BS, Samet JM.""".trimMargin(),
            "Turner"
        )
    }

    @Test
    fun canReturnFirstAuthor_withMultipleLastNames() {
        assertFirstAuthorOf("Lloyd Webber A.", "Lloyd Webber")
        assertFirstAuthorOf("Lloyd Webber A", "Lloyd Webber")
        assertFirstAuthorOf("Lloyd   Webber   Andrew", "Lloyd Webber")
    }

    @Test
    fun canParseNameWithCardinality() {
        p = PubmedAuthorParser("Ln FN 1st, Ln FN 2nd, Ln FN 3rd, Ln FN 4th, Ln FN 5th, Ln FN 100th, Ln FN.")
        p.firstAuthor shouldBeEqualTo "Ln"
        p.authors.map { it.lastName } shouldContain "Ln"
        p.authors.map { it.firstName } shouldContainSame listOf(
            "FN 1st", "FN 2nd", "FN 3rd", "FN 4th", "FN 5th", "FN 100th", "FN"
        )
    }

    @Test
    fun canReturnFirstAuthor_evenWhenCardinalityStandsAfterFirstName() {
        assertFirstAuthorOf("Pope CA 3rd, Lloyd Webber A.", "Pope")
    }

    @Test
    fun canReturnFirstAuthor_evenWhenNameContainsJunior() {
        assertFirstAuthorOf("Cox LA Jr.", "Cox")
    }

    @Test
    fun canReturnFirstAuthor_letsNotBeFooledByInitialsLookingLikeJunior() {
        assertFirstAuthorOf("Cox JR.", "Cox")
    }

    @Test
    fun cannotProperlyReturnFirstAuthor_withInitialsAndCapitalJR() {
        // this is probably not a valid case, but let's state it explicitly here
        assertFirstAuthorOf("Cox LA JR.", "Cox LA")
    }

    @Test
    fun canReturnAuthors() {
        p = PubmedAuthorParser(
            "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Krewsky D, Beckermann BS, Samet JM."
        )
        p.authors.map { it.lastName } shouldContainSame listOf(
            "Turner", "Cohen", "Jerret", "Gapstur", "Driver", "Krewsky", "Beckermann", "Samet"
        )
        p.authors.map { it.firstName } shouldContainSame listOf("MC", "A", "M", "SM", "WR", "D", "BS", "JM")
    }

    @Test
    fun canDoUmlaute() {
        p = PubmedAuthorParser("Flückiger P, Bäni HU.")
        p.authors.map { it.lastName } shouldContainSame listOf("Flückiger", "Bäni")
        p.authors.map { it.firstName } shouldContainSame listOf("P", "HU")
    }

    @Test
    fun canParsePmId35469927() {
        val authors = "Markozannes G, Pantavou K, Rizos EC, Sindosi OΑ, Tagkas C, Seyfried M, " +
            "Saldanha IJ, Hatzianastassiou N, Nikolopoulos GK, Ntzani E."
        p = PubmedAuthorParser(authors)
        p.authors.map { it.lastName } shouldContainSame listOf(
            "Markozannes", "Pantavou", "Rizos", "Sindosi", "Tagkas", "Seyfried",
            "Saldanha", "Hatzianastassiou", "Nikolopoulos", "Ntzani"
        )
    }

    @Test
    fun canParsePmId35469927_resulting_in_validA() {
        val authorsStringPmId35469927 = "Markozannes G, Pantavou K, Rizos EC, Sindosi OΑ, Tagkas C, " +
            "Seyfried M, Saldanha IJ, Hatzianastassiou N, Nikolopoulos GK, Ntzani E."

        // Ensure the fourth author has a capital greek letter alpha in it (visually nearly identical to "A")
        val fourthAuthor = authorsStringPmId35469927.split(", ")[3]
        fourthAuthor shouldStartWith "Sindosi"
        val capitalGreekAlpha = Char(913) // Capital Greek Letter Alpha
        fourthAuthor shouldContain capitalGreekAlpha

        p = PubmedAuthorParser(authorsStringPmId35469927)

        p.authorsString shouldNotContain capitalGreekAlpha
        p.authorsString shouldContain Char(65) // Regular Capital A
    }

    @Test
    fun replacesSeveralGreekLetters_with_visuallySimilarLetters() {
        val wGreeks = "ΑΒΕΖΗΙΚΜΝΟΡΤΥΧ Α."
        val woGreek = "ABEZHIKMNOPTYX A."

        assertLettersAreNotSame(wGreeks, woGreek)
        @Suppress("KotlinConstantConditions")
        (wGreeks == woGreek).shouldBeFalse()

        p = PubmedAuthorParser(wGreeks)
        p.authorsString shouldBeEqualTo woGreek

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

internal class AuthorParserFactoryTest {

    @Test
    fun cratingParser_withNoSetting_usesDefaultAuthorParser() {
        val parser = AuthorParserFactory.create(AuthorParserStrategy.PUBMED).createParser("Turner MC.")
        parser shouldBeInstanceOf PubmedAuthorParser::class
    }
}
