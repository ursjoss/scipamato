package ch.difty.scipamato.core.web.panel.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
