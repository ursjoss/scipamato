package ch.difty.scipamato.common.web.event

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldStartWith
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class WicketEventTest {

    private val targetMock = mockk<AjaxRequestTarget>()

    private val e: WicketEvent = object : WicketEvent(targetMock) {}

    @AfterEach
    fun tearDown() {
        confirmVerified(targetMock)
    }

    @Test
    fun test() {
        e.target shouldBeEqualTo targetMock
        verify { targetMock == any() }
    }

    @Test
    fun equals() {
        EqualsVerifier.forClass(WicketEvent::class.java)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun testingToString() {
        e.toString().shouldStartWith("WicketEvent(target=AjaxRequestTarget")
        verify { targetMock.toString() }
    }
}
