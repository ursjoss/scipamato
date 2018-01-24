package ch.difty.scipamato.core.web.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
