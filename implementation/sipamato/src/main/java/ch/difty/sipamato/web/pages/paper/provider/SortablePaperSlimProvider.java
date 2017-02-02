package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.SipamatoPageRequest;
import ch.difty.sipamato.service.PaperService;
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

    @SpringBean
    private PaperService paperService;

    protected PaperService getPaperService() {
        return paperService;
    }

    /** protected for test purposes */
    protected void setPaperService(final PaperService paperService) {
        this.paperService = paperService;
    }

    SortablePaperSlimProvider(final F filterState, final Integer rowsPerPage) {
        this.filterState = filterState;
        this.maxRowsPerPage = rowsPerPage;
    }

    /**
     * provides an iterator going through the records, starting with the {@literal first} (offset) and providing {@literal count}
     * number of records.
     */
    @Override
    public Iterator<PaperSlim> iterator(final long first, final long count) {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        return findByFilter(new SipamatoPageRequest((int) first, maxRowsPerPage, (int) count, dir, sortProp));
    }

    protected abstract Iterator<PaperSlim> findByFilter(Pageable pageable);

    /**
     * Applies the normal filter and the sort aspect of the pageable to return all records as {@link Paper}s.
     * @return list of all papers
     */
    public List<Paper> findAllPapersByFilter() {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        return findAllPapersByFilter(dir, sortProp);
    }

    protected abstract List<Paper> findAllPapersByFilter(Direction dir, String sortProp);

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

}
