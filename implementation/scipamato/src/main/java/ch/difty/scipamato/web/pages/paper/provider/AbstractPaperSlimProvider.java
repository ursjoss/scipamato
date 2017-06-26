package ch.difty.scipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.filter.PaperSlimFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.paging.PaginationRequest;
import ch.difty.scipamato.paging.Sort.Direction;
import ch.difty.scipamato.service.PaperService;
import ch.difty.scipamato.service.PaperSlimService;

/**
 * Abstract base class for data providers providing the wicket components access to the persisted paper data in the slim format.
 *
 * @author u.joss
 *
 * @param <F> the type of the filter state
 */
public abstract class AbstractPaperSlimProvider<F extends PaperSlimFilter> extends SortableDataProvider<PaperSlim, String> implements IFilterStateLocator<F> {

    private static final long serialVersionUID = 1L;

    private int maxRowsPerPage;

    private F filterState;

    @SpringBean
    private PaperSlimService service;

    @SpringBean
    private PaperService paperService;

    /**
     * Instantiate the provider with the filter and the number of rows per page.
     * @param filterState
     * @param rowsPerPage
     */
    AbstractPaperSlimProvider(final F filterState, final Integer rowsPerPage) {
        this.filterState = filterState;
        this.maxRowsPerPage = rowsPerPage;
    }

    protected PaperSlimService getService() {
        return service;
    }

    /** protected for test purposes */
    protected void setService(final PaperSlimService service) {
        this.service = service;
    }

    protected PaperService getPaperService() {
        return paperService;
    }

    /** protected for test purposes */
    protected void setPaperService(final PaperService paperService) {
        this.paperService = paperService;
    }

    /**
     * provides an iterator going through the records, starting with the {@literal first} (offset) and providing {@literal count} number of records.
     */
    @Override
    public Iterator<PaperSlim> iterator(final long first, final long count) {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        return findPage(new PaginationRequest((int) first, (int) count, dir, sortProp));
    }

    protected abstract Iterator<PaperSlim> findPage(PaginationContext pc);

    /**
     * Applies the normal filter and the sort aspect of the pageable to return all records as {@link Paper}s.
     * @return list of all papers
     */
    public List<Paper> findAllPapersByFilter() {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        return findAll(dir, sortProp);
    }

    protected abstract List<Paper> findAll(Direction dir, String sortProp);

    /**
     * Applies the normal filter and the sort aspect of the pageable to return only the ids of all papers (unpaged).
     * @return list of all paper ids
     */
    public List<Long> findAllPaperIdsByFilter() {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        return findAllIds(dir, sortProp);
    }

    protected abstract List<Long> findAllIds(Direction dir, String sortProp);

    @Override
    public long size() {
        return getSize();
    }

    protected abstract long getSize();

    @Override
    public IModel<PaperSlim> model(final PaperSlim entity) {
        return new Model<>(entity);
    }

    @Override
    public F getFilterState() {
        return filterState;
    }

    @Override
    public void setFilterState(final F filterState) {
        this.filterState = filterState;
    }

    /**
     * Return the (max) rowsPerPage (or pageSize), regardless of the number of records actually available on the page.
     * @return rows per page
     */
    public int getRowsPerPage() {
        return maxRowsPerPage;
    }

    public abstract Long getSearchOrderId();

}
