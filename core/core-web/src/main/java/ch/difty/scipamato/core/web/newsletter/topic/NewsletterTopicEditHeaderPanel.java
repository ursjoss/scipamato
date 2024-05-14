package ch.difty.scipamato.core.web.newsletter.topic;

import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.web.common.DeletableDefinitionEditHeaderPanel;

import java.io.Serial;

@SuppressWarnings("SameParameterValue")
public abstract class NewsletterTopicEditHeaderPanel
    extends DeletableDefinitionEditHeaderPanel<NewsletterTopicDefinition, NewsletterTopicTranslation, Integer> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    NewsletterTopicEditHeaderPanel(@NotNull final String id, @Nullable final IModel<NewsletterTopicDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
    }
}
