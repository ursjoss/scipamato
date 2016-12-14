package ch.difty.sipamato.entity.filter;

import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTOPENLEFT;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTOPENLEFTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTOPENLEFTRIGHT;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTOPENLEFTRIGHTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTOPENRIGHT;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTOPENRIGHTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTREGEX;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.NOTWORD;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.OPENLEFT;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.OPENLEFTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.OPENLEFTRIGHT;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.OPENLEFTRIGHTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.OPENRIGHT;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.OPENRIGHTQUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.QUOTED;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.RAW;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.REGEX;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.SOME;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.WHITESPACE;
import static ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType.WORD;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.filter.StringSearchTerm2.MatchType;
import ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType;

public class StringSearchTerm2Test {

    private static final String FIELD_NAME = "fooField";

    private StringSearchTerm2 st;

    private void assertSingleToken(TokenType tt, String rawData, String data, boolean negate) {
        assertThat(st.getFieldName()).isEqualTo(FIELD_NAME);
        assertThat(st.getTokens()).hasSize(1);
        assertToken(0, tt, rawData, data, negate);
    }

    private void assertToken(int idx, TokenType tt, String rawData, String data, boolean negate) {
        assertThat(st.getTokens().get(idx).rawData).isEqualTo(rawData);
        assertThat(st.getTokens().get(idx).sqlData).isEqualTo(data);
        assertThat(st.getTokens().get(idx).type).isEqualTo(tt);
        assertThat(st.getTokens().get(idx).negate).isEqualTo(negate);
    }

    @Test
    public void lexingRegex_withValidRegex_withoutWhitespaceBeforeOrAfter_matchesRegexWithoutMeta() {
        st = new StringSearchTerm2(FIELD_NAME, "s/bar/");
        assertSingleToken(TokenType.REGEX, "bar", "bar", false);
    }

    @Test
    public void lexingRegex_withValidRegex_negated_withoutWhitespaceBeforeOrAfter_matchesRegexNegatedWithoutMeta() {
        st = new StringSearchTerm2(FIELD_NAME, "-s/bar/");
        assertSingleToken(TokenType.NOTREGEX, "bar", "bar", true);
    }

    @Test
    public void lexingRegex_withValidRegex_withWhitespaceBeforeAndAfter_matchesTrimmedRegexWithoutMeta() {
        st = new StringSearchTerm2(FIELD_NAME, "  s/foo bar/    " + "\n  ");
        assertSingleToken(TokenType.REGEX, "foo bar", "foo bar", false);
    }

    @Test
    public void lexingRegex_withInvalidSeparator_matchesWord() {
        st = new StringSearchTerm2(FIELD_NAME, "s|bar|");
        assertToken(0, TokenType.WORD, "s", "s", false);
        assertToken(1, TokenType.WORD, "bar", "bar", false);
    }

