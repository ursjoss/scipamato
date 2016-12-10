package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * Extension of the {@link SortablePaperSlimProvider} using the {@link SearchOrder} as filter class.
 *
 * @author u.joss
 */
public class SearchOrderBasedSortablePaperSlimProvider extends SortablePaperSlimProvider<SearchOrder> {

    private static final long serialVersionUID = 1L;

    public SearchOrderBasedSortablePaperSlimProvider(SearchOrder searchOrder) {
        super(searchOrder);
        Injector.get().inject(this);
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    @Override
    protected Iterator<PaperSlim> findByFilter(Pageable pageable) {
        return getService().findBySearchOrder(getFilterState(), pageable).iterator();
    }

    @Override
    protected long getSize() {
        return getService().countBySearchOrder(getFilterState());
    }
}
