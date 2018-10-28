package ch.difty.scipamato.core.web.keyword;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;

public class KeywordDefinitionProvider extends SortableDataProvider<KeywordDefinition, String>
    implements IFilterStateLocator<KeywordFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private KeywordService service;

    private KeywordFilter filter;

    KeywordDefinitionProvider() {
        this(null);
    }

    public KeywordDefinitionProvider(KeywordFilter filter) {
        Injector
            .get()
            .inject(this);
        this.filter = filter != null ? filter : new KeywordFilter();
        setSort(Keyword.KeywordFields.NAME.getName(), SortOrder.ASCENDING);
    }

    /** package-private for test purposes */
    void setService(KeywordService service) {
        this.service = service;
    }

    @Override
    public Iterator<KeywordDefinition> iterator(long offset, long size) {
        Sort.Direction dir = getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProp = getSort().getProperty();
        PaginationContext pc = new PaginationRequest((int) offset, (int) size, dir, sortProp);
        return service
            .findPageOfKeywordDefinitions(filter, pc)
            .iterator();
    }

    @Override
    public long size() {
        return service.countByFilter(filter);
    }

    @Override
    public IModel<KeywordDefinition> model(KeywordDefinition entity) {
        return new Model<>(entity);
    }

    @Override
    public KeywordFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(KeywordFilter state) {
        this.filter = state;
    }
}
