package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import io.mockk.verify
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

internal class SearchOrderSelectorPanelInEditModeTest : SearchOrderSelectorPanelTest() {

    override val mode: Mode
        get() = Mode.EDIT

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

    @Test
    fun deleteButton_withNoSearchSelected_isDisabled() {
        tester.startComponentInPage(SearchOrderSelectorPanel(PANEL_ID, Model.of(), mode))
        tester.assertDisabled("$PANEL_ID:form:delete")
    }

    @Test
    fun deleteButton_withGlobalSearchOfOtherUserSelected_isDisabled() {
        searchOrder.apply {
            owner = OWNER_ID + 1
            isGlobal = true
        }
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:delete")
    }


    @Test
    fun deleteButton_withGlobalSearchOfCurrentUserSelected_isEnabled() {
        searchOrder.apply {
            isGlobal = true
        }
        tester.startComponentInPage(makePanel())
        tester.assertEnabled("$PANEL_ID:form:delete")
    }

    @Test
    fun deleteButton_withLocalSearchOfCurrentUserSelected_isEnabled() {
        tester.startComponentInPage(makePanel())
        tester.assertEnabled("$PANEL_ID:form:delete")
        verify(exactly = 0) { searchOrderServiceMock.remove(any()) }
    }

    @Test
    fun deleting_doesDelete() {
        // Why isn't it respecting the ConfirmationBehavior in the test setting?
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:form:delete")
        verify(exactly = 1) { searchOrderServiceMock.remove(any()) }
        tester.assertRenderedPage(PaperSearchPage::class.java)
    }
}
