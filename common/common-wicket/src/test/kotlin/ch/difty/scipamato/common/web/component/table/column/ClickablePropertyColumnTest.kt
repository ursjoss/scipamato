package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.TestRecord
import ch.difty.scipamato.common.web.WicketBaseTest
import ch.difty.scipamato.common.web.component.SerializableConsumer
import com.nhaarman.mockitokotlin2.mock
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class ClickablePropertyColumnTest : WicketBaseTest() {

    private val consumerMock = mock<SerializableConsumer<IModel<String>>>()

    private val displayModel = Model("foo")

    private var clickPerformed: String? = null

    @Test
    fun testOnClick() {
        val property = "prop"
        val c = ClickablePropertyColumn<String, String>(displayModel, property, consumerMock)
        val clickModel = Model.of("bar")
        c.onClick(clickModel)
        verify<SerializableConsumer<IModel<String>>>(consumerMock).accept(clickModel)
    }

    @Test
    fun testOnClick_withSort() {
        val property = "prop"
        val c = ClickablePropertyColumn(
            displayModel, property, property,
            consumerMock
        )
        val clickModel = Model.of("bar")
        c.onClick(clickModel)
        verify<SerializableConsumer<IModel<String>>>(consumerMock).accept(clickModel)
    }

    @Test
    fun testOnClick_inNewTab() {
        val property = "prop"
        val c = ClickablePropertyColumn(
            displayModel, property, property,
            consumerMock, true
        )
        val clickModel = Model.of("bar")
        c.onClick(clickModel)
        verify<SerializableConsumer<IModel<String>>>(consumerMock).accept(clickModel)
    }

    @Test
    fun testPanel() {
        tester.startComponentInPage(
            ClickablePropertyColumnTestPanel("panel", SerializableConsumer { this.setVariable(it) }, false)
        )
        assertComponents()
        assertThat(clickPerformed).isNull()
    }

    @Test
    fun clickLink() {
        tester.startComponentInPage(
            ClickablePropertyColumnTestPanel("panel", SerializableConsumer { this.setVariable(it) }, false)
        )
        tester.clickLink("panel:table:body:rows:1:cells:2:cell:link")
        assertThat(clickPerformed).isEqualTo("TestRecord(id=1, name=foo)")
    }

    @Test
    fun clickLink_inNewTab() {
        tester.startComponentInPage(
            ClickablePropertyColumnTestPanel("panel", SerializableConsumer { this.setVariable(it) }, true)
        )
        tester.clickLink("panel:table:body:rows:1:cells:2:cell:link")
        assertThat(clickPerformed).isEqualTo("TestRecord(id=1, name=foo)")
    }

    private fun setVariable(trModel: IModel<TestRecord>) {
        clickPerformed = trModel
            .getObject()
            .toString()
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
