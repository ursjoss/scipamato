package ch.difty.scipamato.core.web.paper

import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class NewsletterChangeEventTest {

    private lateinit var e: NewsletterChangeEvent

    @MockK
    private lateinit var targetMock: AjaxRequestTarget

    @MockK
    private lateinit var targetMock2: AjaxRequestTarget

    @Test
    fun canRetrieveTarget() {
        e = NewsletterChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
    }

    @Test
    fun canOverrideTarget() {
        e = NewsletterChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
        e.target = targetMock2
        e.target shouldBeEqualTo targetMock2
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(NewsletterChangeEvent::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
