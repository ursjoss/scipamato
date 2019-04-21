package ch.difty.scipamato.core.web.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SelfUpdateEventTest {

    @Mock
    private AjaxRequestTarget targetMock;

    @Test
    public void instantiate() {
        SelfUpdateEvent e = new SelfUpdateEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }
}
