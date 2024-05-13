package ch.difty.scipamato.core.web.newsletter.topic;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DuplicateKeyException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.DefinitionEditPage;

@MountPath("newsletter-topic/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings("SameParameterValue")
public class NewsletterTopicEditPage
    extends DefinitionEditPage<NewsletterTopicDefinition, NewsletterTopicTranslation, Integer> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    @SpringBean
    private NewsletterTopicService service;

    NewsletterTopicEditPage(@Nullable final IModel<NewsletterTopicDefinition> model,
        @Nullable final PageReference callingPageRef) {
        super(model, callingPageRef);
    }

    @Nullable
    @Override
    protected NewsletterTopicDefinition persistModel() {
        return service.saveOrUpdate(getModelObject());
    }

    @NotNull
    @Override
    protected NewsletterTopicEditHeaderPanel newDefinitionHeaderPanel(@NotNull final String id) {
        return new NewsletterTopicEditHeaderPanel(id, getModel()) {

            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Nullable
            @Override
            protected NewsletterTopicDefinition doDelete(@NotNull final NewsletterTopicDefinition ntd,
                @NotNull final Integer recordId) {
                return service.delete(recordId, ntd.getVersion());
            }

            @Nullable
            @Override
            protected PageReference getCallingPageRef() {
                return NewsletterTopicEditPage.this.getCallingPageRef();
            }

            @NotNull
            @Override
            protected Class<? extends Page> staticResponsePage() {
                return NewsletterTopicListPage.class;
            }
        };
    }

    @NotNull
    @Override
    protected NewsletterTopicEditTranslationPanel newDefinitionTranslationPanel(@NotNull final String id) {
        return new NewsletterTopicEditTranslationPanel(id, getModel());
    }

    @Override
    protected void handleDuplicateKeyException(@NotNull final DuplicateKeyException dke) {
        if (dke.getMessage() != null)
            error(dke.getMessage());
        else
            error("Unexpected DuplicateKeyConstraintViolation");
    }
}
