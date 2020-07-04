package ch.difty.scipamato.common.web.event

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldStartWith
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.Test

internal class WicketEventTest {

    private val targetSpy: AjaxRequestTarget = AjaxRequestTargetSpy()
    private val e: WicketEvent = object : WicketEvent(targetSpy) {}

    @Test
    fun test() {
        e.target shouldBeEqualTo targetSpy
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
    }
}
