package ch.difty.scipamato.core.web.code;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditHeaderPanel;
import ch.difty.scipamato.core.web.model.CodeClassModel;

@SuppressWarnings("SameParameterValue")
abstract class CodeEditHeaderPanel extends DefinitionEditHeaderPanel<CodeDefinition, CodeTranslation, String> {

    private BootstrapSelect<CodeClass> codeClasses;

    CodeEditHeaderPanel(final String id, final IModel<CodeDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
        final TextField<String> code = new TextField<>(CodeDefinition.CodeDefinitionFields.CODE.getName());
        queueFieldAndLabel(code);
        queueBootstrapSelectAndLabel("codeClass");
        queueFieldAndLabel(new TextField<Integer>(CodeDefinition.CodeDefinitionFields.SORT.getName()));
        queue(new Label("internalLabel",
            new StringResourceModel(CodeDefinition.CodeDefinitionFields.INTERNAL.getName() + LABEL_RESOURCE_TAG, this,
                null)));
        CheckBoxX internal = new CheckBoxX(CodeDefinition.CodeDefinitionFields.INTERNAL.getName());
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
            CodeFilter.CodeFilterFields.CODE_CLASS.getName());
        final CodeClassModel choices = new CodeClassModel(getLocale().getLanguage());
        final IChoiceRenderer<CodeClass> choiceRenderer = new ChoiceRenderer<>(
            CodeClass.CoreEntityFields.DISPLAY_VALUE.getName(), CodeClass.IdScipamatoEntityFields.ID.getName());
        codeClasses = new BootstrapSelect<>(id, model, choices, choiceRenderer);
        queue(codeClasses);
    }

    /**
     * Validator to guarantee that the selected Code (e.g. "2A") matches the code class
     * that corresponds with the first number in the code (i.e. Code Class 2).
     */
    class CodeMustMatchCodeClassValidator extends AbstractFormValidator {
        private static final long serialVersionUID = 1L;

        private final FormComponent<?>[] components;

        CodeMustMatchCodeClassValidator(final TextField<String> codeField,
            final BootstrapSelect<CodeClass> codeClasses) {
            this.components = new FormComponent<?>[] { AssertAs.notNull(codeField, "field"),
                AssertAs.notNull(codeClasses, "codeClasses") };
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void validate(final Form<?> form) {
            final TextField<String> codeField = (TextField<String>) components[0];
            final BootstrapSelect<CodeClass> codeClassField = (BootstrapSelect<CodeClass>) components[1];
            final String codeValue = codeField.getConvertedInput();
            final CodeClass codeClass = codeClassField.getConvertedInput();
            if (codeValue == null || codeValue.isEmpty() || !codeValue
                .substring(0, 1)
                .equalsIgnoreCase(codeClass
                    .getId()
                    .toString()))
                error(codeField, resourceKey() + ".error");
        }
    }

}
