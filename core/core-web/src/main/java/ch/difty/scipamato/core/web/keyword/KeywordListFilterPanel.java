package ch.difty.scipamato.core.web.keyword;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
abstract class KeywordListFilterPanel
    extends DefinitionListFilterPanel<KeywordDefinition, KeywordFilter, KeywordService, KeywordDefinitionProvider> {

    KeywordListFilterPanel(@NotNull final String id, @NotNull final KeywordDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueFieldAndLabel(new TextField<>("name",
            PropertyModel.of(getFilter(), KeywordFilter.KeywordFilterFields.NAME_MASK.getFieldName())));

        queueNewKeywordButton("newKeyword");
    }

    private void queueNewKeywordButton(final String id) {
        queue(doQueueNewKeywordButton(id));
    }

    protected abstract BootstrapAjaxButton doQueueNewKeywordButton(@NotNull final String id);
}
