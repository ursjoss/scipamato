package ch.difty.scipamato.core.web.codeclass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DuplicateKeyException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.code.CodeListPage;
import ch.difty.scipamato.core.web.common.DefinitionEditPage;

@MountPath("codeclasses/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "SpellCheckingInspection" })
public class CodeClassEditPage extends DefinitionEditPage<CodeClassDefinition, CodeClassTranslation, Integer> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern DUPLICATE_KEY_PATTERN = Pattern.compile(
        "Key \\(code_class_id, lang_code\\)=\\((\\d+), \\w+\\) already exists.*PSQLException: "
        + "ERROR: duplicate key value violates unique constraint");

    @SuppressWarnings("unused")
    @SpringBean
    private CodeClassService service;

    CodeClassEditPage(@Nullable final IModel<CodeClassDefinition> model, @Nullable final PageReference callingPageRef) {
        super(model, callingPageRef);
    }

    @NotNull
    @Override
    protected CodeClassDefinition persistModel() {
        return service.saveOrUpdate(getModelObject());
    }

    @NotNull
    @Override
    protected CodeClassEditHeaderPanel newDefinitionHeaderPanel(@NotNull final String id) {
        return new CodeClassEditHeaderPanel(id, getModel()) {

            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Nullable
            @Override
            protected PageReference getCallingPageRef() {
                return CodeClassEditPage.this.getCallingPageRef();
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
    protected CodeClassEditTranslationPanel newDefinitionTranslationPanel(@NotNull final String id) {
        return new CodeClassEditTranslationPanel(id, getModel());
    }

    @Override
    protected void handleDuplicateKeyException(@NotNull final DuplicateKeyException dke) {
        final String errorMsg = dke.getMessage();
        if (errorMsg != null) {
            final Matcher matcher = DUPLICATE_KEY_PATTERN.matcher(errorMsg);
            if (matcher.find()) {
                error(new StringResourceModel("save.duplicatekey.hint", this, null)
                    .setParameters(matcher.group(1))
                    .getString());
            } else {
                error(errorMsg);
            }
        } else {
            error("Unexpected DuplicateKeyConstraintViolation");
        }
    }
}
