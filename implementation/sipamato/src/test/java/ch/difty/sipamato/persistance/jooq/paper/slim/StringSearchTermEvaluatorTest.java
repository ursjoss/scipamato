package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.StringSearchTerm2;
import ch.difty.sipamato.entity.filter.StringSearchTerm2.Token;
import ch.difty.sipamato.entity.filter.StringSearchTerm2.TokenType;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class StringSearchTermEvaluatorTest {

    private final StringSearchTermEvaluator e = new StringSearchTermEvaluator();

    private final List<Token> tokens = new ArrayList<>();
    @Mock
    private StringSearchTerm2 stMock;
    @Mock
    private Token tokenMock;

    @Test
    public void evaluating_withNullParameter_throws() {
        try {
            e.evaluate(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchTerm must not be null.");
        }
    }

    private void expectToken(TokenType type, String term) {
        when(stMock.getFieldName()).thenReturn("fieldX");
        tokens.add(new Token(type, term));
        when(stMock.getTokens()).thenReturn(tokens);
    }

    private String prepare(String... strings) {
        final StringBuilder sb = new StringBuilder();
        final String nl = System.getProperty("line.separator");
        for (final String s : strings) {
            sb.append(s).append(nl);
        }
        if (sb.length() > nl.length()) {
            return sb.substring(0, sb.length() - nl.length());
        } else {
            return sb.toString();
        }
    }

    @Test
    public void buildingonditionForNotRegex_appliesNotRegex() {
        expectToken(TokenType.NOTREGEX, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("not(fieldX like_regex 'foo')");
    }

    @Test
    public void buildingonditionForRegex_appliesRegex() {
        expectToken(TokenType.REGEX, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("fieldX like_regex 'foo'");
    }

    @Test
    public void buildingonditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingonditionForSome_appliesNotEmpty() {
        expectToken(TokenType.SOME, "whatever");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("char_length(cast(fieldX as varchar)) > 0");
    }

    @Test
    public void buildingonditionForNotOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) not like lower('%foo%')");
    }

    @Test
    public void buildingonditionForOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) like lower('%foo%')");
    }

    @Test
    public void buildingonditionForNotOpenLeftRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHT, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) not like lower('%foo%')");
    }

    @Test
    public void buildingonditionForOpenLeftRight_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHT, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) like lower('%foo%')");
    }

    @Test
    public void buildingonditionForNotOpenRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENRIGHTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) not like lower('foo%')");
    }

    @Test
    public void buildingonditionForOpenRightQuoted_appliesLike() {
        expectToken(TokenType.OPENRIGHTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) like lower('foo%')");
    }

    @Test
    public void buildingonditionForNotOpenRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENRIGHT, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) not like lower('foo%')");
    }

    @Test
    public void buildingonditionForOpenRight_appliesLike() {
        expectToken(TokenType.OPENRIGHT, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) like lower('foo%')");
    }

    @Test
    public void buildingonditionForNotOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) not like lower('%foo')");
    }

    @Test
    public void buildingonditionForOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) like lower('%foo')");
    }

    @Test
    public void buildingonditionForNotOpenLeft_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFT, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) not like lower('%foo')");
    }

    @Test
    public void buildingonditionForOpenLeft_appliesLike() {
        expectToken(TokenType.OPENLEFT, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) like lower('%foo')");
    }

    @Test
    public void buildingonditionForNotQuoted_appliesInequal() {
        expectToken(TokenType.NOTQUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) <> lower('foo')");
    }

    @Test
    public void buildingonditionForQuoted_appliesEqual() {
        expectToken(TokenType.QUOTED, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("lower(cast(fieldX as varchar)) = lower('foo')");
    }

    @Test
    public void buildingonditionForNotWord_appliesNotContains() {
        expectToken(TokenType.NOTWORD, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo(prepare(
            // @formatter:off
            "not(lower(cast(fieldX as varchar)) like ('%' || replace(",
            "  replace(",
            "    replace(",
            "      lower('foo'), ",
            "      '!', ",
            "      '!!'",
            "    ), ",
            "    '%', ",
            "    '!%'",
            "  ), ",
            "  '_', ",
            "  '!_'",
            ") || '%') escape '!')"
            // @formatter:on
        ));
    }

    @Test
    public void buildingonditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo(prepare(
            // @formatter:off
            "lower(cast(fieldX as varchar)) like ('%' || replace(",
            "  replace(",
            "    replace(",
            "      lower('foo'), ",
            "      '!', ",
            "      '!!'",
            "    ), ",
            "    '%', ",
            "    '!%'",
            "  ), ",
            "  '_', ",
            "  '!_'",
            ") || '%') escape '!'"
            // @formatter:on
        ));
    }

    @Test
    public void buildingonditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1");
    }

}
