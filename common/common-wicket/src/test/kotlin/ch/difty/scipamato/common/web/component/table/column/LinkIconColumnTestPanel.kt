package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.TestRecord
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

internal abstract class LinkIconColumnTestPanel(
    id: String,
    private val titleModel: IModel<String>?,
) : Panel(id) {

    override fun onInitialize() {
        super.onInitialize()

        val columns = listOf(
            PropertyColumn(Model.of("name"), "name", "name"),
            makeLinkIconColumn()
        )

        add(DefaultDataTable("table", columns, TestDataProvider(), 10))
    }

    private fun makeLinkIconColumn(): IColumn<TestRecord, String?> =
        object : LinkIconColumn<TestRecord>(Model.of("linkIconColumnLabel")) {
            private val serialVersionUID: Long = 1L
            override fun createIconModel(rowModel: IModel<TestRecord>): IModel<String> = Model.of("fa fa-fw fa-filter")
            override fun createTitleModel(rowModel: IModel<TestRecord>): IModel<String>? = titleModel
            override fun onClickPerformed(
                target: AjaxRequestTarget,
                rowModel: IModel<TestRecord>,
                link: AjaxLink<Void>,
            ) {
                this@LinkIconColumnTestPanel.onClickPerformed(rowModel, link)
            }
        }


    protected abstract fun onClickPerformed(rowModel: IModel<TestRecord>, link: AjaxLink<Void>)

    internal class TestDataProvider : SortableDataProvider<TestRecord, String>() {
        override fun iterator(first: Long, count: Long): Iterator<TestRecord> = listOf(TestRecord(1, "foo")).iterator()
        override fun size(): Long = 1
        override fun model(record: TestRecord): IModel<TestRecord> = Model.of(record)

        companion object {
            private const val serialVersionUID: Long = 1L
        }
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
