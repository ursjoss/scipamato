package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import org.junit.jupiter.api.Test

internal class SearchOrderPanelInEditModeTest : SearchOrderPanelTest() {

    override val mode: Mode
        get() = Mode.EDIT

    @Test
    fun clickingDeleteIconLink() {
        tester.startComponentInPage(makePanel())
        tester.assertContains("foo")
        tester.clickLink("panel:form:searchConditions:body:rows:1:cells:2:cell:link")
        tester.assertInfoMessages("Removed foo")
        tester.assertComponentOnAjaxResponse("$PANEL_ID:form:searchConditions")
    }
}
