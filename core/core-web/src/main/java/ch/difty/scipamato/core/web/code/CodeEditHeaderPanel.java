package ch.difty.scipamato.core.web.code;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.web.common.DeletableDefinitionEditHeaderPanel;
import ch.difty.scipamato.core.web.model.CodeClassModel;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
abstract class CodeEditHeaderPanel extends DeletableDefinitionEditHeaderPanel<CodeDefinition, CodeTranslation, String> {

    private BootstrapSelect<CodeClass> codeClasses;

    CodeEditHeaderPanel(@NotNull final String id, @NotNull final IModel<CodeDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
        final TextField<String> code = new TextField<>(CodeDefinition.CodeDefinitionFields.CODE.getFieldName());
        queueFieldAndLabel(code);
        queueBootstrapSelectAndLabel("codeClass");
        queueFieldAndLabel(new TextField<>(CodeDefinition.CodeDefinitionFields.SORT.getFieldName()));
        queue(new Label("internalLabel",
            new StringResourceModel(CodeDefinition.CodeDefinitionFields.INTERNAL.getFieldName() + LABEL_RESOURCE_TAG,
                this, null)));
        CheckBoxX internal = new CheckBoxX(CodeDefinition.CodeDefinitionFields.INTERNAL.getFieldName());
        internal
            .getConfig()
            .withThreeState(false)
            .withUseNative(true);
        queue(internal);

        getForm().add(new CodeMustMatchCodeClassValidator(code, codeClasses));
    }

    protected abstract Form getForm();

    private void queueBootstrapSelectAndLabel(final String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)));
        final PropertyModel<CodeClass> model = PropertyModel.of(getModel(),
            CodeFilter.CodeFilterFields.CODE_CLASS.getFieldName());
        final CodeClassModel choices = new CodeClassModel(getLocale().getLanguage());
        final IChoiceRenderer<CodeClass> choiceRenderer = new ChoiceRenderer<>(
            CoreEntity.CoreEntityFields.DISPLAY_VALUE.getFieldName(),
            IdScipamatoEntity.IdScipamatoEntityFields.ID.getFieldName());
        codeClasses = new BootstrapSelect<>(id, model, choices, choiceRenderer);
        queue(codeClasses);
    }

    /**
     * Validator to guarantee that the selected Code (e.g. "2A") matches the code class
     * that corresponds with the first number in the code (i.e. Code Class 2).
     */
    static class CodeMustMatchCodeClassValidator extends AbstractFormValidator {
        private static final long serialVersionUID = 1L;

        private final FormComponent<?>[] components;

        CodeMustMatchCodeClassValidator(@NotNull final TextField<String> codeField,
            @NotNull final BootstrapSelect<CodeClass> codeClasses) {
            this.components = new FormComponent<?>[] { codeField, codeClasses };
        }

        @NotNull
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void validate(@NotNull final Form<?> form) {
            final TextField<String> codeField = (TextField<String>) components[0];
            final BootstrapSelect<CodeClass> codeClassField = (BootstrapSelect<CodeClass>) components[1];
            final String codeValue = codeField.getConvertedInput();
            final CodeClass codeClass = codeClassField.getConvertedInput();
            if (codeClass == null || codeValue == null || codeValue.isEmpty() || !codeValue
                .substring(0, 1)
                .equalsIgnoreCase(codeClass
                    .getId()
                    .toString()))
                error(codeField, resourceKey() + ".error");
        }
    }
}
