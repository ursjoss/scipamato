package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class ToggleExclusionsEventTest {

    @Test
    fun canRetrieveTarget() {
        val targetDummy = AjaxRequestTargetSpy()
        val e = ToggleExclusionsEvent(targetDummy)
        e.target shouldBeEqualTo targetDummy
    }
}
