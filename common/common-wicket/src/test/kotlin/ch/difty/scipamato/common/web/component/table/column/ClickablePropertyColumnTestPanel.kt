package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.TestRecord
import ch.difty.scipamato.common.web.component.SerializableConsumer
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

internal class ClickablePropertyColumnTestPanel(
    id: String,
    private val action: SerializableConsumer<IModel<TestRecord>>,
    private val inNewTab: Boolean,
) : Panel(id) {

    override fun onInitialize() {
        super.onInitialize()

        val columns = listOf(
            PropertyColumn(Model.of("name"), "name", "name"),
            makeClickableColumn("test", action)
        )
        add(DefaultDataTable("table", columns, TestDataProvider(), 10))
    }

    @Suppress("SameParameterValue")
    private fun makeClickableColumn(
        id: String,
        action: SerializableConsumer<IModel<TestRecord>>,
    ): IColumn<TestRecord, String> =
        object : ClickablePropertyColumn<TestRecord, String>(Model.of(id), "name", action, null, inNewTab) {}

    internal class TestDataProvider : SortableDataProvider<TestRecord, String>() {
        override fun iterator(first: Long, count: Long): Iterator<TestRecord> = listOf(TestRecord(1, "foo")).iterator()
        override fun size(): Long = 1
        override fun model(record: TestRecord): IModel<TestRecord> = Model.of(record)
    }
}
