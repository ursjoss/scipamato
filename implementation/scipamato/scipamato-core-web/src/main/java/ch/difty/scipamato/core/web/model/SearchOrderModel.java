package ch.difty.scipamato.core.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.SearchOrderService;

/**
 * Model that offers a wicket page to load a certain amount of
 * {@link SearchOrder}s visible to user with provided id.
 *
 * @author u.joss
 */
public class SearchOrderModel extends LoadableDetachableModel<List<SearchOrder>> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private SearchOrderService service;

    private final int owner;
    private final int maxRows;

    public SearchOrderModel(int userId, int maxRows) {
        Injector.get()
            .inject(this);
        this.owner = userId;
        this.maxRows = maxRows;
    }

    @Override
    protected List<SearchOrder> load() {
        final SearchOrderFilter filter = new SearchOrderFilter();
        filter.setOwnerIncludingGlobal(owner);
        final PaginationContext pc = new PaginationRequest(0, maxRows, Direction.ASC, "global");
        return service.findPageByFilter(filter, pc);
    }

}
