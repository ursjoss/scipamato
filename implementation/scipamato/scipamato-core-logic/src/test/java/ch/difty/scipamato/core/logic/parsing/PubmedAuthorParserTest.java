package ch.difty.scipamato.core.logic.parsing;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.TestUtils;

public class PubmedAuthorParserTest {

    private PubmedAuthorParser p;

    @Test
    public void degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter(() -> new PubmedAuthorParser(null), "authorsString");
    }

    @Test
    public void canReturnOriginalAuthorsString() {
        String authorsString = "Bond J.";
        p = new PubmedAuthorParser(authorsString);
        assertThat(p.getAuthorsString()).isEqualTo(authorsString);
    }

    private void assertFirstAuthorOf(String input, String expected) {
        p = new PubmedAuthorParser(input);
        assertThat(p
            .getFirstAuthor()
            .orElse("n.a.")).isEqualTo(expected);
    }

    @Test
    public void canReturnFirstAuthor_withSingleAuthorOnly() {
        // proper format
        assertFirstAuthorOf("Bond J.", "Bond");
        // unclean format
        assertFirstAuthorOf("Bond   J.", "Bond");
        assertFirstAuthorOf("Bond J", "Bond");
        assertFirstAuthorOf("Bond ", "Bond");
        assertFirstAuthorOf("  Bond ", "Bond");
        assertFirstAuthorOf("  Bond.", "Bond");
    }

    @Test
    public void canReturnFirstAuthor_withMultipleAuthors() {
        // proper format
        assertFirstAuthorOf(
            "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.",
            "Turner");
    }

    @Test
    public void canReturnFirstAuthor_withMultipleLastNames() {
        assertFirstAuthorOf("Lloyd Webber A.", "Lloyd Webber");
        assertFirstAuthorOf("Lloyd Webber A", "Lloyd Webber");
        assertFirstAuthorOf("Lloyd   Webber   Andrew", "Lloyd Webber");
    }

    @Test
    public void canParseNameWithCardinality() {
        p = new PubmedAuthorParser("Ln FN 1st, Ln FN 2nd, Ln FN 3rd, Ln FN 4th, Ln FN 5th, Ln FN 100th, Ln FN.");
        assertThat(p
            .getFirstAuthor()
            .orElse("n.a.")).isEqualTo("Ln");
        assertThat(p
            .getAuthors()
            .stream()
            .map(Author::getLastName)
            .toArray()).containsOnly("Ln");
        assertThat(p
            .getAuthors()
            .stream()
            .map(Author::getFirstName)
            .toArray()).containsExactly("FN 1st", "FN 2nd", "FN 3rd", "FN 4th", "FN 5th", "FN 100th", "FN");
    }

    @Test
    public void canReturnFirstAuthor_evenWhenCardinalityStandsAfterFirstName() {
        assertFirstAuthorOf("Pope CA 3rd, Lloyd Webber A.", "Pope");
    }

    @Test
    public void canReturnFirstAuthor_evenWhenNameContainsJunior() {
        assertFirstAuthorOf("Cox LA Jr.", "Cox");
    }

    @Test
    public void canReturnFirstAuthor_letsNotBeFooledByInitialsLookingLikeJunior() {
        assertFirstAuthorOf("Cox JR.", "Cox");
    }

    @Test
    public void cannotProperlyReturnFirstAuthor_withInitialsAndCapitalJR() {
        // this is probably not a valid case, but let's state it explicitly here
        assertFirstAuthorOf("Cox LA JR.", "Cox LA");
    }

    @Test
    public void canReturnAuthors() {
        p = new PubmedAuthorParser(
            "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Krewsky D, Beckermann BS, Samet JM.");
        assertThat(p
            .getAuthors()
            .stream()
            .map(Author::getLastName)
            .collect(toList())).containsExactly("Turner", "Cohen", "Jerret", "Gapstur", "Driver", "Krewsky",
            "Beckermann", "Samet");
        assertThat(p
            .getAuthors()
            .stream()
            .map(Author::getFirstName)
            .collect(toList())).containsExactly("MC", "A", "M", "SM", "WR", "D", "BS", "JM");
    }

    @Test
    public void canDoUmlaute() {
        p = new PubmedAuthorParser("Flückiger P, Bäni HU.");
        assertThat(p
            .getAuthors()
            .stream()
            .map(Author::getLastName)
            .collect(toList())).containsExactly("Flückiger", "Bäni");
        assertThat(p
            .getAuthors()
            .stream()
            .map(Author::getFirstName)
            .collect(toList())).containsExactly("P", "HU");
    }

}
