package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.EMPTY
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTOPENLEFT
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTOPENLEFTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTOPENLEFTRIGHT
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTOPENLEFTRIGHTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTOPENRIGHT
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTOPENRIGHTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTREGEX
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.NOTWORD
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.OPENLEFT
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.OPENLEFTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.OPENLEFTRIGHT
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.OPENLEFTRIGHTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.OPENRIGHT
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.OPENRIGHTQUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.QUOTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.RAW
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.REGEX
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.SOME
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.UNSUPPORTED
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.WHITESPACE
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.WORD
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.byMatchType
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType.values
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val FIELD_NAME = "fooField"

internal class StringSearchTermTest {

    private fun assertSingleToken(st: StringSearchTerm, tt: TokenType, rawData: String, data: String, negate: Boolean) {
        assertThat(st.fieldName).isEqualTo(FIELD_NAME)
        assertThat(st.tokens).hasSize(1)
        assertToken(st, 0, tt, rawData, data, negate)
    }

    private fun assertToken(
        st: StringSearchTerm,
        idx: Int,
        tt: TokenType,
        rawData: String,
        data: String,
        negate: Boolean
    ) {
        assertThat(st.tokens[idx].rawData).isEqualTo(rawData)
        assertThat(st.tokens[idx].sqlData).isEqualTo(data)
        assertThat(st.tokens[idx].type).isEqualTo(tt)
        assertThat(st.tokens[idx].negate).isEqualTo(negate)
    }

    @Test
    fun lexingRegex_withValidRegex_withoutWhitespaceBeforeOrAfter_matchesRegexWithoutMeta() {
        val st = StringSearchTerm(FIELD_NAME, "s/bar/")
        assertSingleToken(st, REGEX, "bar", "bar", false)
    }

    @Test
    fun lexingRegex_withValidRegex_negated_withoutWhitespaceBeforeOrAfter_matchesRegexNegatedWithoutMeta() {
        val st = StringSearchTerm(FIELD_NAME, "-s/bar/")
        assertSingleToken(st, NOTREGEX, "bar", "bar", true)
    }

    @Test
    fun lexingRegex_withValidRegex_withWhitespaceBeforeAndAfter_matchesTrimmedRegexWithoutMeta() {
        val st = StringSearchTerm(FIELD_NAME, "  s/foo bar/    " + "\n  ")
        assertSingleToken(st, REGEX, "foo bar", "foo bar", false)
    }

    @Test
    fun lexingRegex_withInvalidSeparator_matchesWord() {
        val st = StringSearchTerm(FIELD_NAME, "s|bar|")
        assertToken(st, 0, WORD, "s", "s", false)
        assertToken(st, 1, WORD, "bar", "bar", false)
    }

