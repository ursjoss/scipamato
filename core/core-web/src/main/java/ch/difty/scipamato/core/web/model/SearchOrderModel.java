package ch.difty.scipamato.core.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.common.web.model.InjectedLoadableDetachableModel;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.SearchOrderService;

/**
 * Model that offers a wicket page to load a certain amount of
 * {@link SearchOrder}s visible to user with provided id.
 *
 * @author u.joss
 */
public class SearchOrderModel extends InjectedLoadableDetachableModel<SearchOrder> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SpringBean
    private SearchOrderService service;

    private final int owner;
    private final int maxRows;

    public SearchOrderModel(final int userId, final int maxRows) {
        super();
        this.owner = userId;
        this.maxRows = maxRows;
    }

    @NotNull
    @Override
    protected List<SearchOrder> load() {
        final SearchOrderFilter filter = new SearchOrderFilter();
        filter.setOwnerIncludingGlobal(owner);
        final PaginationContext pc = new PaginationRequest(0, maxRows, Direction.DESC, "id");
        return service.findPageByFilter(filter, pc);
    }
}
