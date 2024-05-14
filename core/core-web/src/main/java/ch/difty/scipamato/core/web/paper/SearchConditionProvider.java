package ch.difty.scipamato.core.web.paper;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.search.SearchCondition;

/**
 * Builds up the {@link SearchCondition} provider based on the model passed into
 * the constructor.
 *
 * @author u.joss
 */
public class SearchConditionProvider extends SortableDataProvider<SearchCondition, String> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private final IModel<List<SearchCondition>> searchConditionsModel;

    public SearchConditionProvider(@NotNull final IModel<List<SearchCondition>> searchConditionsModel) {
        Objects.requireNonNull(searchConditionsModel.getObject());
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
    @NotNull
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

    @NotNull
    @Override
    public IModel<SearchCondition> model(final SearchCondition searchCondition) {
        return new Model<>(searchCondition);
    }
}
