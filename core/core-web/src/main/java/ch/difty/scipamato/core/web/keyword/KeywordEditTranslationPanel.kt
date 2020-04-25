package ch.difty.scipamato.core.web.keyword

import ch.difty.scipamato.common.web.AbstractPanel
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.IModel
import org.apache.wicket.model.StringResourceModel

internal abstract class KeywordEditTranslationPanel(
    id: String,
    model: IModel<KeywordDefinition?>?
) : DefinitionEditTranslationPanel<KeywordDefinition?, KeywordTranslation>(id, model) {

    override fun addColumns(item: Item<KeywordTranslation>) {
        item.add(Label("langCode"))
        item.add(TextField<String>("name"))
        item.add(newAddLink("addTranslation", item))
        item.add(newRemoveLink("removeTranslation", item))
    }

    @Suppress("SameParameterValue")
    private fun newAddLink(id: String, item: Item<KeywordTranslation>): AjaxLink<Void> = object : AjaxLink<Void>(id) {
        override fun onClick(target: AjaxRequestTarget) {
            val currentKt = item.modelObject
            val langCode = currentKt.langCode
            val newKt = KeywordTranslation(null, langCode, null, 1)
            this@KeywordEditTranslationPanel.modelObject!!.addTranslation(langCode, newKt)
            target.add(form)
        }
    }.apply<AjaxLink<Void>> {
        add(ButtonBehavior())
        body = StringResourceModel("button." + id + AbstractPanel.LABEL_RESOURCE_TAG)
    }

    private fun newRemoveLink(id: String, item: Item<KeywordTranslation>): AjaxLink<Void> =
        object : AjaxLink<Void>(id) {
            override fun onClick(target: AjaxRequestTarget) {
                val currentKt = item.modelObject
                val keywordDefinition = this@KeywordEditTranslationPanel.modelObject!!
                keywordDefinition.removeTranslation(currentKt)
                target.add(form)
            }
        }.apply<AjaxLink<Void>> {
            add(ButtonBehavior())
            body = StringResourceModel("button." + id + AbstractPanel.LABEL_RESOURCE_TAG)
            add(ConfirmationBehavior())
        }

    protected abstract val form: Form<*>
}
