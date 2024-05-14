package ch.difty.scipamato.core.web.newsletter;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.DefinitionProvider;

public class NewsletterTopicDefinitionProvider extends DefinitionProvider<NewsletterTopicDefinition, NewsletterTopicFilter, NewsletterTopicService> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewsletterTopicService service;

    NewsletterTopicDefinitionProvider() {
        this(null);
    }

    public NewsletterTopicDefinitionProvider(@Nullable final NewsletterTopicFilter filter) {
        super(filter);
        setSort(NewsletterTopic.NewsletterTopicFields.TITLE.getFieldName(), SortOrder.ASCENDING);
    }

    @NotNull
    @Override
    protected NewsletterTopicService getService() {
        return service;
    }

    @NotNull
    @Override
    protected NewsletterTopicFilter newFilter() {
        return new NewsletterTopicFilter();
    }
}
