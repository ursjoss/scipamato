package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.core.web.WicketTest
import org.apache.wicket.markup.html.panel.Panel
import org.junit.jupiter.api.Test

abstract class PanelTest<T : Panel> : WicketTest() {

    @Test
    fun assertPanel() {
        tester.startComponentInPage(makePanel())
        assertSpecificComponents()
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    protected abstract fun makePanel(): T
    protected abstract fun assertSpecificComponents()

    companion object {
        const val PANEL_ID = "panel"
    }
}
