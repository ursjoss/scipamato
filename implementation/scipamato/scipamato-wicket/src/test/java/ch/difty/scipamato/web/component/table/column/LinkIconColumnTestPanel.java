package ch.difty.scipamato.web.component.table.column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.web.component.TestRecord;

abstract class LinkIconColumnTestPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private final IModel<String> titleModel;

    public LinkIconColumnTestPanel(String id, IModel<String> titleModel) {
        super(id);
        this.titleModel = titleModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final List<IColumn<TestRecord, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<TestRecord, String>(Model.of("name"), "name", "name"));
        columns.add(makeLinkIconColumn("test"));

        add(new DefaultDataTable<TestRecord, String>("table", columns, new TestDataProvider(), 10));
    }

    private IColumn<TestRecord, String> makeLinkIconColumn(String id) {
        return new LinkIconColumn<TestRecord>(Model.of("linkIconColumnLabel")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<TestRecord> rowModel) {
                return Model.of("fa fa-fw fa-filter");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<TestRecord> rowModel) {
                return titleModel;
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<TestRecord> rowModel, AjaxLink<Void> link) {
                LinkIconColumnTestPanel.this.onClickPerformed(rowModel, link);
            }
        };
    }

    protected abstract void onClickPerformed(IModel<TestRecord> rowModel, AjaxLink<Void> link);

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