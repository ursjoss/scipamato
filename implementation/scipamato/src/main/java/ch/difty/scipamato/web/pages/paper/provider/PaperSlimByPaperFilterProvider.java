package ch.difty.scipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.paging.PaginationRequest;
import ch.difty.scipamato.paging.Sort.Direction;
import ch.difty.scipamato.persistance.jooq.paper.PaperFilter;

/**
 * Extension of {@link AbstractPaperSlimProvider} using the {@link PaperFilter} as filter class.
 *
 * @author u.joss
 */
public class PaperSlimByPaperFilterProvider extends AbstractPaperSlimProvider<PaperFilter> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link PaperFilter} and a specific number of rows per page (pageSize)
     * @param filter
     *     the paper filter search specification
     * @param rowsPerPage
     *     the max numbers of rows per page
     */
    public PaperSlimByPaperFilterProvider(final PaperFilter filter, final int rowsPerPage) {
        super(filter != null ? filter : new PaperFilter(), rowsPerPage);
        Injector.get().inject(this);
        setSort(Paper.ID, SortOrder.DESCENDING);
    }

    /** {@inheritDoc} */
    @Override
    protected Iterator<PaperSlim> findPage(final PaginationContext pc) {
        return getService().findPageByFilter(getFilterState(), pc).iterator();
    }

    /** {@inheritDoc} */
    @Override
    protected long getSize() {
        return getService().countByFilter(getFilterState());
    }

    /** {@inheritDoc} */
    @Override
    protected List<Paper> findAll(final Direction dir, final String sortProp) {
        return getPaperService().findPageByFilter(getFilterState(), new PaginationRequest(dir, sortProp));
    }

    @Override
    protected List<Long> findAllIds(Direction dir, String sortProp) {
        return getPaperService().findPageOfIdsByFilter(getFilterState(), new PaginationRequest(dir, sortProp));
    }

    @Override
    public Long getSearchOrderId() {
        return null;
    }

}
