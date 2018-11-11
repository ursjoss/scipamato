package ch.difty.scipamato.core.web.keyword;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DuplicateKeyException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionEditHeaderPanel;
import ch.difty.scipamato.core.web.common.DefinitionEditPage;
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel;

@MountPath("keyword/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
public class KeywordEditPage extends DefinitionEditPage<KeywordDefinition> {

    @SpringBean
    private KeywordService service;

    KeywordEditPage(final IModel<KeywordDefinition> model, final PageReference callingPageRef) {
        super(model, callingPageRef);
    }

    @Override
    protected KeywordDefinition persistModel() {
        return service.saveOrUpdate(getModelObject());
    }

    @Override
    protected DefinitionEditHeaderPanel newDefinitionHeaderPanel(final String id) {
        return new KeywordEditHeaderPanel(id, getModel()) {
            @Override
            protected KeywordDefinition doDelete(final KeywordDefinition kd, final Integer recordId) {
                return service.delete(recordId, kd.getVersion());
            }

            @Override
            protected PageReference getCallingPageRef() {
                return KeywordEditPage.this.getCallingPageRef();
            }

            @Override
            protected Class<? extends Page> staticResponsePage() {
                return KeywordListPage.class;
            }
        };
    }

    @Override
    protected DefinitionEditTranslationPanel newDefinitionTranslationPanel(final String id) {
        return new KeywordEditTranslationPanel(id, getModel()) {
            @Override
            protected Form getForm() {
                return KeywordEditPage.this.getForm();
            }
        };
    }

    @Override
    protected void handleDuplicateKeyException(final DuplicateKeyException dke) {
        if (dke != null && dke.getMessage() != null)
            error(dke.getMessage());
    }
}
