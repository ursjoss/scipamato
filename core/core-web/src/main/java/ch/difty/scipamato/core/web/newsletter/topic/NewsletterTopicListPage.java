package ch.difty.scipamato.core.web.newsletter.topic;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
public class NewsletterTopicListPage
    extends DefinitionListPage<NewsletterTopicDefinition, NewsletterTopicFilter, NewsletterTopicService, NewsletterTopicDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    @SpringBean
    private NewsletterTopicService service;

    public NewsletterTopicListPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected NewsletterTopicFilter newFilter() {
        return new NewsletterTopicFilter();
    }

    @NotNull
    @Override
    protected NewsletterTopicDefinitionProvider newProvider(@Nullable final NewsletterTopicFilter filter) {
        return new NewsletterTopicDefinitionProvider(filter);
    }

    @NotNull
    @Override
    protected Panel newFilterPanel(@NotNull final String id) {
        return new NewsletterTopicListFilterPanel(id, getProvider()) {

            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @NotNull
            @Override
            protected BootstrapAjaxButton doQueueNewNewsletterTopicButton(@NotNull final String id) {
                return newResponsePageButton(id,
                    () -> new NewsletterTopicEditPage(Model.of(service.newUnpersistedNewsletterTopicDefinition()), getPage().getPageReference()));
            }
        };
    }

    @NotNull
    @Override
    protected Panel newResultPanel(@NotNull final String id) {
        return new NewsletterTopicListResultPanel(id, getProvider());
    }
}
