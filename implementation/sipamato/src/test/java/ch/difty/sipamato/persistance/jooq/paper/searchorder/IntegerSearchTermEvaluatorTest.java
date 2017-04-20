package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.IntegerSearchTerm;
import ch.difty.sipamato.entity.filter.IntegerSearchTerm.MatchType;

@RunWith(MockitoJUnitRunner.class)
public class IntegerSearchTermEvaluatorTest extends SearchTermEvaluatorTest<IntegerSearchTerm> {

    private final IntegerSearchTermEvaluator e = new IntegerSearchTermEvaluator();

    @Mock
    private IntegerSearchTerm stMock;

    @Override
    protected IntegerSearchTermEvaluator getEvaluator() {
        return e;
    }

    private void expectSearchTerm(MatchType type, int v) {
        expectSearchTerm(type, v, v);
    }

    private void expectSearchTerm(MatchType type, int v1, int v2) {
        when(stMock.getType()).thenReturn(type);
        when(stMock.getFieldName()).thenReturn("fieldX");
        when(stMock.getValue()).thenReturn(v1);
        when(stMock.getValue2()).thenReturn(v2);
    }

    @Test
    public void buildingCondition_withGreaterThanComparison() {
        expectSearchTerm(MatchType.GREATER_THAN, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x > 10");
    }

    @Test
    public void buildingCondition_withGreaterThanOrEqualComparison() {
        expectSearchTerm(MatchType.GREATER_OR_EQUAL, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x >= 10");
    }

    @Test
    public void buildingCondition_withExactValue() {
        expectSearchTerm(MatchType.EXACT, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x = 10");
    }

    @Test
    public void buildingCondition_withLessThanOrEqualComparison() {
        expectSearchTerm(MatchType.LESS_OR_EQUAL, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x <= 10");
    }

    @Test
    public void buildingCondition_withLessThanComparison() {
        expectSearchTerm(MatchType.LESS_THAN, 10);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x < 10");
    }

    @Test
    public void buildingCondition_withRangeomparison() {
        expectSearchTerm(MatchType.RANGE, 10, 15);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x between 10 and 15");
    }

    @Test
    public void buildingCondition_withFieldValueMissing() {
        int any = 0;
        expectSearchTerm(MatchType.MISSING, any, any);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x is null");
    }

    @Test
    public void buildingCondition_withAnyFieldValuePresent() {
        int any = 0;
        expectSearchTerm(MatchType.PRESENT, any, any);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x is not null");
    }

}
