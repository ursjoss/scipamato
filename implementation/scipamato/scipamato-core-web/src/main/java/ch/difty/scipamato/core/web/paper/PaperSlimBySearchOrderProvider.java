package ch.difty.scipamato.core.web.paper;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.projection.PaperSlim;

/**
 * Extension of the {@link AbstractPaperSlimProvider} using the
 * {@link SearchOrder} as filter class.
 *
 * @author u.joss
 */
public class PaperSlimBySearchOrderProvider extends AbstractPaperSlimProvider<SearchOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link SearchOrder} and a specific number of
     * rews per page (pageSize)
     *
     * @param searchOrder
     *            the search specification
     * @param rowsPerPage
     */
    public PaperSlimBySearchOrderProvider(final SearchOrder searchOrder, final int rowsPerPage) {
        super(searchOrder != null ? searchOrder : new SearchOrder(), rowsPerPage);
        Injector.get()
            .inject(this);
        setSort(Paper.ID, SortOrder.DESCENDING);
    }

    @Override
    protected Iterator<PaperSlim> findPage(final PaginationContext pc) {
        return getService().findPageBySearchOrder(getFilterState(), pc)
            .iterator();
    }

    @Override
    protected long getSize() {
        return getService().countBySearchOrder(getFilterState());
    }

    @Override
    protected List<Paper> findAll(final Direction dir, final String sortProp) {
        return getPaperService().findPageBySearchOrder(getFilterState(), new PaginationRequest(dir, sortProp),
            getLanguageCode());
    }

    @Override
    protected List<Long> findAllIds(Direction dir, String sortProp) {
        return getPaperService().findPageOfIdsBySearchOrder(getFilterState(), new PaginationRequest(dir, sortProp));
    }

    @Override
    public Long getSearchOrderId() {
        return getFilterState().getId();
    }

    @Override
    public boolean isShowExcluded() {
        return getFilterState().isShowExcluded();
    }

    @Override
    public void setShowExcluded(boolean showExcluded) {
        getFilterState().setShowExcluded(showExcluded);
    }
}
