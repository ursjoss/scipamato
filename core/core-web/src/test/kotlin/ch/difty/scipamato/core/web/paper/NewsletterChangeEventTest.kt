package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class NewsletterChangeEventTest {

    private val targetMock = AjaxRequestTargetSpy()
    private val e: NewsletterChangeEvent = NewsletterChangeEvent(targetMock)

    @AfterEach
    fun tearDown() {
        targetMock.reset()
    }

    @Test
    fun canRetrieveTarget() {
        e.target shouldBeEqualTo targetMock
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(NewsletterChangeEvent::class.java)
            .withRedefinedSuperclass()
            .verify()
    }
}
