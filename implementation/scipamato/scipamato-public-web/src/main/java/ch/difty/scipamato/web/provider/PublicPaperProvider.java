package ch.difty.scipamato.web.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;

public class PublicPaperProvider extends SortableDataProvider<PublicPaper, String> implements ISortableDataProvider<PublicPaper, String>, IFilterStateLocator<PublicPaperFilter> {

    private static final long serialVersionUID = 1L;

    private PublicPaperFilter paperFilter;
    private final int maxRowsPerPage;

    // TODO hardcoded stubbed result list. Need to replace with service calls to database
    private List<PublicPaper> list = new ArrayList<>();

    public PublicPaperProvider(final PublicPaperFilter paperFilter, final int resultPageSize) {
        Injector.get().inject(this);
        this.paperFilter = paperFilter != null ? paperFilter : new PublicPaperFilter();
        this.maxRowsPerPage = resultPageSize;
        setSort(PublicPaper.NUMBER, SortOrder.DESCENDING);

        list.add(new PublicPaper(1l, 1l, 1000, "authors1", "title1", "location1", 2016, "goals1", "methods1", "population1", "result1", "comment1"));
        list.add(new PublicPaper(2l, 2l, 1002, "authors2", "title2", "location2", 2017, "goals2", "methods2", "population2", "result2", "comment2"));
    }

    /**
     * Return the (max) rowsPerPage (or pageSize), regardless of the number of records actually available on the page.
     * @return rows per page
     */
    public int getRowsPerPage() {
        return maxRowsPerPage;
    }

    @Override
    public Iterator<? extends PublicPaper> iterator(long first, long count) {
        // TODO implement properly
        return list.iterator();
    }

    @Override
    public long size() {
        // TODO implement properly
        return list.size();
    }

    @Override
    public IModel<PublicPaper> model(PublicPaper entity) {
        return new Model<>(entity);
    }

    @Override
    public PublicPaperFilter getFilterState() {
        return paperFilter;
    }

    @Override
    public void setFilterState(final PublicPaperFilter filterState) {
        this.paperFilter = filterState;
    }

}
