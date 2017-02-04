package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.AuditSearchTerm;
import ch.difty.sipamato.entity.filter.AuditSearchTerm.Token;
import ch.difty.sipamato.entity.filter.AuditSearchTerm.TokenType;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class AuditSearchTermEvaluatorTest {

    private final AuditSearchTermEvaluator e = new AuditSearchTermEvaluator();

    private final List<Token> tokens = new ArrayList<>();

    @Mock
    private AuditSearchTerm stMock;
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

    private void expectToken(TokenType type, String term, String fieldName) {
        when(stMock.getFieldName()).thenReturn(fieldName);
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
    public void buildingConditionForGeraterOrEqual_applies() {
        expectToken(TokenType.GREATEROREQUAL, "2017-01-12 00:00:00", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("PAPER.CREATED >= timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForGreaterThan_applies() {
        expectToken(TokenType.GREATERTHAN, "2017-01-12 00:00:00", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("PAPER.CREATED > timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForExact_appliesRegex() {
        expectToken(TokenType.EXACT, "2017-01-12 00:00:00", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("PAPER.CREATED = timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForLessThan_applies() {
        expectToken(TokenType.LESSTHAN, "2017-01-12 00:00:00", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("PAPER.CREATED < timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForLessThanOrEqual_applies() {
        expectToken(TokenType.LESSOREQUAL, "2017-01-12 00:00:00", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("PAPER.CREATED <= timestamp '2017-01-12 00:00:00.0'");
    }

    @Test
    public void buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1");
    }

    @Test
    public void buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo", "PAPER.CREATED_BY");
        assertThat(e.evaluate(stMock).toString()).isEqualTo(prepare(
            // @formatter:off
              "\"PUBLIC\".\"PAPER\".\"ID\" in (",
              "  select \"PUBLIC\".\"PAPER\".\"ID\"",
              "  from \"PUBLIC\".\"PAPER\"",
              "    join \"PUBLIC\".\"USER\"",
              "    on PAPER.CREATED_BY = \"PUBLIC\".\"USER\".\"ID\"",
              "  where lower(\"PUBLIC\".\"USER\".\"USER_NAME\") like '%foo%'",
              ")"
            // @formatter:on
        ));
    }

    @Test
    public void buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo", "PAPER.CREATED");
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1");
    }

}
