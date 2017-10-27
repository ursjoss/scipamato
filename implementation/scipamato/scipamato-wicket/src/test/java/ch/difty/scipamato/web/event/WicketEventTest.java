package ch.difty.scipamato.web.event;

import static org.assertj.core.api.Assertions.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WicketEventTest {

    @Mock
    private AjaxRequestTarget target;

    @Test
    public void test() {
        WicketEvent e = new WicketEvent(target) {
        };
        assertThat(e.getTarget()).isEqualTo(target);
    }
}
