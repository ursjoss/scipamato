package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;

/**
 * Abstract baseclass for dataproviders providing the wicket components access to the persisted paper data in the slim format.
 *
 * @author u.joss
 */
public abstract class SortablePaperSlimProvider<F extends PaperSlimFilter> extends SortableDataProvider<PaperSlim, String> implements IFilterStateLocator<F> {

    private static final long serialVersionUID = 1L;

    private final Integer pageSize;

    private F filterState;

    @SpringBean
    private PaperSlimService service;

    protected PaperSlimService getService() {
        return service;
    }

    /** protected for test purposes */
    protected void setService(PaperSlimService service) {
        this.service = service;
    }

    SortablePaperSlimProvider(F filterState, final Integer pageSize) {
        this.filterState = filterState;
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<PaperSlim> iterator(long offset, long actualSize) {
        int pageSizeToUse = pageSize != null ? pageSize.intValue() : (int) actualSize;
        Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        String sortProp = getSort().getProperty();
        Pageable pageable = new PageRequest(getPageIndex(offset, pageSizeToUse), (int) pageSizeToUse, dir, sortProp);
        return findByFilter(pageable);
    }

    protected abstract Iterator<PaperSlim> findByFilter(Pageable pageable);

    private int getPageIndex(long from, long size) {
        return size > 0 ? (int) (from / size) : 0;
    }

    @Override
    public long size() {
        return getSize();
    }

    protected abstract long getSize();

    @Override
    public IModel<PaperSlim> model(PaperSlim entity) {
        return new Model<PaperSlim>(entity);
    }

    @Override
    public F getFilterState() {
        return filterState;
    }

    @Override
    public void setFilterState(F filterState) {
        this.filterState = filterState;
    }

    /**
     * Return the (max) pageSize, regardless of the number of records actually available on the page.
     * @return pageSize if specified, null otherwise
     */
    public Integer getPageSize() {
        return pageSize;
    }

}
