package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.filter.StringSearchTerm.MatchType;

public class StringSearchTermTest {

    private static final String FIELD_NAME = "fn";

    private StringSearchTerm st;

    private void assertTerm(MatchType type, String value, String raw) {
        assertThat(st.getSearchTermType()).isEqualTo(SearchTerm.SearchTermType.STRING);
        assertThat(st.getFieldName()).isEqualTo(FIELD_NAME);
        assertThat(st.getType()).isEqualTo(type);
        assertThat(st.getValue()).isEqualTo(value);
        assertThat(st.getRawSearchTerm()).isEqualTo(raw);
    }

    @Test
    public void contains() {
        final String raw = "foo";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "foo", raw);
    }

    @Test
    public void contains_minimal() {
        final String raw = "f";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "f", raw);
    }

    @Test
    public void contains_withAsterisks() {
        final String raw = "*foo*";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "foo", raw);
    }

    @Test
    public void contains_withAsterisks_minimal() {
        final String raw = "*f*";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "f", raw);
    }

    @Test
    public void contains_withAsterisksAndSpaces() {
        final String raw = "  *foo*  ";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "foo", raw);
    }

    @Test
    public void contains_withAsterisksAndSpacesBetweenAsteriskAndTerm_keepsSpace() {
        final String raw = "  * foo*  ";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, " foo", raw);
    }

    @Test
    public void contains_withAsterisksAndQuotes() {
        final String raw = "\"*foo*\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "foo", raw);
    }

    @Test
    public void contains_withAsterisksAndQuotesAndSpaces() {
        final String raw = "  \"* foo *\"  ";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, " foo ", raw);
    }

    @Test
    public void exactSearch() {
        final String raw = "\"foo\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, "foo", raw);
    }

    @Test
    public void exactSearch_minimal() {
        final String raw = "\"f\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, "f", raw);
    }

    @Test
    public void exactSearch_usingEquals() {
        final String raw = "=\"foo\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, "foo", raw);
    }

    @Test
    public void exactSearch_usingEqualsAndSpaces() {
        final String raw = "  =\"foo\" ";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, "foo", raw);
    }

    @Test
    public void startingWith() {
        final String raw = "foo*";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.STARTS_WITH, "foo", raw);
    }

    @Test
    public void startingWith_minimal() {
        final String raw = "f*";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.STARTS_WITH, "f", raw);
    }

    @Test
    public void statingWith_withQuotes() {
        final String raw = "\"foo*\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.STARTS_WITH, "foo", raw);
    }

    @Test
    public void statingWith_withQuotes_minimal() {
        final String raw = "\"f*\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.STARTS_WITH, "f", raw);
    }

    @Test
    public void startingWith_withQuotesAndSpaces() {
        final String raw = "  \" foo *\"    ";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.STARTS_WITH, " foo ", raw);
    }

    @Test
    public void endingWith() {
        final String raw = "*foo";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.ENDS_WITH, "foo", raw);
    }

    @Test
    public void endingWith_minimal() {
        final String raw = "*f";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.ENDS_WITH, "f", raw);
    }

    @Test
    public void endingWith_withQuotes() {
        final String raw = "\"*foo\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.ENDS_WITH, "foo", raw);
    }

    @Test
    public void endingWith_withQuotes_minimal() {
        final String raw = "\"*f\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.ENDS_WITH, "f", raw);
    }

    @Test
    public void endingWith_withQuotesAndSpaces() {
        final String raw = "  \"* foo \"    ";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.ENDS_WITH, " foo ", raw);
    }

    @Test
    public void oddCases_resultingInContains_missingEndQuote() {
        final String raw = "\"foo";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "\"foo", raw);
    }

    @Test
    public void oddCases_resultingInContains_missingEndQuoteStartingWithEquals() {
        final String raw = "=\"foo";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "=\"foo", raw);
    }

    @Test
    public void oddCases_resultingInContains_missingEndQuoteStartingWithQuoteAsterisk() {
        final String raw = "\"*foo";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "\"*foo", raw);
    }

    @Test
    public void oddCases_resultingInContains_missingEndQuoteStartingWithEqualsQuoteAsterisk() {
        final String raw = "=\"*foo";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "=\"*foo", raw);
    }

    @Test
    public void oddCases_resultingInContains_missingStartQuote() {
        final String raw = "foo\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "foo\"", raw);
    }

    @Test
    public void oddCases_resultingInContains_missingStartQuote_Star() {
        final String raw = "foo*\"";
        st = new StringSearchTerm(FIELD_NAME, raw);
        assertTerm(MatchType.CONTAINS, "foo*\"", raw);
    }

}
