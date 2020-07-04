package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class SelfUpdateEventTest {

    @Test
    fun instantiate() {
        val targetDummy = AjaxRequestTargetSpy()
        val e = SelfUpdateEvent(targetDummy)
        e.target shouldBeEqualTo targetDummy
    }
}
