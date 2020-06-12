package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.ClearAllMocksExtension
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class ToggleExclusionsEventTest {

    @MockK
    private lateinit var targetMock: AjaxRequestTarget

    @Test
    fun canRetrieveTarget() {
        val e = ToggleExclusionsEvent(targetMock)
        e.target shouldBeEqualTo targetMock
    }
}
