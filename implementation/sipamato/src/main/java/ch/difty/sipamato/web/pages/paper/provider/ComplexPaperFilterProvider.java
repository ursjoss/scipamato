package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.ComplexPaperFilter;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Builds up the {@link ComplexPaperFilter} provider based on the {@link SearchOrder} passed into the constructor as model,
 * exposing the list of {@link ComplexPaperFilter}s contained within.
 *
 * @author u.joss
 */
public class ComplexPaperFilterProvider extends SortableDataProvider<ComplexPaperFilter, String> {

    private static final long serialVersionUID = 1L;

    private final List<ComplexPaperFilter> filters;

    public ComplexPaperFilterProvider(final IModel<SearchOrder> searchOrderModel) {
        AssertAs.notNull(searchOrderModel, "searchOrderModel");
        AssertAs.notNull(searchOrderModel.getObject(), "searchOrder.filters");
        this.filters = searchOrderModel.getObject().getFilters();
    }

    @Override
    public Iterator<ComplexPaperFilter> iterator(final long offset, final long size) {
        // TODO currently ignoring offset and size. Might need to implement
        return filters.iterator();
    }

    @Override
    public long size() {
        return filters.size();
    }

    @Override
    public IModel<ComplexPaperFilter> model(final ComplexPaperFilter filter) {
        return new Model<ComplexPaperFilter>(filter);
    }

}
