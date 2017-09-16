package ch.difty.scipamato.web.panel.search;

import static org.assertj.core.api.Assertions.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ToggleExclusionsEventTest {

    private ToggleExclusionsEvent e;

    @Mock
    private AjaxRequestTarget targetMock;

    @Test
    public void canRetrieveTarget() {
        e = new ToggleExclusionsEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }
}
