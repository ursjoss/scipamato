package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.paging.PaginationRequest;
import ch.difty.sipamato.paging.Sort.Direction;

/**
 * Extension of the {@link AbstractPaperSlimProvider} using the {@link SearchOrder} as filter class.
 *
 * @author u.joss
 */
public class PaperSlimBySearchOrderProvider extends AbstractPaperSlimProvider<SearchOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link SearchOrder} and a specific number of rews per page (pageSize)
     * @param searchOrder
     *          the search specification 
     * @param rowsPerPage
     */
    public PaperSlimBySearchOrderProvider(final SearchOrder searchOrder, final int rowsPerPage) {
        super(searchOrder != null ? searchOrder : new SearchOrder(), rowsPerPage);
        Injector.get().inject(this);
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    /** {@inheritDoc} */
    @Override
    protected Iterator<PaperSlim> findPage(final PaginationContext pc) {
        return getService().findPageBySearchOrder(getFilterState(), pc).iterator();
    }

    /** {@inheritDoc} */
    @Override
    protected long getSize() {
        return getService().countBySearchOrder(getFilterState());
    }

    /** {@inheritDoc} */
    @Override
    protected List<Paper> findAll(final Direction dir, final String sortProp) {
        return getPaperService().findPageBySearchOrder(getFilterState(), new PaginationRequest(dir, sortProp));
    }

}
