package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class SearchOrderSelectorPanelInEditModeTest : SearchOrderSelectorPanelTest() {

    override val mode: Mode
        get() = Mode.EDIT

    @Test
    fun loadingPage_withSearchOrderWithCurrentOwner_rendersGlobalCheckBoxDisabled() {
        tester.startComponentInPage(makePanel())
        tester.assertEnabled("$PANEL_ID:form:global")
    }

    @Test
    fun withGlobalSearchOrders_withSameOwner_globalCheckBox_enabled() {
        tester.startComponentInPage(makePanel())
        tester.assertEnabled("$PANEL_ID:form:global")
    }

    @Test
    fun withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        searchOrder.apply { owner = OWNER_ID + 1 }
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }

    @Test
    fun changingName_forSearchOwnedByUser_addsTargetsAndSaves() {
        tester.startComponentInPage(makePanel())
        tester.executeAjaxEvent("$PANEL_ID:form:name", "change")
        val b = "$PANEL_ID:form:"
        tester.assertComponentOnAjaxResponse(b + "global")
        tester.assertComponentOnAjaxResponse(b + "name")
        tester.assertComponentOnAjaxResponse(b + "showExcluded")
        tester.assertComponentOnAjaxResponse(b + "showExcludedLabel")
        verify { searchOrderServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun changingName_forSearchOwnedByDifferentUser_doesNotAddTargetNorSaves() {
        searchOrder.apply { owner = OWNER_ID + 1 }
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }
}
