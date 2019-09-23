package ch.difty.scipamato.core.web.newsletter.topic;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;
import ch.difty.scipamato.core.web.newsletter.NewsletterTopicDefinitionProvider;

@MountPath("newsletter/topics")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue" })
public class NewsletterTopicListPage extends
    DefinitionListPage<NewsletterTopicDefinition, NewsletterTopicFilter, NewsletterTopicService, NewsletterTopicDefinitionProvider> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    @SpringBean
    private NewsletterTopicService service;

    public NewsletterTopicListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected NewsletterTopicFilter newFilter() {
        return new NewsletterTopicFilter();
    }

    @Override
    protected NewsletterTopicDefinitionProvider newProvider(final NewsletterTopicFilter filter) {
        return new NewsletterTopicDefinitionProvider(filter);
    }

    @Override
    protected Panel newFilterPanel(final String id) {
        return new NewsletterTopicListFilterPanel(id, getProvider()) {

            @Override
            protected BootstrapAjaxButton doQueueNewNewsletterTopicButton(final String id) {
                return newResponsePageButton(id,
                    () -> new NewsletterTopicEditPage(Model.of(service.newUnpersistedNewsletterTopicDefinition()),
                        getPage().getPageReference()));
            }
        };
    }

    @Override
    protected Panel newResultPanel(final String id) {
        return new NewsletterTopicListResultPanel(id, getProvider());
    }

}