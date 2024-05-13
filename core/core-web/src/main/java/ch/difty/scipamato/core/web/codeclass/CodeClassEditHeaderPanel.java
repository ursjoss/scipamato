package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditHeaderPanel;

@SuppressWarnings("SameParameterValue")
public abstract class CodeClassEditHeaderPanel extends DefinitionEditHeaderPanel<CodeClassDefinition, CodeClassTranslation, Integer> {

    @java.io.Serial
    private static final long serialVersionUID = 1011264548992464361L;

    CodeClassEditHeaderPanel(@NotNull final String id, @Nullable final IModel<CodeClassDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
        final TextField<Integer> idField = new TextField<>(
            CodeClassDefinition.CodeClassDefinitionFields.ID.getFieldName());
        idField.setEnabled(false);
        queueFieldAndLabel(idField);
    }
}
