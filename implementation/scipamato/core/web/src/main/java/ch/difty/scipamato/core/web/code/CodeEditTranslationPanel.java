package ch.difty.scipamato.core.web.code;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
class CodeEditTranslationPanel extends DefinitionEditTranslationPanel<CodeDefinition, CodeTranslation> {

    private static final long serialVersionUID = 1L;

    CodeEditTranslationPanel(@NotNull final String id, @Nullable final IModel<CodeDefinition> model) {
        super(id, model);
    }

    @Override
    protected void addColumns(@NotNull final Item<CodeTranslation> item) {
        item.add(new Label("langCode"));
        item.add(new TextField<String>("name"));
        item.add(new TextField<String>("comment"));
    }
}
