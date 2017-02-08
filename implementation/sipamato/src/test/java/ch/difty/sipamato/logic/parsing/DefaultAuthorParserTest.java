package ch.difty.sipamato.logic.parsing;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;

public class DefaultAuthorParserTest {

    private DefaultAuthorParser p;

    @Test
    public void degenerateConstruction() {
        try {
            new DefaultAuthorParser(null);
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("authorsString must not be null.");
        }
    }

    @Test
    public void canReturnOriginalAuthorsString() {
        String authorsString = "Bond J.";
        p = new DefaultAuthorParser(authorsString);
        assertThat(p.getAuthorsString()).isEqualTo(authorsString);
    }

    private void assertFirstAuthorOf(String input, String expected) {
        p = new DefaultAuthorParser(input);
        assertThat(p.getFirstAuthor().orElse("n.a.")).isEqualTo(expected);
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
        assertFirstAuthorOf("Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.", "Turner");
    }

    @Test
    public void canReturnFirstAuthor_withMultipleLastNames() {
        assertFirstAuthorOf("Lloyd Webber A.", "Lloyd Webber");
        assertFirstAuthorOf("Lloyd Webber A", "Lloyd Webber");
        assertFirstAuthorOf("Lloyd   Webber   Andrew", "Lloyd Webber");
    }

    @Test
    public void canParseNameWithCardinality() {
        p = new DefaultAuthorParser("Ln FN 1st, Ln FN 2nd, Ln FN 3rd, Ln FN 4th, Ln FN 5th, Ln FN 100th, Ln FN.");
        assertThat(p.getFirstAuthor().orElse("n.a.")).isEqualTo("Ln");
        assertThat(p.getAuthors().map(Author::getLastName).toArray()).containsOnly("Ln");
        assertThat(p.getAuthors().map(Author::getFirstName).toArray()).containsExactly("FN 1st", "FN 2nd", "FN 3rd", "FN 4th", "FN 5th", "FN 100th", "FN");
    }

    @Test
    public void canReturnFirstAuthor_evenWhenCardinalityStandsAfterFirstName() {
        assertFirstAuthorOf("Pope CA 3rd, Lloyd Webber A.", "Pope");
    }

    @Test
    public void canReturnAuthors() {
        p = new DefaultAuthorParser("Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Krewsky D, Beckermann BS, Samet JM.");
        assertThat(p.getAuthors().map(Author::getLastName).collect(toList())).containsExactly("Turner", "Cohen", "Jerret", "Gapstur", "Driver", "Krewsky", "Beckermann", "Samet");
        assertThat(p.getAuthors().map(Author::getFirstName).collect(toList())).containsExactly("MC", "A", "M", "SM", "WR", "D", "BS", "JM");
    }

    @Test
    public void canDoUmlaute() {
        p = new DefaultAuthorParser("Flückiger P, Bäni HU.");
        assertThat(p.getAuthors().map(Author::getLastName).collect(toList())).containsExactly("Flückiger", "Bäni");
        assertThat(p.getAuthors().map(Author::getFirstName).collect(toList())).containsExactly("P", "HU");
    }

}
