package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditHeaderPanel;

@SuppressWarnings("SameParameterValue")
abstract class CodeClassEditHeaderPanel
    extends DefinitionEditHeaderPanel<CodeClassDefinition, CodeClassTranslation, Integer> {

    CodeClassEditHeaderPanel(final String id, final IModel<CodeClassDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
        final TextField<Integer> idField = new TextField<>(CodeClassDefinition.CodeClassDefinitionFields.ID.getName());
        idField.setEnabled(false);
        queueFieldAndLabel(idField);
    }

}
