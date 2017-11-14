package ch.difty.scipamato.web.provider;

import java.awt.print.Paper;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.web.filter.PaperFilter;

public class PaperProvider extends SortableDataProvider<Paper, String> implements IFilterStateLocator<PaperFilter> {

    private static final long serialVersionUID = 1L;

    public PaperProvider(PaperFilter paperFilter, int resultPageSize) {
        AssertAs.notNull(paperFilter, "paperFilter");
    }

    @Override
    public Iterator<? extends Paper> iterator(long first, long count) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IModel<Paper> model(Paper object) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaperFilter getFilterState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFilterState(PaperFilter state) {
        // TODO Auto-generated method stub

    }

}
