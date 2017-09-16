package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.jooq.Condition;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.entity.filter.BooleanSearchTerm;

public class BooleanSearchTermEvaluatorTest extends SearchTermEvaluatorTest<BooleanSearchTerm> {

    private final BooleanSearchTermEvaluator e = new BooleanSearchTermEvaluator();

    @Mock
    private BooleanSearchTerm stMock;

    @Override
    protected BooleanSearchTermEvaluator getEvaluator() {
        return e;
    }

    private void expectSearchTerm(boolean v) {
        when(stMock.getFieldName()).thenReturn("fieldX");
        when(stMock.getValue()).thenReturn(v);
    }

    @Test
    public void buildingCondition_whenTrue() {
        expectSearchTerm(true);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x = true");
    }

    @Test
    public void buildingCondition_whenFalse() {
        expectSearchTerm(false);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("field_x = false");
    }
}
