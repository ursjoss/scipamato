package ch.difty.scipamato.core.web.newsletter.topic;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DuplicateKeyException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.DefinitionEditPage;
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel;

@MountPath("newsletter-topic/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
public class NewsletterTopicEditPage extends DefinitionEditPage<NewsletterTopicDefinition> {

    @SpringBean
    private NewsletterTopicService service;

    NewsletterTopicEditPage(final IModel<NewsletterTopicDefinition> model, final PageReference callingPageRef) {
        super(model, callingPageRef);
    }

    @Override
    protected NewsletterTopicDefinition persistModel() {
        return service.saveOrUpdate(getModelObject());
    }

    @Override
    protected NewsletterTopicEditHeaderPanel newDefinitionHeaderPanel(final String id) {
        return new NewsletterTopicEditHeaderPanel(id, getModel()) {

            @Override
            protected NewsletterTopicDefinition doDelete(final NewsletterTopicDefinition ntd, final Integer recordId) {
                return service.delete(recordId, ntd.getVersion());
            }

            @Override
            protected PageReference getCallingPageRef() {
                return NewsletterTopicEditPage.this.getCallingPageRef();
            }

            @Override
            protected Class<? extends Page> staticResponsePage() {
                return NewsletterTopicListPage.class;
            }
        };
    }

    @Override
    protected DefinitionEditTranslationPanel newDefinitionTranslationPanel(final String id) {
        return new NewsletterTopicEditTranslationPanel(id, getModel());
    }

    @Override
    protected void handleDuplicateKeyException(final DuplicateKeyException dke) {
        if (dke != null && dke.getMessage() != null)
            info(dke.getMessage());
    }
}
