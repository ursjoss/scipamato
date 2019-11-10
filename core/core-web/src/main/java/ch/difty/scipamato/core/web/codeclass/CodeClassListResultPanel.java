package ch.difty.scipamato.core.web.codeclass;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.DefinitionListResultPanel;

@SuppressWarnings("SameParameterValue")
class CodeClassListResultPanel extends
    DefinitionListResultPanel<CodeClassDefinition, CodeClassFilter, CodeClassService, CodeClassDefinitionProvider> {

    private static final long serialVersionUID = 1L;

    CodeClassListResultPanel(@NotNull final String id, @NotNull final CodeClassDefinitionProvider provider) {
        super(id, provider);
    }

    @NotNull
    @Override
    protected List<IColumn<CodeClassDefinition, String>> makeTableColumns() {
        final List<IColumn<CodeClassDefinition, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(CodeClassDefinition.CodeClassDefinitionFields.ID.getFieldName()));
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        return columns;
    }

    private void onTitleClick(@NotNull final IModel<CodeClassDefinition> model) {
        setResponsePage(new CodeClassEditPage(model, getPage().getPageReference()));
    }
}
