package ch.difty.scipamato.core.web.paper;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;

/**
 * Extension of {@link AbstractPaperSlimProvider} using the {@link PaperFilter}
 * as filter class.
 *
 * @author u.joss
 */
public class PaperSlimByPaperFilterProvider extends AbstractPaperSlimProvider<PaperFilter> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    /**
     * Instantiate the provider with a {@link PaperFilter} and a specific number of
     * rows per page (pageSize)
     *
     * @param filter
     *     the paper filter search specification
     * @param rowsPerPage
     *     the max numbers of rows per page
     */
    public PaperSlimByPaperFilterProvider(@Nullable final PaperFilter filter, final int rowsPerPage) {
        super(filter != null ? filter : new PaperFilter(), rowsPerPage);
        Injector
            .get()
            .inject(this);
        setSort(IdScipamatoEntity.IdScipamatoEntityFields.ID.getFieldName(), SortOrder.DESCENDING);
    }

    @NotNull
    @Override
    protected Iterator<PaperSlim> findPage(@NotNull final PaginationContext pc) {
        return getService()
            .findPageByFilter(getFilterState(), pc)
            .iterator();
    }

    @Override
    protected long getSize() {
        return getService().countByFilter(getFilterState());
    }

    @NotNull
    @Override
    protected List<Paper> findAll(@NotNull final Direction dir, @NotNull final String sortProp) {
        return getPaperService().findPageByFilter(getFilterState(), new PaginationRequest(dir, sortProp));
    }

    @NotNull
    @Override
    protected List<Long> findAllIds(@NotNull final Direction dir, @NotNull final String sortProp) {
        return getPaperService().findPageOfIdsByFilter(getFilterState(), new PaginationRequest(dir, sortProp));
    }
}
