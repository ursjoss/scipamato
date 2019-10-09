package ch.difty.scipamato.core.web;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;

public abstract class DefinitionProvider<T extends DefinitionEntity, F extends ScipamatoFilter, S extends DefinitionProviderService<T, F>>
    extends SortableDataProvider<T, String> implements IFilterStateLocator<F> {

    private final S service;
    
    private F filter;

    protected DefinitionProvider(final F filter) {
        Injector
            .get()
            .inject(this);
        this.service = getService();
        this.filter = filter != null ? filter : newFilter();
    }

    protected abstract S getService();

    protected abstract F newFilter();

    @Override
    public Iterator<T> iterator(long offset, long size) {
        final Sort.Direction dir = getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        final String sortProp = getSort().getProperty();
        final PaginationContext pc = new PaginationRequest((int) offset, (int) size, dir, sortProp);
        return service.findPageOfEntityDefinitions(filter, pc);
    }

    @Override
    public long size() {
        return service.countByFilter(filter);
    }

    @Override
    public IModel<T> model(T entity) {
        return new Model<>(entity);
    }

    @Override
    public F getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(F state) {
        this.filter = state;
    }
}
