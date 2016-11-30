package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.entity.filter.ComplexPaperFilter;

public class ComplexPaperFilterProvider extends SortableDataProvider<ComplexPaperFilter, String> {

    private static final long serialVersionUID = 1L;

    private IModel<CompositeComplexPaperFilter> compositeModel;

    public ComplexPaperFilterProvider(IModel<CompositeComplexPaperFilter> compositeModel) {
        this.compositeModel = compositeModel;
    }

    @Override
    public Iterator<ComplexPaperFilter> iterator(long offset, long size) {
        return compositeModel.getObject().getFilters().iterator();
    }

    @Override
    public long size() {
        return compositeModel.getObject().getFilters().size();
    }

    @Override
    public IModel<ComplexPaperFilter> model(ComplexPaperFilter entity) {
        return new Model<ComplexPaperFilter>(entity);
    }

}
