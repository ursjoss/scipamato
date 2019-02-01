package ch.difty.scipamato.core.persistence.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.entity.search.AuditSearchTerm;
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.Token;
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.TokenType;

public class AuditSearchTermEvaluatorTest extends SearchTermEvaluatorTest<AuditSearchTerm> {

    private final AuditSearchTermEvaluator e = new AuditSearchTermEvaluator();

    private final List<Token> tokens = new ArrayList<>();

    @Mock
    private AuditSearchTerm stMock;

    @Override
    protected AuditSearchTermEvaluator getEvaluator() {
        return e;
    }

    private void expectToken(TokenType type, String term, String fieldName) {
        when(stMock.getFieldName()).thenReturn(fieldName);
        tokens.add(new Token(type, term));
        when(stMock.getTokens()).thenReturn(tokens);
    }

    @Test
    public void buildingConditionForGreaterOrEqual_applies() {
        expectToken(TokenType.GREATEROREQUAL, "2017-01-12 00:00:00", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("paper.created >= timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForGreaterThan_applies() {
        expectToken(TokenType.GREATERTHAN, "2017-01-12 00:00:00", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("paper.created > timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForExact_appliesRegex() {
        expectToken(TokenType.EXACT, "2017-01-12 00:00:00", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("paper.created = timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForLessThan_applies() {
        expectToken(TokenType.LESSTHAN, "2017-01-12 00:00:00", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("paper.created < timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForLessThanOrEqual_applies() {
        expectToken(TokenType.LESSOREQUAL, "2017-01-12 00:00:00", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("paper.created <= timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForDateRange_applies() {
        expectToken(TokenType.RANGEQUOTED, "2017-01-11 10:00:00-2017-01-12 15:14:13", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            "paper.created between timestamp '2017-01-11 10:00:00.0' and timestamp '2017-01-12 15:14:13.0'");
    }

    @Test
    public void buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingConditionForWhitespace_appliesTrueCondition2() {
        expectToken(TokenType.WHITESPACE, "   ", "paper.last_modified");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo", "paper.created_by");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualToIgnoringCase(concat(
            // @formatter:off
              "\"public\".\"paper\".\"id\" in (",
              "  select \"public\".\"paper\".\"id\"",
              "  from \"public\".\"paper\"",
              "    join \"public\".\"scipamato_user\"",
              "    on paper.created_by = \"public\".\"scipamato_user\".\"id\"",
              "  where lower(\"public\".\"scipamato_user\".\"user_name\") like '%foo%'",
              ")"
            // @formatter:on
        ));
    }

    @Test
    public void buildingConditionForWord_appliesContains2() {
        expectToken(TokenType.WORD, "foo", "paper.last_modified_by");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualToIgnoringCase(concat(
            // @formatter:off
              "\"public\".\"paper\".\"id\" in (",
              "  select \"public\".\"paper\".\"id\"",
              "  from \"public\".\"paper\"",
              "    join \"public\".\"scipamato_user\"",
              "    on paper.last_modified_by = \"public\".\"scipamato_user\".\"id\"",
              "  where lower(\"public\".\"scipamato_user\".\"user_name\") like '%foo%'",
              ")"
            // @formatter:on
        ));
    }

    @Test
    public void buildingConditionForGreaterOrEquals() {
        expectToken(TokenType.GREATEROREQUAL, "2019-01-05", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualToIgnoringCase(concat("paper.created >= timestamp '2019-01-05 00:00:00.0'"));
    }

    @Test
    public void buildingConditionForGreaterOrEquals2() {
        expectToken(TokenType.GREATEROREQUAL, "2019-01-05", "paper.last_modified");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualToIgnoringCase(concat("paper.last_modified >= timestamp '2019-01-05 00:00:00.0'"));
    }

    @Test
    public void buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo", "paper.created");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingConditionForWrongField_withExactMatch_throws() {
        expectToken(TokenType.EXACT, "foo", "bar");
        validateDegenerateField(
            "Field bar is not one of the expected date fields [paper.created, paper.last_modified] entitled to use MatchType.EQUALS");
    }

    @Test
    public void buildingConditionForWrongField_withContainedMatch_throws() {
        expectToken(TokenType.WORD, "foo", "baz");
        validateDegenerateField(
            "Field baz is not one of the expected user fields [paper.created_by, paper.last_modified_by] entitled to use MatchType.CONTAINS");
    }

    private void validateDegenerateField(String msg) {
        try {
            e.evaluate(stMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(msg);
        }
    }

    @Test
    public void buildingConditionForWord_withNonUserField_throws() {
        expectToken(TokenType.WORD, "foo", "firstAuthor");
        try {
            e.evaluate(stMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                    "Field firstAuthor is not one of the expected user fields [paper.created_by, paper.last_modified_by] entitled to use MatchType.CONTAINS");
        }
    }
}
