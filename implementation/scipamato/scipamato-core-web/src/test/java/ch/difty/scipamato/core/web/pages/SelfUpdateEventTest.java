package ch.difty.scipamato.core.web.pages;

import static org.assertj.core.api.Assertions.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelfUpdateEventTest {

    @Mock
    private AjaxRequestTarget targetMock;

    @Test
    public void instantiate() {
        SelfUpdateEvent e = new SelfUpdateEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }
}
