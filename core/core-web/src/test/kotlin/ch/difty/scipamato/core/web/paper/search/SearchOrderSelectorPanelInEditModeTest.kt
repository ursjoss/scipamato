package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class SearchOrderSelectorPanelInEditModeTest : SearchOrderSelectorPanelTest() {

    override val mode: Mode
        get() = Mode.EDIT

    @Test
    fun loadingPage_withSearchOrderWithCurrentOwner_rendersGlobalCheckBoxDisabled() {
        every { searchOrderMock.owner } returns OWNER_ID
        tester.startComponentInPage(makePanel())
        tester.assertEnabled("$PANEL_ID:form:global")
    }

    @Test
    fun withGlobalSearchOrders_withSameOwner_globalCheckBox_enabled() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID
        tester.startComponentInPage(makePanel())
        tester.assertEnabled("$PANEL_ID:form:global")
        verify(exactly = 3) { searchOrderMock.owner }
    }

    @Test
    fun withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID + 1
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
        verify(exactly = 3) { searchOrderMock.owner }
    }

    @Test
    fun changingName_forSearchOwnedByUser_addsTargetsAndSaves() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID
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
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID + 1
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }
}
