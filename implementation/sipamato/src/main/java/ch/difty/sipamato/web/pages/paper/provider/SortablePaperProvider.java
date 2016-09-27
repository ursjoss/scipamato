package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.service.PaperService;

/**
 * The dataprovider providing the wicket components access to the persisted paper data
 *
 * @author u.joss
 */
public class SortablePaperProvider extends SortableDataProvider<Paper, String> implements IFilterStateLocator<PaperFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService service;

    private PaperFilter filter;

    public SortablePaperProvider() {
        this(new PaperFilter());
    }

    public SortablePaperProvider(PaperFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    /** protected for test purposes */
    protected void setService(PaperService service) {
        this.service = service;
    }

    /** protected for test purposes */
    protected PaperFilter getFilter() {
        return filter;
    }

    @Override
    public Iterator<Paper> iterator(long first, long count) {
        return service.findByFilter(filter, first, count).iterator();
    }

    @Override
    public long size() {
        return service.countByFilter(filter);
    }

    @Override
    public IModel<Paper> model(Paper entity) {
        return new Model<Paper>(entity);
    }

    @Override
    public PaperFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(PaperFilter state) {
        this.filter = state;
    }

}
