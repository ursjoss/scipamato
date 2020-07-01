package ch.difty.scipamato.core.web.paper

import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.unmockkAll
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class NewsletterChangeEventTest {

    private lateinit var e: NewsletterChangeEvent

    private lateinit var targetMock: AjaxRequestTarget

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun canRetrieveTarget() {
        targetMock = mockk()
        e = NewsletterChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
    }

    @Test
    fun canOverrideTarget() {
        targetMock = mockk()
        e = NewsletterChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock

        val targetMock2 = mockk<AjaxRequestTarget>()
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
