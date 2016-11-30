package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.SimplePaperFilter;
import ch.difty.sipamato.service.PaperService;

/**
 * The dataprovider providing the wicket components access to the persisted paper data
 *
 * @author u.joss
 */
public class SortablePaperProvider extends SortableDataProvider<Paper, String> implements IFilterStateLocator<SimplePaperFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService service;

    private SimplePaperFilter filter;

    public SortablePaperProvider() {
        this(new SimplePaperFilter());
    }

    public SortablePaperProvider(SimplePaperFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    /** protected for test purposes */
    protected void setService(PaperService service) {
        this.service = service;
    }

    /** protected for test purposes */
    protected SimplePaperFilter getFilter() {
        return filter;
    }

    @Override
    public Iterator<Paper> iterator(long offset, long size) {
        Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        String sortProp = getSort().getProperty();
        Pageable pageable = new PageRequest(getPageIndex(offset, size), (int) size, dir, sortProp);
        return service.findByFilter(filter, pageable).iterator();
    }

    private int getPageIndex(long from, long size) {
        return (int) (from / size);
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
    public SimplePaperFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(SimplePaperFilter state) {
        this.filter = state;
    }

}
