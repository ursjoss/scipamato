package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.TestRecord
import ch.difty.scipamato.common.web.WicketBaseTest
import ch.difty.scipamato.common.web.clickLinkSameSite
import ch.difty.scipamato.common.web.component.SerializableConsumer
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

internal class ClickablePropertyColumnTest : WicketBaseTest() {

    private val actionMock = mockk<SerializableConsumer<IModel<String>>>(relaxed = true)

    private val displayModel = Model("foo")

    private var clickPerformed: String? = null

    @Test
    fun testOnClick() {
        val property = "prop"
        val c = ClickablePropertyColumn<String, String>(displayModel, property, actionMock)
        val clickModel = Model.of("bar")
        c.onClick(clickModel)
        verify { actionMock.accept(clickModel) }
    }

    @Test
    fun testOnClick_withSort() {
        val property = "prop"
        val c = ClickablePropertyColumn(
            displayModel, property, actionMock,
            property
        )
        val clickModel = Model.of("bar")
        c.onClick(clickModel)
        verify { actionMock.accept(clickModel) }
    }

    @Test
    fun testOnClick_inNewTab() {
        val property = "prop"
        val c = ClickablePropertyColumn(
            displayModel, property, actionMock,
            property, true
        )
        val clickModel = Model.of("bar")
        c.onClick(clickModel)
        verify { actionMock.accept(clickModel) }
    }

    @Test
    fun testPanel() {
        tester.startComponentInPage(
            ClickablePropertyColumnTestPanel("panel", ::setVariable, false)
        )
        assertComponents()
        clickPerformed.shouldBeNull()
    }

    @Test
    fun clickLink() {
        tester.startComponentInPage(
            ClickablePropertyColumnTestPanel("panel", ::setVariable, false)
        )
        tester.clickLinkSameSite("panel:table:body:rows:1:cells:2:cell:link")
        clickPerformed shouldBeEqualTo "TestRecord(id=1, name=foo)"
    }

    @Test
    fun clickLink_inNewTab() {
        tester.startComponentInPage(
            ClickablePropertyColumnTestPanel("panel", ::setVariable, true)
        )
        tester.clickLinkSameSite("panel:table:body:rows:1:cells:2:cell:link")
        clickPerformed shouldBeEqualTo "TestRecord(id=1, name=foo)"
    }

    private fun setVariable(trModel: IModel<TestRecord>?) {
        clickPerformed = trModel?.getObject()?.toString()
    }

    private fun assertComponents() {
        tester.assertComponent("panel", ClickablePropertyColumnTestPanel::class.java)
        tester.assertComponent("panel:table", DefaultDataTable::class.java)
        tester.assertComponent("panel:table:body:rows", DataGridView::class.java)
        tester.assertLabel("panel:table:body:rows:1:cells:1:cell", "foo")
        tester.assertComponent("panel:table:body:rows:1:cells:2:cell", Panel::class.java)
        tester.assertComponent("panel:table:body:rows:1:cells:2:cell:link", Link::class.java)
        tester.assertModelValue("panel:table:body:rows:1:cells:2:cell:link", TestRecord(1, "foo"))
        tester.assertModelValue("panel:table:body:rows:1:cells:2:cell:link:label", "foo")
    }
}
