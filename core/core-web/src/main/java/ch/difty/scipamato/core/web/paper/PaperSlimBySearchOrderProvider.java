package ch.difty.scipamato.core.web.paper;

import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StopWatch;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.SearchOrder;

/**
 * Extension of the {@link AbstractPaperSlimProvider} using the
 * {@link SearchOrder} as filter class.
 *
 * @author u.joss
 */
@Slf4j
public class PaperSlimBySearchOrderProvider extends AbstractPaperSlimProvider<SearchOrder> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link SearchOrder} and a specific number of
     * rows per page (pageSize)
     *
     * @param searchOrder
     *     the search specification
     * @param rowsPerPage
     *     number of rows to place on the page
     */
    public PaperSlimBySearchOrderProvider(@Nullable final SearchOrder searchOrder, final int rowsPerPage) {
        super(searchOrder != null ? searchOrder : new SearchOrder(), rowsPerPage);
        Injector
            .get()
            .inject(this);
        setSort(IdScipamatoEntity.IdScipamatoEntityFields.ID.getFieldName(), SortOrder.DESCENDING);
    }

    @NotNull
    @Override
    protected Iterator<PaperSlim> findPage(@NotNull final PaginationContext pc) {
        final StopWatch watch = new StopWatch("PaperSlimBySearchOrderProvider#findPage");
        watch.start();
        if (getSearchOrderId() != null) {
            log.info("++ ------------------------------------------------");
            log.info("++ Getting iterator (p{}@{}) for so {}...", pc.getOffset(), pc.getPageSize(), getSearchOrderId());
        }
        final Iterator<PaperSlim> iterator = getService()
            .findPageBySearchOrder(getFilterState(), pc)
            .iterator();
        watch.stop();
        if (getSearchOrderId() != null) {
            log.info("++   iterator found (p{}@{}) for so {} in {} ms", pc.getOffset(), pc.getPageSize(), getSearchOrderId(),
                watch.getTotalTimeMillis());
            log.info("++ ------------------------------------------------");
        }
        return iterator;
    }

    @Override
    protected long getSize() {
        final StopWatch watch = new StopWatch("PaperSlimBySearchOrderProvider#getSize");
        watch.start();
        if (getSearchOrderId() != null)
            log.info(">> Getting size for so {}...", getSearchOrderId());
        final int size = getService().countBySearchOrder(getFilterState());
        watch.stop();
        if (getSearchOrderId() != null)
            log.info(">> size {} found for so {} in {} ms.", size, getSearchOrderId(), watch.getTotalTimeMillis());
        return size;
    }

    @NotNull
    @Override
    protected List<Paper> findAll(@NotNull final Direction dir, @NotNull final String sortProp) {
        return getPaperService().findPageBySearchOrder(getFilterState(), new PaginationRequest(dir, sortProp), getLanguageCode());
    }

    @NotNull
    @Override
    protected List<Long> findAllIds(@NotNull final Direction dir, @NotNull final String sortProp) {
        return getPaperService().findPageOfIdsBySearchOrder(getFilterState(), new PaginationRequest(dir, sortProp));
    }

    @Nullable
    @Override
    public Long getSearchOrderId() {
        return getFilterState().getId();
    }

    @Override
    public boolean isShowExcluded() {
        return getFilterState().isShowExcluded();
    }

    @Override
    public void setShowExcluded(final boolean showExcluded) {
        getFilterState().setShowExcluded(showExcluded);
    }
}
