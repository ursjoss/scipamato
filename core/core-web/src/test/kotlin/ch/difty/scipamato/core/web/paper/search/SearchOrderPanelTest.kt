package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.common.web.component.table.column.LinkIconPanel
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import ch.difty.scipamato.core.web.common.PanelTest
import ch.difty.scipamato.newFormTesterSameSite
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

internal abstract class SearchOrderPanelTest : PanelTest<SearchOrderPanel>() {

    override fun makePanel(): SearchOrderPanel {
        val sc = SearchCondition()
        sc.addSearchTerm(SearchTerm.newSearchTerm(1, SearchTermType.STRING.id, 1, "authors", "foo"))
        val conditions = listOf(sc)
        val searchOrder = SearchOrder(conditions)
        searchOrder.id = 5L
        return SearchOrderPanel(PANEL_ID, Model.of(searchOrder), mode!!)
    }

    abstract val mode: Mode?

    override fun assertSpecificComponents() {
        val b = PANEL_ID
        tester.assertComponent(b, Panel::class.java)
        assertForm("$b:form")
    }

    private fun assertForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        val bb = "$b:addSearchCondition"
        tester.assertComponent(bb, BootstrapAjaxButton::class.java)
        tester.assertModelValue(bb, "Add Search Condition")
        assertSearchConditions("$b:searchConditions")
    }

    private fun assertSearchConditions(b: String) {
        tester.assertComponent(b, BootstrapDefaultDataTable::class.java)
        tester.assertComponent("$b:body", WebMarkupContainer::class.java)
        tester.assertComponent("$b:body:rows", DataGridView::class.java)
        tester.assertLabel("$b:body:rows:1:cells:1:cell:link:label", "foo")
        tester.assertComponent("$b:body:rows:1:cells:2:cell", LinkIconPanel::class.java)
        tester.assertComponent("$b:body:rows:1:cells:2:cell:link", AjaxLink::class.java)
        tester.assertLabel("$b:body:rows:1:cells:2:cell:link:image", "")
    }

    @Test
    fun newButtonIsEnabled_ifSearchOrderIdPresent() {
        tester.startComponentInPage(makePanel())
        tester.isEnabled("$PANEL_ID:form:addSearchCondition")
    }

    @Test
    fun newButtonIsDisabled_ifSearchOrderIdNotPresent() {
        tester.startComponentInPage(SearchOrderPanel(PANEL_ID, Model.of(SearchOrder()), mode!!))
        tester.isDisabled("$PANEL_ID:form:addSearchCondition")
    }

    @Test
    fun clickingNewButton_forwardsToPaperSearchCriteriaPage() {
        tester.startComponentInPage(makePanel())
        val formTester = tester.newFormTesterSameSite("$PANEL_ID:form", false)
        formTester.submit("addSearchCondition")
        tester.assertRenderedPage(PaperSearchCriteriaPage::class.java)
    }

    @Test
    fun clickingLink_opensPaperSearchCriteriaPage() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:form:searchConditions:body:rows:1:cells:1:cell:link")
        tester.assertRenderedPage(PaperSearchCriteriaPage::class.java)
    }

    @Test
    fun searchOrderIdDefined_withRegularModel() {
        val p = makePanel()
        p.isSearchOrderIdDefined.shouldBeTrue()
    }

    @Test
    fun searchOrderIdDefined_withNullModel() {
        val p = SearchOrderPanel(PANEL_ID, null, mode!!)
        p.isSearchOrderIdDefined.shouldBeFalse()
    }

    @Test
    fun searchOrderIdDefined_withModelOFSearchOrderWIthNullId() {
        val searchOrder = SearchOrder()
        searchOrder.id.shouldBeNull()
        val p = SearchOrderPanel(PANEL_ID, Model.of(searchOrder), mode!!)
        p.isSearchOrderIdDefined.shouldBeFalse()
    }
}