    @Test
    public void lexingQuotedString_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "\"hi there\"");
        assertSingleToken(TokenType.QUOTED, "hi there", "hi there", false);
    }

    @Test
    public void lexingOpenRight_withQuotedString_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "\"hi the*\"");
        assertSingleToken(TokenType.OPENRIGHTQUOTED, "hi the", "hi the%", false);
    }

    @Test
    public void lexingOpenRight_withQuotedStringNegated_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "-\"hi the*\"");
        assertSingleToken(TokenType.NOTOPENRIGHTQUOTED, "hi the", "hi the%", true);
    }

    @Test
    public void lexingOpenRight_withUnQuotedString_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "hi ho*");
        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "hi", "hi", false);
        assertToken(1, TokenType.OPENRIGHT, "ho", "ho%", false);
    }

    @Test
    public void lexingOpenLeft_withQuotedString_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "\"*hi the\"");
        assertSingleToken(TokenType.OPENLEFTQUOTED, "hi the", "%hi the", false);
    }

    @Test
    public void lexingOpenLeft_withUnQuotedString_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "*hi lo");
        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.OPENLEFT, "hi", "%hi", false);
        assertToken(1, TokenType.WORD, "lo", "lo", false);
    }

    @Test
    public void lexingOpenLeftRight_withQuotedString_matchesContentWithoutQuotes() {
        st = new StringSearchTerm2(FIELD_NAME, "\"*abc*\" foo *def* ");
        assertThat(st.getTokens()).hasSize(3);
        assertToken(0, TokenType.OPENLEFTRIGHTQUOTED, "abc", "%abc%", false);
        assertToken(1, TokenType.WORD, "foo", "foo", false);
        assertToken(2, TokenType.OPENLEFTRIGHT, "def", "%def%", false);
    }

    @Test
    public void lexingCombination_matchesQuotedTrimmedContent() {
        st = new StringSearchTerm2(FIELD_NAME, " foo \"hi there\"   bar ");

        assertThat(st.getFieldName()).isEqualTo(FIELD_NAME);
        assertThat(st.getTokens()).hasSize(3);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.QUOTED, "hi there", "hi there", false);
        assertToken(2, TokenType.WORD, "bar", "bar", false);
    }

    @Test
    public void lexingNot_() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -bar");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTWORD, "bar", "bar", true);
    }

    @Test
    public void lexingNotQuoted() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -\"bar baz\"");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTQUOTED, "bar baz", "bar baz", true);
    }

    @Test
    public void lexingNotOpenLeftQuoted() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -\"*bar baz\"");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTOPENLEFTQUOTED, "bar baz", "%bar baz", true);
    }

    @Test
    public void lexingNotOpenRightQuoted() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -\"bar baz*\"");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTOPENRIGHTQUOTED, "bar baz", "bar baz%", true);
    }

    @Test
    public void lexingNotOpenLeftRightQuoted() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -\"*bar baz*\"");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTOPENLEFTRIGHTQUOTED, "bar baz", "%bar baz%", true);
    }

    @Test
    public void lexingNot_withNotOpenRight_() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -bar*");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTOPENRIGHT, "bar", "bar%", true);
    }

    @Test
    public void lexingNot_withNotOpenLeft() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -*bar");

        assertThat(st.getTokens()).hasSize(2);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTOPENLEFT, "bar", "%bar", true);
    }

    @Test
    public void lexingNot_withNotOpenLeftRight() {
        st = new StringSearchTerm2(FIELD_NAME, "foo -*bar* baz ");

        assertThat(st.getTokens()).hasSize(3);
        assertToken(0, TokenType.WORD, "foo", "foo", false);
        assertToken(1, TokenType.NOTOPENLEFTRIGHT, "bar", "%bar%", true);
        assertToken(2, TokenType.WORD, "baz", "baz", false);
    }

    @Test
    public void lexingSome() {
        st = new StringSearchTerm2(FIELD_NAME, ">\"\"");

        assertThat(st.getTokens()).hasSize(1);
        assertToken(0, TokenType.SOME, ">\"\"", ">\"\"", false);
    }

    @Test
    public void assertTokenTypes() {
        assertThat(StringSearchTerm2.TokenType.values()).containsExactly(NOTREGEX, REGEX, WHITESPACE, SOME, NOTOPENLEFTRIGHTQUOTED, OPENLEFTRIGHTQUOTED, NOTOPENLEFTRIGHT, OPENLEFTRIGHT,
                NOTOPENRIGHTQUOTED, OPENRIGHTQUOTED, NOTOPENRIGHT, OPENRIGHT, NOTOPENLEFTQUOTED, OPENLEFTQUOTED, NOTOPENLEFT, OPENLEFT, NOTQUOTED, QUOTED, NOTWORD, WORD, RAW);
    }

    @Test
    public void assertTokenTypes_contains() {
        assertThat(TokenType.byMatchType(MatchType.CONTAINS)).containsExactly(NOTWORD, WORD);
    }

    @Test
    public void assertTokenTypes_equal() {
        assertThat(TokenType.byMatchType(MatchType.EQUALS)).containsExactly(NOTQUOTED, QUOTED);
    }

    @Test
    public void assertTokenTypes_like() {
        assertThat(TokenType.byMatchType(MatchType.LIKE)).containsExactly(NOTOPENLEFTRIGHTQUOTED, OPENLEFTRIGHTQUOTED, NOTOPENLEFTRIGHT, OPENLEFTRIGHT, NOTOPENRIGHTQUOTED, OPENRIGHTQUOTED,
                NOTOPENRIGHT, OPENRIGHT, NOTOPENLEFTQUOTED, OPENLEFTQUOTED, NOTOPENLEFT, OPENLEFT);
    }

    @Test
    public void assertTokenTypes_regex() {
        assertThat(TokenType.byMatchType(MatchType.REGEX)).containsExactly(NOTREGEX, REGEX);
    }

    @Test
    public void assertTokenTypes_length() {
        assertThat(TokenType.byMatchType(MatchType.LENGTH)).containsExactly(SOME);
    }

    @Test
    public void assertTokenTypes_none() {
        assertThat(TokenType.byMatchType(MatchType.NONE)).containsExactly(WHITESPACE, RAW);
    }
}
