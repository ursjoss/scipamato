package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.IntegerSearchTerm;
import ch.difty.sipamato.entity.filter.IntegerSearchTerm.MatchType;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class IntegerSearchTermEvaluatorTest {

    private final IntegerSearchTermEvaluator e = new IntegerSearchTermEvaluator();

    @Mock
    private IntegerSearchTerm stMock;

    @Test
    public void evaluating_withNullParameter_throws() {
        try {
            e.evaluate(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchTerm must not be null.");
        }
    }

    private void expectSearchTerm(MatchType type, int v) {
        expectSearchTerm(type, v, v);
    }

    private void expectSearchTerm(MatchType type, int v1, int v2) {
        when(stMock.getType()).thenReturn(type);
        when(stMock.getKey()).thenReturn("fieldX");
        when(stMock.getValue()).thenReturn(v1);
        when(stMock.getValue2()).thenReturn(v2);
    }

    @Test
    public void buildingCondition_withGreaterThanComparison() {
        expectSearchTerm(MatchType.GREATER_THAN, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX > 10");
    }

    @Test
    public void buildingCondition_withGreaterThanOrEqualComparison() {
        expectSearchTerm(MatchType.GREATER_OR_EQUAL, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX >= 10");
    }

    @Test
    public void buildingCondition_withExactValue() {
        expectSearchTerm(MatchType.EXACT, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX = 10");
    }

    @Test
    public void buildingCondition_withLessThanOrEqualComparison() {
        expectSearchTerm(MatchType.LESS_OR_EQUAL, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX <= 10");
    }

    @Test
    public void buildingCondition_withLessThanComparison() {
        expectSearchTerm(MatchType.LESS_THAN, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX < 10");
    }

    @Test
    public void buildingCondition_withRangeomparison() {
        expectSearchTerm(MatchType.RANGE, 10, 15);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX between 10 and 15");
    }
}
