package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.paging.PaginationRequest;
import ch.difty.sipamato.paging.Sort.Direction;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

/**
 * Extension of {@link SortablePaperSlimProvider} using the {@link PaperFilter} as filter class.
 *
 * @author u.joss
 */
public class FilterBasedSortablePaperSlimProvider extends SortablePaperSlimProvider<PaperFilter> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link PaperFilter} and a specific number of rews per page (pageSize)
     * @param filter
     *     the paper filter search specification
     * @param rowsPerPage
     */
    public FilterBasedSortablePaperSlimProvider(final PaperFilter filter, final int rowsPerPage) {
        super(filter, rowsPerPage);
        Injector.get().inject(this);
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
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

}
