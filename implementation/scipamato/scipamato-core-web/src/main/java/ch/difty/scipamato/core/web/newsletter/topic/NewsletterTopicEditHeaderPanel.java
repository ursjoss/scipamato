package ch.difty.scipamato.core.web.newsletter.topic;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditHeaderPanel;

@SuppressWarnings({ "WicketForgeJavaIdInspection", "SameParameterValue" })
abstract class NewsletterTopicEditHeaderPanel
    extends DefinitionEditHeaderPanel<NewsletterTopicDefinition, NewsletterTopicTranslation, Integer> {

    private BootstrapSelect<CodeClass> codeClasses;

    NewsletterTopicEditHeaderPanel(final String id, final IModel<NewsletterTopicDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
    }

}
