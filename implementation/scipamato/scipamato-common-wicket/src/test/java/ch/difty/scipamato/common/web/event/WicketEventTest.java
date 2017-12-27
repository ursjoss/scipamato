package ch.difty.scipamato.common.web.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@RunWith(MockitoJUnitRunner.class)
public class WicketEventTest {

    @Mock
    private AjaxRequestTarget targetMock;

    private WicketEvent e;

    @Before
    public void setUp() {
        e = new WicketEvent(targetMock) {
        };
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(targetMock);
    }

    @Test
    public void test() {
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(WicketEvent.class)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void testingToString() {
        assertThat(e.toString()).isEqualTo("WicketEvent(target=targetMock)");
    }
}
