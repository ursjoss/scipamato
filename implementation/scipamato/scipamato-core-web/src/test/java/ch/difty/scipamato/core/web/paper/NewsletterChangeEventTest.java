package ch.difty.scipamato.core.web.paper;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NewsletterChangeEventTest {

    private NewsletterChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock, targetMock2;

    @Test
    public void canRetrieveTarget() {
        e = new NewsletterChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    public void canOverrideTarget() {
        e = new NewsletterChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
        e.setTarget(targetMock2);
        assertThat(e.getTarget()).isEqualTo(targetMock2);
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(NewsletterChangeEvent.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}