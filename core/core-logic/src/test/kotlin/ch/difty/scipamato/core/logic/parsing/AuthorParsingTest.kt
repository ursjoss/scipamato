@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.parsing

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
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
}

internal class AuthorParserFactoryTest {

    @Test
    fun cratingParser_withNoSetting_usesDefaultAuthorParser() {
        val parser = AuthorParserFactory.create(AuthorParserStrategy.PUBMED).createParser("Turner MC.")
        parser shouldBeInstanceOf PubmedAuthorParser::class
    }
}
