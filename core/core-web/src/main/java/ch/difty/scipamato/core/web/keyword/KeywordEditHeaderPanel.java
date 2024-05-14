package ch.difty.scipamato.core.web.keyword;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.web.common.DeletableDefinitionEditHeaderPanel;

@SuppressWarnings("SameParameterValue")
public abstract class KeywordEditHeaderPanel extends DeletableDefinitionEditHeaderPanel<KeywordDefinition, KeywordTranslation, Integer> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    KeywordEditHeaderPanel(@NotNull final String id, @Nullable final IModel<KeywordDefinition> model) {
        super(id, model);
    }

    @Override
    protected void makeAndQueueFilterFields() {
        queueFieldAndLabel(new TextField<>(Keyword.KeywordFields.SEARCH_OVERRIDE.getFieldName()));
    }
}
