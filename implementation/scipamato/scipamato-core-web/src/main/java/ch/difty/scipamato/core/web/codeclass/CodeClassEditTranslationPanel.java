package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel;

@SuppressWarnings("SameParameterValue")
class CodeClassEditTranslationPanel extends DefinitionEditTranslationPanel<CodeClassDefinition, CodeTranslation> {

    CodeClassEditTranslationPanel(final String id, final IModel<CodeClassDefinition> model) {
        super(id, model);
    }

    @Override
    protected void addColumns(final Item<CodeTranslation> item) {
        item.add(new Label("langCode"));
        item.add(new TextField<String>("name"));
        item.add(new TextField<String>("description"));
    }
}
