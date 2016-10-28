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
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;

/**
 * The dataprovider providing the wicket components access to the persisted paper data in the slim format
 *
 * @author u.joss
 */
public class SortablePaperSlimProvider extends SortableDataProvider<PaperSlim, String> implements IFilterStateLocator<PaperFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperSlimService service;

    private PaperFilter filter;

    public SortablePaperSlimProvider() {
        this(new PaperFilter());
    }

    public SortablePaperSlimProvider(PaperFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    /** protected for test purposes */
    protected void setService(PaperSlimService service) {
        this.service = service;
    }

    /** protected for test purposes */
    protected PaperFilter getFilter() {
        return filter;
    }

    @Override
    public Iterator<PaperSlim> iterator(long offset, long size) {
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
    public IModel<PaperSlim> model(PaperSlim entity) {
        return new Model<PaperSlim>(entity);
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
