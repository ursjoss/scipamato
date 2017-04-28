package ch.difty.sipamato.web.panel.paper;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NewFieldChangeEventTest {

    private NewFieldChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock, targetMock2;

    @Test
    public void canRetrieveTarget() {
        e = new NewFieldChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    public void usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = new NewFieldChangeEvent(targetMock);
        assertThat(e.getId()).isNull();
    }

    @Test
    public void usingWithId_doesAddId() {
        e = new NewFieldChangeEvent(targetMock).withId("foo");
        assertThat(e.getId()).isEqualTo("foo");
    }

    @Test
    public void canOverrideTarget() {
        e = new NewFieldChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
        e.setTarget(targetMock2);
        assertThat(e.getTarget()).isEqualTo(targetMock2);
    }

}
