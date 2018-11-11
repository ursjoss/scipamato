package ch.difty.scipamato.core.web.keyword;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
abstract class KeywordListFilterPanel
    extends DefinitionListFilterPanel<KeywordDefinition, KeywordFilter, KeywordService, KeywordDefinitionProvider> {

    KeywordListFilterPanel(final String id, final KeywordDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueFieldAndLabel(new TextField<String>("name",
            PropertyModel.of(getFilter(), KeywordFilter.KeywordFilterFields.NAME_MASK.getName())));

        queueNewKeywordButton("newKeyword");
    }

    private void queueNewKeywordButton(final String id) {
        queue(doQueueNewKeywordButton(id));
    }

    protected abstract BootstrapAjaxButton doQueueNewKeywordButton(final String id);

}