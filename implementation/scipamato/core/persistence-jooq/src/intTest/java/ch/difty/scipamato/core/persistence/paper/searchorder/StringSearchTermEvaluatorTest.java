package ch.difty.scipamato.core.persistence.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.search.StringSearchTerm;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.Token;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType;

@ExtendWith(MockitoExtension.class)
class StringSearchTermEvaluatorTest extends SearchTermEvaluatorTest<StringSearchTerm> {

    private final StringSearchTermEvaluator e = new StringSearchTermEvaluator();

    private final List<Token> tokens = new ArrayList<>();

    @Mock
    private StringSearchTerm stMock;

    @Override
    protected StringSearchTermEvaluator getEvaluator() {
        return e;
    }

    private void expectToken(TokenType type, String term) {
        when(stMock.getFieldName()).thenReturn("field_x");
        tokens.add(new Token(type, term));
        when(stMock.getTokens()).thenReturn(tokens);
    }

    @Test
    void buildingConditionForNotRegex_appliesNotRegex() {
        expectToken(TokenType.NOTREGEX, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("not(field_x like_regex 'foo')");
    }

    @Test
    void buildingConditionForRegex_appliesRegex() {
        expectToken(TokenType.REGEX, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("field_x like_regex 'foo'");
    }

    @Test
    void buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    void buildingConditionForSome_appliesNotEmpty() {
        expectToken(TokenType.SOME, "whatever");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  field_x is not null\n" +
            "  and char_length(cast(field_x as varchar)) > 0\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    void buildingConditionForEmpty_appliesEmpty() {
        expectToken(TokenType.EMPTY, "whatever");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  field_x is null\n" +
            "  or char_length(cast(field_x as varchar)) = 0\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    void buildingConditionForNotOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) not like lower('%foo%')");
    }

    @Test
    void buildingConditionForOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo%')");
    }

    @Test
    void buildingConditionForNotOpenLeftRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) not like lower('%foo%')");
    }

    @Test
    void buildingConditionForOpenLeftRight_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo%')");
    }

    @Test
    void buildingConditionForNotOpenRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) not like lower('foo%')");
    }

    @Test
    void buildingConditionForOpenRightQuoted_appliesLike() {
        expectToken(TokenType.OPENRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('foo%')");
    }

    @Test
    void buildingConditionForNotOpenRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) not like lower('foo%')");
    }

    @Test
    void buildingConditionForOpenRight_appliesLike() {
        expectToken(TokenType.OPENRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('foo%')");
    }

    @Test
    void buildingConditionForNotOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) not like lower('%foo')");
    }

    @Test
    void buildingConditionForOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo')");
    }

    @Test
    void buildingConditionForNotOpenLeft_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) not like lower('%foo')");
    }

    @Test
    void buildingConditionForOpenLeft_appliesLike() {
        expectToken(TokenType.OPENLEFT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo')");
    }

    @Test
    void buildingConditionForNotQuoted_appliesUnequal() {
        expectToken(TokenType.NOTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) <> lower('foo')");
    }

    @Test
    void buildingConditionForQuoted_appliesEqual() {
        expectToken(TokenType.QUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) = lower('foo')");
    }

    @Test
    void buildingConditionForNotWord_appliesNotContains() {
        expectToken(TokenType.NOTWORD, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "not(lower(cast(field_x as varchar)) like ('%' || replace(",
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
    void buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "lower(cast(field_x as varchar)) like ('%' || replace(",
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
    void buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    void buildingConditionForUnsupported_throws() {
        expectToken(TokenType.UNSUPPORTED, "foo");
        try {
            e.evaluate(stMock);
            fail("should have thrown exception");
        } catch (Error ex) {
            assertThat(ex)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Evaluation of type UNSUPPORTED is not supported...");
        }
    }

}
