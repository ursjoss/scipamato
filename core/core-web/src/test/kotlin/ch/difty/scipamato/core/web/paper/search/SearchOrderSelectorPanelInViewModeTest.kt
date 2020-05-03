package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class SearchOrderSelectorPanelInViewModeTest : SearchOrderSelectorPanelTest() {

    override val mode: Mode
        get() = Mode.VIEW

    @Test
    fun withGlobalSearchOrders_withSameOwner_globalCheckBox_disabled() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }

    @Test
    fun withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID + 1
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }

    @Test
    fun changingName_forGlobalNotSelfOwnedSearch_disablesName() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.isGlobal } returns true
        every { searchOrderMock.owner } returns OWNER_ID + 1
        assertNameDisabled()
    }

    private fun assertNameDisabled() {
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:name")
    }

    @Test
    fun changingName_forGlobalSelfOwnedSearch_disablesName() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.isGlobal } returns true
        every { searchOrderMock.owner } returns OWNER_ID
        assertNameDisabled()
    }

    @Test
    fun changingName_forNotGlobalNotSelfOwnedSearch_disablesName() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.isGlobal } returns false
        every { searchOrderMock.owner } returns OWNER_ID + 1
        assertNameDisabled()
    }

    @Test
    fun changingName_forNotGlobalSelfOwnedSearch_doesAddTargetAndSaves() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.isGlobal } returns false
        every { searchOrderMock.owner } returns OWNER_ID
        tester.startComponentInPage(makePanel())
        tester.executeAjaxEvent("$PANEL_ID:form:name", "change")
        verify { searchOrderServiceMock.saveOrUpdate(any()) }
    }
}
