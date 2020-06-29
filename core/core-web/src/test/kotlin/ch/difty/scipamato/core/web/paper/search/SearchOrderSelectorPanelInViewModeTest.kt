package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class SearchOrderSelectorPanelInViewModeTest : SearchOrderSelectorPanelTest() {

    override val mode: Mode
        get() = Mode.VIEW

    @Test
    fun withGlobalSearchOrders_withSameOwner_globalCheckBox_disabled() {
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }

    @Test
    fun withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        searchOrder.apply { owner = OWNER_ID + 1 }
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }

    @Test
    fun changingName_forGlobalNotSelfOwnedSearch_disablesName() {
        searchOrder.apply {
            isGlobal = true
            owner = OWNER_ID + 1
        }
        assertNameDisabled()
    }

    private fun assertNameDisabled() {
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:name")
    }

    @Test
    fun changingName_forGlobalSelfOwnedSearch_disablesName() {
        searchOrder.apply { isGlobal = true }
        assertNameDisabled()
    }

    @Test
    fun changingName_forNotGlobalNotSelfOwnedSearch_disablesName() {
        searchOrder.apply {
            isGlobal = true
            owner = OWNER_ID + 1
        }
        assertNameDisabled()
    }

    @Test
    fun changingName_forNotGlobalSelfOwnedSearch_doesAddTargetAndSaves() {
        searchOrder.apply { isGlobal = false }
        tester.startComponentInPage(makePanel())
        tester.executeAjaxEvent("$PANEL_ID:form:name", "change")
        verify { searchOrderServiceMock.saveOrUpdate(any()) }
    }
}
