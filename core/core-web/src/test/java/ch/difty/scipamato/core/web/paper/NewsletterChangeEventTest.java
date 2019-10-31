package ch.difty.scipamato.core.web.paper;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NewsletterChangeEventTest {

    private NewsletterChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock, targetMock2;

    @Test
    void canRetrieveTarget() {
        e = new NewsletterChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    void canOverrideTarget() {
        e = new NewsletterChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
        e.setTarget(targetMock2);
        assertThat(e.getTarget()).isEqualTo(targetMock2);
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(NewsletterChangeEvent.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
