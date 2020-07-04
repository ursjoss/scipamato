package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
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
    fun canOverrideTarget() {
        e.target shouldBeEqualTo targetMock

        val targetDummy2 = AjaxRequestTargetSpy()
        e.target = targetDummy2
        e.target shouldBeEqualTo targetDummy2
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
