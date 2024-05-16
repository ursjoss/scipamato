package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;

@SuppressWarnings("SameParameterValue")
class CodeClassListFilterPanel
    extends DefinitionListFilterPanel<CodeClassDefinition, CodeClassFilter, CodeClassService, CodeClassDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    CodeClassListFilterPanel(@NotNull final String id, @NotNull final CodeClassDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueFieldAndLabel(new TextField<>(CodeClassDefinition.CodeClassDefinitionFields.NAME.getFieldName(),
            PropertyModel.of(getFilter(), CodeClassFilter.CodeClassFilterFields.NAME_MASK.getFieldName())));

        queueFieldAndLabel(new TextField<>(CodeClassTranslation.CodeClassTranslationFields.DESCRIPTION.getFieldName(),
            PropertyModel.of(getFilter(), CodeClassFilter.CodeClassFilterFields.DESCRIPTION_MASK.getFieldName())));
    }
}
