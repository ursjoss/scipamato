package ch.difty.scipamato.core.web.paper.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ToggleExclusionsEventTest {

    @Mock
    private AjaxRequestTarget targetMock;

    @Test
    public void canRetrieveTarget() {
        final ToggleExclusionsEvent e = new ToggleExclusionsEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }
}
