package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.entity.filter.AuditSearchTerm;
import ch.difty.scipamato.entity.filter.AuditSearchTerm.Token;
import ch.difty.scipamato.entity.filter.AuditSearchTerm.TokenType;

public class AuditSearchTermEvaluatorTest extends SearchTermEvaluatorTest<AuditSearchTerm> {

    private final AuditSearchTermEvaluator e = new AuditSearchTermEvaluator();

    private final List<Token> tokens = new ArrayList<>();

    @Mock
    private AuditSearchTerm stMock;
    @Mock
    private Token tokenMock;

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
    public void buildingConditionForGeraterOrEqual_applies() {
        expectToken(TokenType.GREATEROREQUAL, "2017-01-12 00:00:00", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created >= timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForGreaterThan_applies() {
        expectToken(TokenType.GREATERTHAN, "2017-01-12 00:00:00", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created > timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForExact_appliesRegex() {
        expectToken(TokenType.EXACT, "2017-01-12 00:00:00", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created = timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForLessThan_applies() {
        expectToken(TokenType.LESSTHAN, "2017-01-12 00:00:00", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created < timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForLessThanOrEqual_applies() {
        expectToken(TokenType.LESSOREQUAL, "2017-01-12 00:00:00", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created <= timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo", "paper.created_by");
        assertThat(e.evaluate(stMock).toString()).isEqualToIgnoringCase(concat(
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
    public void buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo", "paper.created");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingConditionForWrongField_withExactMatch_throws() {
        expectToken(TokenType.EXACT, "foo", "bar");
        validateDegenerateField("Field bar is not one of the expected date fields [paper.created, paper.last_modified] entitled to use MatchType.EQUALS");
    }

    @Test
    public void buildingConditionForWrongField_withContainedMatch_throws() {
        expectToken(TokenType.WORD, "foo", "baz");
        validateDegenerateField("Field baz is not one of the expected user fields [paper.created_by, paper.last_modified_by] entitled to use MatchType.CONTAINS");
    }

    private void validateDegenerateField(String msg) {
        try {
            e.evaluate(stMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage(msg);
        }
    }

}
