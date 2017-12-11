package ch.difty.scipamato.common.web.component.table.column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.common.web.TestRecord;
import ch.difty.scipamato.common.web.component.SerializableConsumer;

class ClickablePropertyColumnTestPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private final SerializableConsumer<IModel<TestRecord>> consumer;

    public ClickablePropertyColumnTestPanel(String id, SerializableConsumer<IModel<TestRecord>> consumer) {
        super(id);
        this.consumer = consumer;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final List<IColumn<TestRecord, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<TestRecord, String>(Model.of("name"), "name", "name"));
        columns.add(makeClickableColumn("test", consumer));

        add(new DefaultDataTable<TestRecord, String>("table", columns, new TestDataProvider(), 10));
    }

    private IColumn<TestRecord, String> makeClickableColumn(String id, SerializableConsumer<IModel<TestRecord>> consumer) {
        return new ClickablePropertyColumn<TestRecord, String>(Model.of("linkIconColumnLabel"), "name", consumer) {
            private static final long serialVersionUID = 1L;
        };
    }

    static class TestDataProvider extends SortableDataProvider<TestRecord, String> {

        private static final long serialVersionUID = 1L;

        @Override
        public Iterator<TestRecord> iterator(long first, long count) {
            return Arrays.asList(new TestRecord(1, "foo")).iterator();
        }

        @Override
        public long size() {
            return 1;
        }

        @Override
        public IModel<TestRecord> model(TestRecord record) {
            return Model.of(record);
        }
    };
}