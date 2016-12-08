package ch.difty.sipamato.web.pages.paper.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Builds up the {@link SearchCondition} provider based on the {@link SearchOrder} passed into the constructor as model,
 * exposing the list of {@link SearchCondition}s contained within.
 *
 * @author u.joss
 */
public class SearchConditionProvider extends SortableDataProvider<SearchCondition, String> {

    private static final long serialVersionUID = 1L;

    private final IModel<SearchOrder> searchOrderModel;

    public SearchConditionProvider(final IModel<SearchOrder> searchOrderModel) {
        AssertAs.notNull(searchOrderModel, "searchOrderModel");
        AssertAs.notNull(searchOrderModel.getObject(), "searchOrder");
        this.searchOrderModel = searchOrderModel;
    }

    @Override
    public Iterator<SearchCondition> iterator(final long offset, final long size) {
        // TODO currently ignoring offset and size. Might need to implement
        return getConditions().iterator();
    }

    /*
     * The modelObject can become null if the external model changes after instantiation of the provider
     */
    private List<SearchCondition> getConditions() {
        final SearchOrder searchOrder = searchOrderModel.getObject();
        return searchOrder != null ? searchOrder.getSearchConditions() : new ArrayList<SearchCondition>();
    }

    @Override
    public long size() {
        return getConditions().size();
    }

    @Override
    public IModel<SearchCondition> model(final SearchCondition searchCondition) {
        return new Model<SearchCondition>(searchCondition);
    }

}
