package ch.difty.scipamato.publ.web.pym

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

class PymScriptsTest {

    @Test
    fun testValues() {
        PymScripts.entries shouldContainSame listOf(PymScripts.INSTANTIATE, PymScripts.RESIZE)
    }

    @Test
    fun instantiating() {
        PymScripts.INSTANTIATE.id shouldBeEqualTo "pymChild"
        PymScripts.INSTANTIATE.script shouldBeEqualTo "var pymChild = new pym.Child({ id: 'scipamato-public' });"
    }

    @Test
    fun resizing() {
        PymScripts.RESIZE.id shouldBeEqualTo "pymResize"
        PymScripts.RESIZE.script shouldBeEqualTo "pymChild.sendHeight();"
    }
}
