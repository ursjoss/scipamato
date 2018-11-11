package ch.difty.scipamato.core.web.keyword;

import java.util.Iterator;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
abstract class KeywordEditTranslationPanel
    extends DefinitionEditTranslationPanel<KeywordDefinition, KeywordTranslation> {

    KeywordEditTranslationPanel(final String id, final IModel<KeywordDefinition> model) {
        super(id, model);
    }

    @Override
    protected void addColumns(final Item<KeywordTranslation> item) {
        item.add(new Label("langCode"));
        item.add(new TextField<String>("name"));
        item.add(newAddLink("addTranslation", item));
        item.add(newRemoveLink("removeTranslation", item));
    }

    private AjaxLink<Void> newAddLink(final String id, final Item<KeywordTranslation> item) {
        final AjaxLink<Void> newLink = new AjaxLink<>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                final KeywordTranslation currentKt = item.getModelObject();
                final String langCode = currentKt.getLangCode();
                final KeywordTranslation newKt = new KeywordTranslation(null, langCode, null, 1);
                KeywordEditTranslationPanel.this
                    .getModelObject()
                    .getTranslations()
                    .put(langCode, newKt);
                target.add(getForm());
            }
        };
        newLink.add(new ButtonBehavior());
        newLink.setBody(new StringResourceModel("button." + id + LABEL_RESOURCE_TAG));
        return newLink;
    }

    private AjaxLink<Void> newRemoveLink(final String id, final Item<KeywordTranslation> item) {
        final AjaxLink<Void> newLink = new AjaxLink<>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                final KeywordTranslation currentKt = item.getModelObject();
                final String langCode = currentKt.getLangCode();
                final Iterator<KeywordTranslation> it = KeywordEditTranslationPanel.this
                    .getModelObject()
                    .getTranslations()
                    .get(langCode)
                    .iterator();
                while (it.hasNext()) {
                    final KeywordTranslation kt = it.next();
                    if (currentKt.equals(kt)) {
                        it.remove();
                        break;
                    }
                }
                target.add(getForm());
            }
        };
        newLink.add(new ButtonBehavior());
        newLink.setBody(new StringResourceModel("button." + id + LABEL_RESOURCE_TAG));
        newLink.add(new ConfirmationBehavior());
        return newLink;
    }

    protected abstract Form getForm();
}
