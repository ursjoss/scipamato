package ch.difty.scipamato.core.web.code;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DuplicateKeyException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.DefinitionEditPage;

@MountPath("codes/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "SpellCheckingInspection" })
public class CodeEditPage extends DefinitionEditPage<CodeDefinition, CodeTranslation, String> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern DUPLICATE_KEY_PATTERN = Pattern.compile(
        "Key \\(code_class_id, sort\\)=\\((\\d+), (\\d+)\\) already exists.*PSQLException: "
        + "ERROR: duplicate key value violates unique constraint");

    @SuppressWarnings("unused")
    @SpringBean
    private CodeService service;

    public CodeEditPage(@Nullable final IModel<CodeDefinition> model, @Nullable final PageReference callingPageRef) {
        super(model, callingPageRef);
    }

    @Nullable
    @Override
    protected CodeDefinition persistModel() {
        return service.saveOrUpdate(getModelObject());
    }

    @NotNull
    @Override
    protected CodeEditHeaderPanel newDefinitionHeaderPanel(@NotNull final String id) {
        return new CodeEditHeaderPanel(id, getModel()) {

            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @NotNull
            @Override
            protected Form<CodeDefinition> getForm() {
                return CodeEditPage.this.getForm();
            }

            @Nullable
            @Override
            protected CodeDefinition doDelete(@NotNull final CodeDefinition ntd, @NotNull final String recordId) {
                return service.delete(recordId, ntd.getVersion());
            }

            @Nullable
            @Override
            protected PageReference getCallingPageRef() {
                return CodeEditPage.this.getCallingPageRef();
            }

            @NotNull
            @Override
            protected Class<? extends Page> staticResponsePage() {
                return CodeListPage.class;
            }
        };
    }

    @NotNull
    @Override
    protected CodeEditTranslationPanel newDefinitionTranslationPanel(@NotNull final String id) {
        return new CodeEditTranslationPanel(id, getModel());
    }

    @Override
    protected void handleDuplicateKeyException(@NotNull final DuplicateKeyException dke) {
        final String errorMsg = dke.getMessage();
        if (errorMsg != null) {
            final Matcher matcher = DUPLICATE_KEY_PATTERN.matcher(errorMsg);
            if (matcher.find()) {
                error(new StringResourceModel("save.duplicatekey.hint", this, null)
                    .setParameters(matcher.group(2), matcher.group(1))
                    .getString());
            } else {
                error(errorMsg);
            }
        } else {
            error("Unexpected DuplicateKeyConstraintViolation");
        }
    }
}
