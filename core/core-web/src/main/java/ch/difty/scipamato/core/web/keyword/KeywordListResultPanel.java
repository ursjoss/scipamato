package ch.difty.scipamato.core.web.keyword;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionListResultPanel;

@SuppressWarnings("SameParameterValue")
class KeywordListResultPanel extends DefinitionListResultPanel<KeywordDefinition, KeywordFilter, KeywordService, KeywordDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    KeywordListResultPanel(@NotNull final String id, @NotNull final KeywordDefinitionProvider provider) {
        super(id, provider);
    }

    @NotNull
    protected List<IColumn<KeywordDefinition, String>> makeTableColumns() {
        final List<IColumn<KeywordDefinition, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        columns.add(makePropertyColumn("searchOverride"));
        return columns;
    }

    private void onTitleClick(@Nullable final IModel<KeywordDefinition> model) {
        setResponsePage(new KeywordEditPage(model, getPage().getPageReference()));
    }
}
