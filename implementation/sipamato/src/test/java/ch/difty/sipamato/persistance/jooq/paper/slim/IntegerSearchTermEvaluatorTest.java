package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.IntegerSearchTerm;
import ch.difty.sipamato.entity.IntegerSearchTerm.MatchType;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class IntegerSearchTermEvaluatorTest {

    private final IntegerSearchTermEvaluator e = new IntegerSearchTermEvaluator();

    @Mock
    private IntegerSearchTerm istMock;

    @Test
    public void evaluating_withNullParameter_throws() {
        try {
            e.evaluate(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchTerm must not be null.");
        }
    }

    private void expectIntegerSearchTerm(MatchType type, int v) {
        expectIntegerSearchTerm(type, v, v);

    }

    private void expectIntegerSearchTerm(MatchType type, int v1, int v2) {
        when(istMock.getType()).thenReturn(type);
        when(istMock.getKey()).thenReturn("fieldX");
        when(istMock.getValue()).thenReturn(v1);
        when(istMock.getValue2()).thenReturn(v2);
    }

    @Test
    public void buildingIntegerCondition_withGreaterThanComparison() {
        expectIntegerSearchTerm(MatchType.GREATER_THAN, 10);
        Condition c = e.evaluate(istMock);
        assertThat(c.toString()).isEqualTo("fieldX > 10");
    }

    @Test
    public void buildingIntegerCondition_withGreaterThanOrEqualComparison() {
        expectIntegerSearchTerm(MatchType.GREATER_OR_EQUAL, 10);
        Condition c = e.evaluate(istMock);
        assertThat(c.toString()).isEqualTo("fieldX >= 10");
    }

    @Test
    public void buildingIntegerCondition_withExactValue() {
        expectIntegerSearchTerm(MatchType.EXACT, 10);
        Condition c = e.evaluate(istMock);
        assertThat(c.toString()).isEqualTo("fieldX = 10");
    }

    @Test
    public void buildingIntegerCondition_withLessThanOrEqualComparison() {
        expectIntegerSearchTerm(MatchType.LESS_OR_EQUAL, 10);
        Condition c = e.evaluate(istMock);
        assertThat(c.toString()).isEqualTo("fieldX <= 10");
    }

    @Test
    public void buildingIntegerCondition_withLessThanComparison() {
        expectIntegerSearchTerm(MatchType.LESS_THAN, 10);
        Condition c = e.evaluate(istMock);
        assertThat(c.toString()).isEqualTo("fieldX < 10");
    }

    @Test
    public void buildingIntegerCondition_withRangeomparison() {
        expectIntegerSearchTerm(MatchType.RANGE, 10, 15);
        Condition c = e.evaluate(istMock);
        assertThat(c.toString()).isEqualTo("fieldX between 10 and 15");
    }
}
