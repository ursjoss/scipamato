@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.common.TestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PubmedAuthorParserTest {

    private lateinit var p: PubmedAuthorParser

    @Test
    fun degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter({ PubmedAuthorParser(null) }, "authorsString")
    }

    @Test
    fun canReturnOriginalAuthorsString() {
        val authorsString = "Bond J."
        p = PubmedAuthorParser(authorsString)
        assertThat(p.authorsString).isEqualTo(authorsString)
    }

    private fun assertFirstAuthorOf(input: String, expected: String) {
        p = PubmedAuthorParser(input)
        assertThat(p.firstAuthor.orElse("n.a.")).isEqualTo(expected)
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
        assertFirstAuthorOf("Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.", "Turner")
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
        assertThat(p.firstAuthor.orElse("n.a.")).isEqualTo("Ln")
        assertThat(p.authors.map { it.lastName }).containsOnly("Ln")
        assertThat(p.authors.map { it.firstName }).containsExactly("FN 1st", "FN 2nd", "FN 3rd", "FN 4th", "FN 5th", "FN 100th", "FN")
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
        p = PubmedAuthorParser("Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Krewsky D, Beckermann BS, Samet JM.")
        assertThat(p.authors.map { it.lastName }).containsExactly("Turner", "Cohen", "Jerret", "Gapstur", "Driver", "Krewsky", "Beckermann", "Samet")
        assertThat(p.authors.map { it.firstName }).containsExactly("MC", "A", "M", "SM", "WR", "D", "BS", "JM")
    }

    @Test
    fun canDoUmlaute() {
        p = PubmedAuthorParser("Flückiger P, Bäni HU.")
        assertThat(p.authors.map { it.lastName }).containsExactly("Flückiger", "Bäni")
        assertThat(p.authors.map { it.firstName }).containsExactly("P", "HU")
    }

}