    @Test
    fun lexingQuotedString_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "\"hi there\"")
        assertSingleToken(st, QUOTED, "hi there", "hi there", false)
    }

    @Test
    fun lexingOpenRight_withQuotedString_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "\"hi the*\"")
        assertSingleToken(st, OPENRIGHTQUOTED, "hi the", "hi the%", false)
    }

    @Test
    fun lexingOpenRight_withQuotedStringNegated_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "-\"hi the*\"")
        assertSingleToken(st, NOTOPENRIGHTQUOTED, "hi the", "hi the%", true)
    }

    @Test
    fun lexingOpenRight_withUnQuotedString_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "hi ho*")
        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "hi", "hi", false)
        assertToken(st, 1, OPENRIGHT, "ho", "ho%", false)
    }

    @Test
    fun lexingOpenLeft_withQuotedString_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "\"*hi the\"")
        assertSingleToken(st, OPENLEFTQUOTED, "hi the", "%hi the", false)
    }

    @Test
    fun lexingOpenLeft_withUnQuotedString_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "*hi lo")
        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, OPENLEFT, "hi", "%hi", false)
        assertToken(st, 1, WORD, "lo", "lo", false)
    }

    @Test
    fun lexingOpenLeftRight_withQuotedString_matchesContentWithoutQuotes() {
        val st = StringSearchTerm(FIELD_NAME, "\"*abc*\" foo *def* ")
        assertThat(st.tokens).hasSize(3)
        assertToken(st, 0, OPENLEFTRIGHTQUOTED, "abc", "%abc%", false)
        assertToken(st, 1, WORD, "foo", "foo", false)
        assertToken(st, 2, OPENLEFTRIGHT, "def", "%def%", false)
    }

    @Test
    fun lexingCombination_matchesQuotedTrimmedContent() {
        val st = StringSearchTerm(FIELD_NAME, " foo \"hi there\"   bar ")

        assertThat(st.fieldName).isEqualTo(FIELD_NAME)
        assertThat(st.tokens).hasSize(3)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, QUOTED, "hi there", "hi there", false)
        assertToken(st, 2, WORD, "bar", "bar", false)
    }

    @Test
    fun lexingNot_() {
        val st = StringSearchTerm(FIELD_NAME, "foo -bar")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTWORD, "bar", "bar", true)
    }

    @Test
    fun lexingNotQuoted() {
        val st = StringSearchTerm(FIELD_NAME, "foo -\"bar baz\"")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTQUOTED, "bar baz", "bar baz", true)
    }

    @Test
    fun lexingNotOpenLeftQuoted() {
        val st = StringSearchTerm(FIELD_NAME, "foo -\"*bar baz\"")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTOPENLEFTQUOTED, "bar baz", "%bar baz", true)
    }

    @Test
    fun lexingNotOpenRightQuoted() {
        val st = StringSearchTerm(FIELD_NAME, "foo -\"bar baz*\"")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTOPENRIGHTQUOTED, "bar baz", "bar baz%", true)
    }

    @Test
    fun lexingNotOpenLeftRightQuoted() {
        val st = StringSearchTerm(FIELD_NAME, "foo -\"*bar baz*\"")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTOPENLEFTRIGHTQUOTED, "bar baz", "%bar baz%", true)
    }

    @Test
    fun lexingNot_withNotOpenRight_() {
        val st = StringSearchTerm(FIELD_NAME, "foo -bar*")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTOPENRIGHT, "bar", "bar%", true)
    }

    @Test
    fun lexingNot_withNotOpenLeft() {
        val st = StringSearchTerm(FIELD_NAME, "foo -*bar")

        assertThat(st.tokens).hasSize(2)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTOPENLEFT, "bar", "%bar", true)
    }

    @Test
    fun lexingNot_withNotOpenLeftRight() {
        val st = StringSearchTerm(FIELD_NAME, "foo -*bar* baz ")

        assertThat(st.tokens).hasSize(3)
        assertToken(st, 0, WORD, "foo", "foo", false)
        assertToken(st, 1, NOTOPENLEFTRIGHT, "bar", "%bar%", true)
        assertToken(st, 2, WORD, "baz", "baz", false)
    }

    @Test
    fun lexingSome() {
        val st = StringSearchTerm(FIELD_NAME, ">\"\"")

        assertThat(st.tokens).hasSize(1)
        assertToken(st, 0, SOME, ">\"\"", ">\"\"", false)
    }

    @Test
    fun lexingEmpty() {
        val st = StringSearchTerm(FIELD_NAME, "=\"\"")

        assertThat(st.tokens).hasSize(1)
        assertToken(st, 0, EMPTY, "=\"\"", "=\"\"", true)
    }

    @Test
    fun assertTokenTypes() {
        assertThat(values()).containsExactly(
            NOTREGEX, REGEX, WHITESPACE, SOME, EMPTY,
            NOTOPENLEFTRIGHTQUOTED, OPENLEFTRIGHTQUOTED, NOTOPENLEFTRIGHT, OPENLEFTRIGHT, NOTOPENRIGHTQUOTED,
            OPENRIGHTQUOTED, NOTOPENRIGHT, OPENRIGHT, NOTOPENLEFTQUOTED, OPENLEFTQUOTED, NOTOPENLEFT, OPENLEFT,
            NOTQUOTED, QUOTED, NOTWORD, WORD, RAW, UNSUPPORTED
        )
    }

    @Test
    fun assertTokenTypes_contains() {
        assertThat(byMatchType(MatchType.CONTAINS)).containsExactly(NOTWORD, WORD)
    }

    @Test
    fun assertTokenTypes_equal() {
        assertThat(byMatchType(MatchType.EQUALS)).containsExactly(NOTQUOTED, QUOTED)
    }

    @Test
    fun assertTokenTypes_like() {
        assertThat(byMatchType(MatchType.LIKE)).containsExactly(
            NOTOPENLEFTRIGHTQUOTED, OPENLEFTRIGHTQUOTED,
            NOTOPENLEFTRIGHT, OPENLEFTRIGHT, NOTOPENRIGHTQUOTED, OPENRIGHTQUOTED, NOTOPENRIGHT, OPENRIGHT,
            NOTOPENLEFTQUOTED, OPENLEFTQUOTED, NOTOPENLEFT, OPENLEFT
        )
    }

    @Test
    fun assertTokenTypes_regex() {
        assertThat(byMatchType(MatchType.REGEX)).containsExactly(NOTREGEX, REGEX)
    }

    @Test
    fun assertTokenTypes_length() {
        assertThat(byMatchType(MatchType.LENGTH)).containsExactly(SOME, EMPTY)
    }

    @Test
    fun assertTokenTypes_none() {
        assertThat(byMatchType(MatchType.NONE)).containsExactly(WHITESPACE, RAW)
    }

    @Test
    fun lexingPm2dot5_shouldOnlyFindOneToken() {
        val st = StringSearchTerm(FIELD_NAME, "pm2.5")
        assertThat(st.tokens).hasSize(1)
        assertToken(st, 0, WORD, "pm2.5", "pm2.5", false)
    }

    @Test
    fun tokenToString_forUserField() {
        val st = StringSearchTerm(FIELD_NAME, "pm2.5")
        assertThat(st.tokens).hasSize(1)
        assertThat(st.tokens[0].toString()).isEqualTo("(WORD pm2.5)")
    }

    @Test
    fun differentInterpretationOfQuotedAndWord() {
        val st = StringSearchTerm(FIELD_NAME, "=\"foo\" \"foo\" foo =foo")
        assertThat(st.tokens).hasSize(4)
        assertThat(st.tokens[0].toString()).isEqualTo("(QUOTED foo)")
        assertThat(st.tokens[1].toString()).isEqualTo("(QUOTED foo)")
        assertThat(st.tokens[2].toString()).isEqualTo("(WORD foo)")
        assertThat(st.tokens[3].toString()).isEqualTo("(WORD foo)")
    }

    @Test
    fun lexingEmptyString_returnsRaw() {
        val st = StringSearchTerm(FIELD_NAME, "")
        assertSingleToken(st, RAW, "", "", false)
    }
}
