package ch.difty.scipamato.core.web.common

import io.mockk.mockk
import io.mockk.unmockkAll
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.Test

internal class SelfUpdateEventTest {

    @Test
    fun instantiate() {
        val targetMock = mockk<AjaxRequestTarget>()
        val e = SelfUpdateEvent(targetMock)
        e.target shouldBeEqualTo targetMock
        unmockkAll()
    }
}
