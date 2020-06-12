package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.ClearAllMocksExtension
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class SelfUpdateEventTest {

    @MockK
    private lateinit var targetMock: AjaxRequestTarget

    @Test
    fun instantiate() {
        val e = SelfUpdateEvent(targetMock)
        e.target shouldBeEqualTo targetMock
    }
}
