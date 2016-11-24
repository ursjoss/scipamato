package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * Simple implementation of SortablePaperSlimProvider using the {@link SimplePaperFilter} as filter class.
 *
 * @author u.joss
 */
public class SimpleSortablePaperSlimProvider extends SortablePaperSlimProvider<SimplePaperFilter> {

    private static final long serialVersionUID = 1L;

    public SimpleSortablePaperSlimProvider() {
        this(new SimplePaperFilter());
    }

    public SimpleSortablePaperSlimProvider(SimplePaperFilter filter) {
        super(filter);
        Injector.get().inject(this);
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    @Override
    protected Iterator<PaperSlim> findByFilter(Pageable pageable) {
        return getService().findByFilter(getFilterState(), pageable).iterator();
    }

    @Override
    protected long getSize() {
        return getService().countByFilter(getFilterState());
    }
}
