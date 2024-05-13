package ch.difty.scipamato.core.web.code;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.DefinitionListResultPanel;

@SuppressWarnings("SameParameterValue")
class CodeListResultPanel
    extends DefinitionListResultPanel<CodeDefinition, CodeFilter, CodeService, CodeDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    CodeListResultPanel(@NotNull final String id, @NotNull final CodeDefinitionProvider provider) {
        super(id, provider);
    }

    @NotNull
    @Override
    protected List<IColumn<CodeDefinition, String>> makeTableColumns() {
        final List<IColumn<CodeDefinition, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(CodeDefinition.CodeDefinitionFields.CODE.getFieldName()));
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        columns.add(makePropertyColumn(CodeDefinition.CodeDefinitionFields.SORT.getFieldName()));
        columns.add(makeBooleanPropertyColumn(CodeDefinition.CodeDefinitionFields.INTERNAL.getFieldName(),
            CodeDefinition::isInternal));
        return columns;
    }

    private void onTitleClick(final IModel<CodeDefinition> model) {
        setResponsePage(new CodeEditPage(model, getPage().getPageReference()));
    }
}
