package ch.difty.scipamato.core.web.newsletter.topic;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.DefinitionListResultPanel;
import ch.difty.scipamato.core.web.newsletter.NewsletterTopicDefinitionProvider;

@SuppressWarnings("SameParameterValue")
class NewsletterTopicListResultPanel extends
    DefinitionListResultPanel<NewsletterTopicDefinition, NewsletterTopicFilter, NewsletterTopicService, NewsletterTopicDefinitionProvider> {

    private static final long serialVersionUID = 1L;

    NewsletterTopicListResultPanel(@NotNull final String id,
        @NotNull final NewsletterTopicDefinitionProvider provider) {
        super(id, provider);
    }

    @NotNull
    @Override
    protected List<IColumn<NewsletterTopicDefinition, String>> makeTableColumns() {
        final List<IColumn<NewsletterTopicDefinition, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        return columns;
    }

    private void onTitleClick(final IModel<NewsletterTopicDefinition> model) {
        setResponsePage(new NewsletterTopicEditPage(model, getPage().getPageReference()));
    }
}
