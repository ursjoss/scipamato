package ch.difty.scipamato.common.web.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WicketEventTest {

    @Mock
    private AjaxRequestTarget targetMock;

    private WicketEvent e;

    @BeforeEach
    public void setUp() {
        e = new WicketEvent(targetMock) {
        };
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(targetMock);
    }

    @Test
    public void test() {
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(WicketEvent.class)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void testingToString() {
        assertThat(e.toString()).isEqualTo("WicketEvent(target=targetMock)");
    }
}
