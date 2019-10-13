package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.TestRecord
import ch.difty.scipamato.common.web.WicketBaseTest
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.TagTester
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.Test

internal class LinkIconColumnTest : WicketBaseTest() {

    private var clickPerformed: String? = null

    private val lc = object : LinkIconColumn<TestRecord>(Model.of("headerText")) {
        override fun createIconModel(rowModel: IModel<TestRecord>): IModel<String> = Model.of("the iconModel")

        override fun onClickPerformed(target: AjaxRequestTarget, rowModel: IModel<TestRecord>, link: AjaxLink<Void>) {
            clickPerformed = rowModel.getObject()?.toString()
        }
    }

    private fun newPanelWithTitle(title: String?): LinkIconColumnTestPanel =
        object : LinkIconColumnTestPanel(ID, title?.let { Model.of(it) }) {
            override fun onClickPerformed(rowModel: IModel<TestRecord>?, link: AjaxLink<Void>) {
                clickPerformed = rowModel?.getObject()?.toString()
            }
        }

    @Test
    fun testPanel_withTitle() {
        tester.startComponentInPage(newPanelWithTitle("the title"))
        assertComponents()
        assertImageTitle()
        assertThat(clickPerformed).isNull()
    }

    private fun assertComponents() {
        tester.assertComponent("panel", LinkIconColumnTestPanel::class.java)
        tester.assertComponent("panel:table", DefaultDataTable::class.java)
        tester.assertComponent("panel:table:body:rows", DataGridView::class.java)
        tester.assertLabel("panel:table:body:rows:1:cells:1:cell", "foo")
        tester.assertComponent("panel:table:body:rows:1:cells:2:cell", LinkIconPanel::class.java)
        tester.assertModelValue("panel:table:body:rows:1:cells:2:cell", "fa fa-fw fa-filter")
        tester.assertLabel("panel:table:topToolbars:toolbars:2:headers:2:header:label", "linkIconColumnLabel")
    }

    private fun assertImageTitle() {
        val responseTxt = tester
            .lastResponse
            .document
        val tagTester = TagTester.createTagByName(responseTxt, "i")
        assertThat(tagTester.getAttribute("title")).isEqualTo("the title")
    }

    @Test
    fun testPanel_withoutTitle() {
        tester.startComponentInPage(newPanelWithTitle(null))
        assertComponents()
        assertNoImageTitle()
        assertThat(clickPerformed).isNull()
    }

    private fun assertNoImageTitle() {
        val responseTxt = tester.lastResponse.document
        val tagTester = TagTester.createTagByName(responseTxt, "i")
        assertThat(tagTester.getAttribute("title")).isNull()
    }

    @Test
    fun clickingLink() {
        tester.startComponentInPage(newPanelWithTitle("foo"))
        tester.clickLink("panel:table:body:rows:1:cells:2:cell:link")
        assertThat(clickPerformed).isEqualTo("TestRecord(id=1, name=foo)")
    }

    @Test
    fun cssClassIsNull_canSetLater() {
        assertThat(lc.displayModel.getObject()).isEqualTo("headerText")
    }

    @Test
    fun creatingTitleModel_returnsNull() {
        assertThat(lc.createTitleModel(Model.of(TestRecord(1, "foo"))) == null).isTrue()
    }

    companion object {
        private const val ID = "panel"
    }
}
