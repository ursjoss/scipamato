package ch.difty.sipamato.web.panel.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchOrderChangeEventTest {

    private SearchOrderChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock;

    @Test
    public void canRetrieveTarget() {
        e = new SearchOrderChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    public void usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = new SearchOrderChangeEvent(targetMock);
        assertThat(e.getExcludedId()).isNull();
        assertThat(e.getDroppedConditionId()).isNull();
        assertThat(e.isNewSearchOrderRequested()).isFalse();
    }

    @Test
    public void usingWithExcludedPaperId_doesAddExclusionId() {
        e = new SearchOrderChangeEvent(targetMock).withExcludedPaperId(5l);
        assertThat(e.getExcludedId()).isEqualTo(5l);
    }

    @Test
    public void usingWithDroppedConditionId_doesAddConditionId() {
        e = new SearchOrderChangeEvent(targetMock).withDroppedConditionId(5l);
        assertThat(e.getDroppedConditionId()).isEqualTo(5l);
    }

    @Test
    public void requestingNewSearchOrder_setsFlagAccordingly() {
        e = new SearchOrderChangeEvent(targetMock).requestingNewSearchOrder();
        assertThat(e.isNewSearchOrderRequested()).isTrue();
    }

    @Test
    public void requestingNewSearchOrder_withExcludedPaperIdAndNewSearchOrderRequest_newSearchOrderRequestWins() {
        e = new SearchOrderChangeEvent(targetMock).withExcludedPaperId(5l).requestingNewSearchOrder();
        assertThat(e.isNewSearchOrderRequested()).isTrue();
        assertThat(e.getExcludedId()).isNull();
    }

    @Test
    public void requestingNewSearchOrder_withNewSearchOrderRequestAndthenExcludedPaperId_exclusionWins() {
        e = new SearchOrderChangeEvent(targetMock).requestingNewSearchOrder().withExcludedPaperId(5l);
        assertThat(e.getExcludedId()).isEqualTo(5l);
        assertThat(e.isNewSearchOrderRequested()).isFalse();
    }

}
