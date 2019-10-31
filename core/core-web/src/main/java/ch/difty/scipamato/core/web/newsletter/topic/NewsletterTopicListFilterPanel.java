package ch.difty.scipamato.core.web.newsletter.topic;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;
import ch.difty.scipamato.core.web.newsletter.NewsletterTopicDefinitionProvider;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
abstract class NewsletterTopicListFilterPanel extends
    DefinitionListFilterPanel<NewsletterTopicDefinition, NewsletterTopicFilter, NewsletterTopicService, NewsletterTopicDefinitionProvider> {

    NewsletterTopicListFilterPanel(@NotNull final String id,
        @NotNull final NewsletterTopicDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueFieldAndLabel(new TextField<String>("title", PropertyModel.of(getFilter(),
            NewsletterTopicFilter.NewsletterTopicFilterFields.TITLE_MASK.getFieldName())));
        queueNewNewsletterTopicButton("newNewsletterTopic");
    }

    private void queueNewNewsletterTopicButton(final String id) {
        queue(doQueueNewNewsletterTopicButton(id));
    }

    @NotNull
    protected abstract BootstrapAjaxButton doQueueNewNewsletterTopicButton(@NotNull final String id);
}