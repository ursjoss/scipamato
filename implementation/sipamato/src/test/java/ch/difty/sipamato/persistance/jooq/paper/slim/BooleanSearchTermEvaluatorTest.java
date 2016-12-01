package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.BooleanSearchTerm;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class BooleanSearchTermEvaluatorTest {

    private final BooleanSearchTermEvaluator e = new BooleanSearchTermEvaluator();

    @Mock
    private BooleanSearchTerm stMock;

    @Test
    public void evaluating_withNullParameter_throws() {
        try {
            e.evaluate(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchTerm must not be null.");
        }
    }

    private void expectSearchTerm(boolean v) {
        when(stMock.getKey()).thenReturn("fieldX");
        when(stMock.getValue()).thenReturn(v);
    }

    @Test
    public void buildingCondition_whenTrue() {
        expectSearchTerm(true);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX = true");
    }

    @Test
    public void buildingCondition_whenFalse() {
        expectSearchTerm(false);
        Condition c = e.evaluate(stMock);
        assertThat(c.toString()).isEqualTo("fieldX = false");
    }
}
