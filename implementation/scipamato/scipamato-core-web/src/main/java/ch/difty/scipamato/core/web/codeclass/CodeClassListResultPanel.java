package ch.difty.scipamato.core.web.codeclass;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.DefinitionListResultPanel;

@SuppressWarnings("SameParameterValue")
class CodeClassListResultPanel extends
    DefinitionListResultPanel<CodeClassDefinition, CodeClassFilter, CodeClassService, CodeClassDefinitionProvider> {

    CodeClassListResultPanel(final String id, final CodeClassDefinitionProvider provider) {
        super(id, provider);
    }

    @Override
    protected List<IColumn<CodeClassDefinition, String>> makeTableColumns() {
        final List<IColumn<CodeClassDefinition, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(CodeClassDefinition.CodeClassDefinitionFields.ID.getName()));
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        return columns;
    }

    private void onTitleClick(final IModel<CodeClassDefinition> model) {
        setResponsePage(new CodeClassEditPage(model, getPage().getPageReference()));
    }

}
