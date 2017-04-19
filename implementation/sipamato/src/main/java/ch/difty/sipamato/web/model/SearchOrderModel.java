package ch.difty.sipamato.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.paging.PaginationRequest;
import ch.difty.sipamato.paging.Sort.Direction;
import ch.difty.sipamato.persistance.jooq.search.SearchOrderFilter;
import ch.difty.sipamato.service.SearchOrderService;

/**
 * Model that offers a wicket page to load a certain amount of {@link SearchOrder}s visible to user with provided id.
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
        Injector.get().inject(this);
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
