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

    private int maxRowsPerPage;

    private F filterState;

    @SpringBean
    private PaperSlimService service;

    protected PaperSlimService getService() {
        return service;
    }

    /** protected for test purposes */
    protected void setService(final PaperSlimService service) {
        this.service = service;
    }

    SortablePaperSlimProvider(final F filterState, final Integer rowsPerPage) {
        this.filterState = filterState;
        this.maxRowsPerPage = rowsPerPage;
    }

    /**
     * Retrieves the paged data as iterator according to the paging instructions from the calling component.
     * <p/>
     * <b>Note:</b>
     * <p/>
     * The offset calculation in the spring data Pageable implementation accepts the 0-based page
     * index and the page size. It later re-calculates the actual record offset from those two parameters. This
     * dictates that the page size is constant across  all pages (even the last one).
     * <p/>
     * The wicket implementation of DataTable is backed by the DataGridView. This component will adjust the page
     * size it uses to call iterator - depending on the actual number of records on the page. If the last page
     * is not full, the pageSize passed into iterator will be smaller than the theoretical page size. This screws
     * up the offset calculation of the spring data Pageable for the last page.
     * <p/>
     * I'm therefore ignoring the page size passed in from the calling component and simply use the maxRowsPerPage
     * field passed in during construction.
     *
     * TODO consider implementing a pageable implementation that directly uses the offset and recordcount instead of converting from offset to pageSize and back.
     *
     * @param offset
     *      the record offset
     * @param ignoredRecordsOnPage
     *      the size indicator - ignored!
     *
     * @see org.springframework.data.domain.AbstractPageRequest#getOffset
     * @see org.apache.wicket.markup.repeater.AbstractPageableView#getViewSize
     */
    @Override
    public Iterator<PaperSlim> iterator(final long offset, final long ignoredRecordsOnPage) {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        final int pageIndex = getPageIndex(offset, getRowsPerPage());
        return findByFilter(new PageRequest(pageIndex, getRowsPerPage(), dir, sortProp));
    }

    protected abstract Iterator<PaperSlim> findByFilter(Pageable pageable);

    private int getPageIndex(final long from, final long size) {
        return size > 0 ? (int) (from / size) : 0;
    }

    @Override
    public long size() {
        return getSize();
    }

    protected abstract long getSize();

    @Override
    public IModel<PaperSlim> model(final PaperSlim entity) {
        return new Model<PaperSlim>(entity);
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

    /**
     * Sets the number of rows per page.
     *
     * @param rowsPerPage
     */
    public void setRowsPerPage(final int rowsPerPage) {
        this.maxRowsPerPage = rowsPerPage;
    }

}
