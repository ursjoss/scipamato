package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.web.common.PanelTest
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal abstract class SearchOrderSelectorPanelTest : PanelTest<SearchOrderSelectorPanel>() {

    @MockK(relaxed = true)
    lateinit var searchOrderMock: SearchOrder

    private val searchConditions: List<SearchCondition> = ArrayList()

    override fun makePanel(): SearchOrderSelectorPanel =
        SearchOrderSelectorPanel(PANEL_ID, Model.of(searchOrderMock), mode)

    abstract val mode: Mode

    override fun setUpHook() {
        super.setUpHook()
        val searchOrders: List<SearchOrder> = listOf(
            searchOrderMock,
            SearchOrder(20L, "soName", OWNER_ID, true, searchConditions, null)
        )
        every { searchOrderServiceMock.findPageByFilter(any(), any()) } returns searchOrders
        every { searchOrderServiceMock.saveOrUpdate(any()) } returns searchOrderMock
        every { searchOrderMock.id } returns ID
    }

    override fun assertSpecificComponents() {
        val b = PANEL_ID
        tester.assertComponent(b, Panel::class.java)
        assertForm("$b:form")
    }

    private fun assertForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        tester.assertComponent("$b:searchOrder", BootstrapSelect::class.java)
        tester.assertLabel("$b:nameLabel", "Name")
        tester.assertComponent("$b:name", TextField::class.java)
        tester.assertLabel("$b:globalLabel", "Global")
        tester.assertComponent("$b:global", CheckBoxX::class.java)
        tester.assertComponent("$b:new", AjaxSubmitLink::class.java)
        tester.assertComponent("$b:delete", AjaxSubmitLink::class.java)
    }

    @Test
    fun loadingPage_withSearchOrderWithoutOverrides_hidesShowExclusionStuff() {
        every { searchOrderMock.excludedPaperIds } returns emptyList()
        tester.startComponentInPage(makePanel())
        val b = "panel:form:showExcluded"
        tester.assertInvisible(b)
        tester.assertInvisible(b + "Label")
    }

    @Test
    fun loadingPage_withSearchOrderWithOverrides_showsShowExcludedStuff() {
        every { searchOrderMock.excludedPaperIds } returns listOf(3L)
        every { searchOrderMock.isShowExcluded } returns false
        tester.startComponentInPage(makePanel())
        val b = "panel:form:showExcluded"
        tester.assertComponent(b, AjaxCheckBox::class.java)
        tester.assertLabel(b + "Label", "Show Exclusions")
        verify(exactly = 3) { searchOrderMock.excludedPaperIds }
    }

    @Test
    fun changingSearchOrderSelection_addsTargetsAndSendsEvent() {
        tester.startComponentInPage(makePanel())
        tester.executeAjaxEvent("$PANEL_ID:form:searchOrder", "change")
        val b = "$PANEL_ID:form:"
        tester.assertComponentOnAjaxResponse("${b}global")
        tester.assertComponentOnAjaxResponse("${b}name")
        tester.assertComponentOnAjaxResponse("${b}showExcluded")
        tester.assertComponentOnAjaxResponse("${b}showExcludedLabel")

        // TODO how to assert the event was actually broadcast
    }

    @Test
    fun loadingPage_withSearchOrderWithDifferentOwner_rendersGlobalCheckBoxDisabled() {
        every { searchOrderMock.owner } returns OWNER_ID + 1
        tester.startComponentInPage(makePanel())
        tester.assertDisabled("$PANEL_ID:form:global")
    }

    @Test
    fun testSubmittingWithNewButton_createsNewSearchOrder() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID
        tester.startComponentInPage(makePanel())
        val formTester = tester.newFormTester("$PANEL_ID:form")
        formTester.submit("new")
        val b = "$PANEL_ID:form:"
        tester.assertComponentOnAjaxResponse("${b}global")
        tester.assertComponentOnAjaxResponse("${b}name")
        tester.assertComponentOnAjaxResponse("${b}showExcluded")
        tester.assertComponentOnAjaxResponse("${b}showExcludedLabel")
        verify(exactly = 11) { searchOrderMock.id }
        verify(exactly = 2) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 0) { searchOrderServiceMock.saveOrUpdate(searchOrderMock) }
    }

    @Test
    fun testSubmittingWithDeleteButton_deletesSearchOrder() {
        every { searchOrderMock.name } returns VALID_NAME
        every { searchOrderMock.owner } returns OWNER_ID
        tester.startComponentInPage(makePanel())
        val b = "$PANEL_ID:form"
        val formTester = tester.newFormTester(b)
        formTester.submit("delete")
        tester.assertRenderedPage(PaperSearchPage::class.java)
        verify(exactly = 10) { searchOrderMock.id }
        verify(exactly = 3) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify { searchOrderServiceMock.remove(searchOrderMock) }
    }

    companion object {
        private const val ID = 17L
        const val VALID_NAME = "soName"
        const val OWNER_ID = 2
    }
}
