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
    public void usingMinimalConstructor_doesNotAddExclusionId() {
        e = new SearchOrderChangeEvent(targetMock);
        assertThat(e.getExcludedId()).isNull();
    }

    @Test
    public void usingMinimalConstructor_doesNotRequestNewSearchOrder() {
        e = new SearchOrderChangeEvent(targetMock);
    }

    @Test
    public void usingSecondConstructorWithNullId_doesNotAddExclusionId() {
        e = new SearchOrderChangeEvent(targetMock, null);
        assertThat(e.getExcludedId()).isNull();
    }

    @Test
    public void usingSecondConstructor_doesAddExclusionId() {
        e = new SearchOrderChangeEvent(targetMock, 5l);
        assertThat(e.getExcludedId()).isEqualTo(5l);
    }

    @Test
    public void requestingNewSearchOrder_setsFlagAccordingly() {
        e = new SearchOrderChangeEvent(targetMock).requestNewSearchOrder();
        assertThat(e.isNewSearchOrderRequested()).isTrue();
    }

    @Test
    public void requestingNewSearchOrder_whenExcludeIdIsSetAlso_setsFlagNevertheless() {
        e = new SearchOrderChangeEvent(targetMock, 5l).requestNewSearchOrder();
        assertThat(e.isNewSearchOrderRequested()).isTrue();
    }

    @Test
    public void requestingNewSearchOrder_whenExcludeIdIsSetAlso_dropsExcludedId() {
        e = new SearchOrderChangeEvent(targetMock, 5l).requestNewSearchOrder();
        assertThat(e.getExcludedId()).isNull();
    }
}
