package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;

@SuppressWarnings("SameParameterValue")
class CodeClassListFilterPanel extends
    DefinitionListFilterPanel<CodeClassDefinition, CodeClassFilter, CodeClassService, CodeClassDefinitionProvider> {

    private static final long serialVersionUID = 1L;

    CodeClassListFilterPanel(final String id, final CodeClassDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueFieldAndLabel(new TextField<String>(CodeClassDefinition.CodeClassDefinitionFields.NAME.getFieldName(),
            PropertyModel.of(getFilter(), CodeClassFilter.CodeClassFilterFields.NAME_MASK.getFieldName())));

        queueFieldAndLabel(
            new TextField<String>(CodeClassTranslation.CodeClassTranslationFields.DESCRIPTION.getFieldName(),
                PropertyModel.of(getFilter(), CodeClassFilter.CodeClassFilterFields.DESCRIPTION_MASK.getFieldName())));
    }

}