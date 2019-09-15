package ch.difty.scipamato.core.web.paper;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.search.SearchCondition;

/**
 * Builds up the {@link SearchCondition} provider based on the model passed into
 * the constructor.
 *
 * @author u.joss
 */
public class SearchConditionProvider extends SortableDataProvider<SearchCondition, String> {

    private static final long serialVersionUID = 1L;

    private final IModel<List<SearchCondition>> searchConditionsModel;

    public SearchConditionProvider(final IModel<List<SearchCondition>> searchConditionsModel) {
        AssertAs.INSTANCE.notNull(searchConditionsModel, "searchConditionsModel");
        AssertAs.INSTANCE.notNull(searchConditionsModel.getObject(), "searchConditions");
        this.searchConditionsModel = searchConditionsModel;
    }

    /**
     * Returns paged {@link SearchCondition} iterator
     *
     * @param offset
     *     skipping records
     * @param size
     *     page size
     * @return iterator
     */
    @Override
    public Iterator<SearchCondition> iterator(final long offset, final long size) {
        return searchConditionsModel
            .getObject()
            .stream()
            .skip(offset)
            .limit(size)
            .iterator();
    }

    @Override
    public long size() {
        return searchConditionsModel
            .getObject()
            .size();
    }

    @Override
    public IModel<SearchCondition> model(final SearchCondition searchCondition) {
        return new Model<>(searchCondition);
    }

}
