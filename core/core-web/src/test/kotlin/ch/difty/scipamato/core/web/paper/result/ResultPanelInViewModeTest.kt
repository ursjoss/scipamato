package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.common.web.Mode

internal class ResultPanelInViewModeTest : ResultPanelTest() {

    override val mode: Mode
        get() = Mode.VIEW

    override fun assertTableRow(bb: String) {
        tester.assertLabel("$bb:1:cell", java.lang.String.valueOf(NUMBER))
        tester.assertLabel("$bb:2:cell", "firstAuthor")
        tester.assertLabel("$bb:3:cell", "2016")
        tester.assertLabel("$bb:4:cell:link:label", "title")
        tester.assertLabel("$bb:5:cell", "1")
        tester.assertContainsNot("$bb:6:cell:link:image")
        tester.assertContainsNot("$bb:7:cell:link:image")
    }
}
