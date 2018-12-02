package ch.difty.scipamato.core.web.keyword;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditHeaderPanel;

@SuppressWarnings("SameParameterValue")
abstract class KeywordEditHeaderPanel
    extends DefinitionEditHeaderPanel<KeywordDefinition, KeywordTranslation, Integer> {

    KeywordEditHeaderPanel(final String id, final IModel<KeywordDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
        queueFieldAndLabel(new TextField<String>(Keyword.KeywordFields.SEARCH_OVERRIDE.getName()));
    }

}
