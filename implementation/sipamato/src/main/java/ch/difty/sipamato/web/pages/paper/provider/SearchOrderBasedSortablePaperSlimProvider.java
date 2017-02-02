package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.SipamatoPageRequest;

/**
 * Extension of the {@link SortablePaperSlimProvider} using the {@link SearchOrder} as filter class.
 *
 * @author u.joss
 */
public class SearchOrderBasedSortablePaperSlimProvider extends SortablePaperSlimProvider<SearchOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link SearchOrder} and a specific number of rews per page (pageSize)
     * @param searchOrder
     *          the search specification 
     * @param rowsPerPage
     */
    public SearchOrderBasedSortablePaperSlimProvider(final SearchOrder searchOrder, final int rowsPerPage) {
        super(searchOrder, rowsPerPage);
        Injector.get().inject(this);
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    /** {@inheritDoc} */
    @Override
    protected Iterator<PaperSlim> findByFilter(final Pageable pageable) {
        return getService().findBySearchOrder(getFilterState(), pageable).iterator();
    }

    /** {@inheritDoc} */
    @Override
    protected long getSize() {
        return getService().countBySearchOrder(getFilterState());
    }

    /** {@inheritDoc} */
    @Override
    protected List<Paper> findAllPapersByFilter(final Direction dir, final String sortProp) {
        return getPaperService().findBySearchOrder(getFilterState(), new SipamatoPageRequest(dir, sortProp)).getContent();
    }

}
