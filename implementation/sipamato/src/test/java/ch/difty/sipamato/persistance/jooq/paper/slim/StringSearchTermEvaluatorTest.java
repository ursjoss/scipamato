package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.StringSearchTerm;
import ch.difty.sipamato.entity.StringSearchTerm.MatchType;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class StringSearchTermEvaluatorTest {

    private final StringSearchTermEvaluator e = new StringSearchTermEvaluator();

    @Mock
    private StringSearchTerm stMock;

    @Test
    public void evaluating_withNullParameter_throws() {
        try {
            e.evaluate(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchTerm must not be null.");
        }
    }

    private void expectSearchTerm(MatchType type, String v) {
        when(stMock.getType()).thenReturn(type);
        when(stMock.getKey()).thenReturn("fieldX");
        when(stMock.getValue()).thenReturn(v);
    }

    @Test
    public void buildingondition_withExactComparison() {
        expectSearchTerm(MatchType.EXACT, "foo");
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("lower(cast(fieldX as varchar)) = lower('foo')");
    }

    @Test
    public void buildingondition_withContainsComparison() {
        expectSearchTerm(MatchType.CONTAINS, "foo");
        Condition c = e.evaluate(stMock);
        // @formatter:off
        assertThat(c.toString()).isEqualTo("lower(cast(fieldX as varchar)) like ('%' || replace(\n" +
                "  replace(\n" +
                "    replace(\n" +
                "      lower('foo'), \n" +
                "      '!', \n" +
                "      '!!'\n" +
                "    ), \n" +
                "    '%', \n" +
                "    '!%'\n" +
                "  ), \n" +
                "  '_', \n" +
                "  '!_'\n" +
                ") || '%') escape '!'");
        // @formatter:on
    }

    @Test
    public void buildingondition_withStartWithComparison() {
        expectSearchTerm(MatchType.STARTS_WITH, "foo");
        Condition c = e.evaluate(stMock);
        // @formatter:off
        assertThat(c.toString()).isEqualTo("lower(cast(fieldX as varchar)) like (replace(\n" +
                "  replace(\n" +
                "    replace(\n" +
                "      lower('foo'), \n" +
                "      '!', \n" +
                "      '!!'\n" +
                "    ), \n" +
                "    '%', \n" +
                "    '!%'\n" +
                "  ), \n" +
                "  '_', \n" +
                "  '!_'\n" +
                ") || '%') escape '!'");
                // @formatter:on);
    }

    @Test
    public void buildingondition_withEndWithComparison() {
        expectSearchTerm(MatchType.ENDS_WITH, "foo");
        Condition c = e.evaluate(stMock);
        // @formatter:off
        assertThat(c.toString()).isEqualTo("lower(cast(fieldX as varchar)) like ('%' || replace(\n" +
                "  replace(\n" +
                "    replace(\n" +
                "      lower('foo'), \n" +
                "      '!', \n" +
                "      '!!'\n" +
                "    ), \n" +
                "    '%', \n" +
                "    '!%'\n" +
                "  ), \n" +
                "  '_', \n" +
                "  '!_'\n" +
                ")) escape '!'");
        // @formatter:on);
    }

}
