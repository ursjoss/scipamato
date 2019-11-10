package ch.difty.scipamato.core.web.newsletter;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.persistence.NewsletterService;

public class NewsletterProvider extends SortableDataProvider<Newsletter, String>
    implements IFilterStateLocator<NewsletterFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewsletterService service;

    private NewsletterFilter filter;

    NewsletterProvider() {
        this(null);
    }

    public NewsletterProvider(@Nullable NewsletterFilter filter) {
        Injector
            .get()
            .inject(this);
        this.filter = filter != null ? filter : new NewsletterFilter();
        setSort(Newsletter.NewsletterFields.ISSUE.getFieldName(), SortOrder.DESCENDING);
    }

    /** package-private for test purposes */
    void setService(@NotNull NewsletterService service) {
        this.service = service;
    }

    @NotNull
    @Override
    public Iterator<Newsletter> iterator(long offset, long size) {
        Sort.Direction dir = getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProp = getSort().getProperty();
        PaginationContext pc = new PaginationRequest((int) offset, (int) size, dir, sortProp);
        return service
            .findPageByFilter(filter, pc)
            .iterator();
    }

    @Override
    public long size() {
        return service.countByFilter(filter);
    }

    @NotNull
    @Override
    public IModel<Newsletter> model(Newsletter entity) {
        return new Model<>(entity);
    }

    @NotNull
    @Override
    public NewsletterFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(@NotNull NewsletterFilter state) {
        this.filter = state;
    }
}
