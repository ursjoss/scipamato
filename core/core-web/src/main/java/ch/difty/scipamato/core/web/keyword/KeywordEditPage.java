package ch.difty.scipamato.core.web.keyword;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DuplicateKeyException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionEditPage;

@MountPath("keyword/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings("SameParameterValue")
public class KeywordEditPage extends DefinitionEditPage<KeywordDefinition, KeywordTranslation, Integer> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    @SpringBean
    private KeywordService service;

    public KeywordEditPage(@Nullable final IModel<KeywordDefinition> model, @Nullable final PageReference callingPageRef) {
        super(model, callingPageRef);
    }

    @Nullable
    @Override
    protected KeywordDefinition persistModel() {
        return service.saveOrUpdate(getModelObject());
    }

    @NotNull
    @Override
    protected KeywordEditHeaderPanel newDefinitionHeaderPanel(@NotNull final String id) {
        return new KeywordEditHeaderPanel(id, getModel()) {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Nullable
            @Override
            protected KeywordDefinition doDelete(@NotNull final KeywordDefinition kd, @NotNull final Integer recordId) {
                return service.delete(recordId, kd.getVersion());
            }

            @Nullable
            @Override
            protected PageReference getCallingPageRef() {
                return KeywordEditPage.this.getCallingPageRef();
            }

            @NotNull
            @Override
            protected Class<? extends Page> staticResponsePage() {
                return KeywordListPage.class;
            }
        };
    }

    @NotNull
    @Override
    protected KeywordEditTranslationPanel newDefinitionTranslationPanel(@NotNull final String id) {
        return new KeywordEditTranslationPanel(id, getModel()) {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @NotNull
            @Override
            protected Form<KeywordDefinition> getForm() {
                return KeywordEditPage.this.getForm();
            }
        };
    }

    @Override
    protected void handleDuplicateKeyException(@NotNull final DuplicateKeyException dke) {
        if (dke.getMessage() != null)
            error(dke.getMessage());
        else
            error("Unexpected DuplicateKeyConstraintViolation");
    }
}
