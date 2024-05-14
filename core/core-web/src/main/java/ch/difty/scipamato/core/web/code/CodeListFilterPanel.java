package ch.difty.scipamato.core.web.code;

import static ch.difty.scipamato.common.web.WicketUtilsKt.LABEL_RESOURCE_TAG;
import static ch.difty.scipamato.common.web.WicketUtilsKt.LABEL_TAG;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;
import ch.difty.scipamato.core.web.model.CodeClassModel;

@SuppressWarnings("SameParameterValue")
abstract class CodeListFilterPanel extends DefinitionListFilterPanel<CodeDefinition, CodeFilter, CodeService, CodeDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    CodeListFilterPanel(@NotNull final String id, @NotNull final CodeDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueBootstrapSelectAndLabel("codeClass");

        queueFieldAndLabel(new TextField<>(CodeDefinition.CodeDefinitionFields.NAME.getFieldName(),
            PropertyModel.of(getFilter(), CodeFilter.CodeFilterFields.NAME_MASK.getFieldName())));

        queueFieldAndLabel(new TextField<>(CodeTranslation.CodeTranslationFields.COMMENT.getFieldName(),
            PropertyModel.of(getFilter(), CodeFilter.CodeFilterFields.COMMENT_MASK.getFieldName())));

        queueNewCodeButton("newCode");
    }

    private void queueBootstrapSelectAndLabel(final String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)));

        final PropertyModel<CodeClass> model = PropertyModel.of(getFilter(), CodeFilter.CodeFilterFields.CODE_CLASS.getFieldName());
        final CodeClassModel choices = new CodeClassModel(getLocale().getLanguage());
        final IChoiceRenderer<CodeClass> choiceRenderer = new ChoiceRenderer<>(CoreEntity.CoreEntityFields.DISPLAY_VALUE.getFieldName(),
            IdScipamatoEntity.IdScipamatoEntityFields.ID.getFieldName());

        final BootstrapSelect<CodeClass> codeClasses = new BootstrapSelect<>(id, model, choices, choiceRenderer);
        codeClasses.add(new AjaxFormComponentUpdatingBehavior("change") {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(@NotNull final AjaxRequestTarget target) {
                doOnUpdate(target);
            }
        });
        queue(codeClasses);
    }

    protected abstract void doOnUpdate(@NotNull final AjaxRequestTarget target);

    private void queueNewCodeButton(@NotNull final String id) {
        queue(doQueueNewCodeButton(id));
    }

    protected abstract BootstrapAjaxButton doQueueNewCodeButton(@NotNull final String id);
}
