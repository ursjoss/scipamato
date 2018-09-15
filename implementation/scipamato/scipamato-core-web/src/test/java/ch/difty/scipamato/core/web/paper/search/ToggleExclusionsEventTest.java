package ch.difty.scipamato.core.web.paper.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ToggleExclusionsEventTest {

    @Mock
    private AjaxRequestTarget targetMock;

    @Test
    public void canRetrieveTarget() {
        final ToggleExclusionsEvent e = new ToggleExclusionsEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }
}
