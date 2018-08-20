package ch.difty.scipamato.core.web.newsletter;

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
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;

public class NewsletterTopicDefinitionProvider extends SortableDataProvider<NewsletterTopicDefinition, String>
    implements IFilterStateLocator<NewsletterTopicFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewsletterTopicService service;

    private NewsletterTopicFilter filter;

    public NewsletterTopicDefinitionProvider() {
        this(null);
    }

    public NewsletterTopicDefinitionProvider(NewsletterTopicFilter filter) {
        Injector
            .get()
            .inject(this);
        this.filter = filter != null ? filter : new NewsletterTopicFilter();
        setSort(NewsletterTopic.NewsletterTopicFields.TITLE.getName(), SortOrder.ASCENDING);
    }

    /** package-private for test purposes */
    void setService(NewsletterTopicService service) {
        this.service = service;
    }

    @Override
    public Iterator<NewsletterTopicDefinition> iterator(long offset, long size) {
        Sort.Direction dir = getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProp = getSort().getProperty();
        PaginationContext pc = new PaginationRequest((int) offset, (int) size, dir, sortProp);
        return service
            .findPageOfNewsletterTopicDefinitions(filter, pc)
            .iterator();
    }

    @Override
    public long size() {
        return service.countByFilter(filter);
    }

    @Override
    public IModel<NewsletterTopicDefinition> model(NewsletterTopicDefinition entity) {
        return new Model<>(entity);
    }

    @Override
    public NewsletterTopicFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(NewsletterTopicFilter state) {
        this.filter = state;
    }
}
