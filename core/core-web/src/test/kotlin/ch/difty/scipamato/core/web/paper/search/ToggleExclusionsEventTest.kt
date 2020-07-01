package ch.difty.scipamato.core.web.paper.search

import io.mockk.mockk
import io.mockk.unmockkAll
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.Test

internal class ToggleExclusionsEventTest {

    @Test
    fun canRetrieveTarget() {
        val targetMock = mockk<AjaxRequestTarget>()
        val e = ToggleExclusionsEvent(targetMock)
        e.target shouldBeEqualTo targetMock
        unmockkAll()
    }
}
