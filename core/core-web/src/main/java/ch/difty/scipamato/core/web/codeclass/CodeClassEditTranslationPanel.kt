package ch.difty.scipamato.core.web.codeclass

import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation
import ch.difty.scipamato.core.web.common.DefinitionEditTranslationPanel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.IModel

internal class CodeClassEditTranslationPanel(
    id: String,
    model: IModel<CodeClassDefinition?>?
) : DefinitionEditTranslationPanel<CodeClassDefinition?, CodeClassTranslation>(id, model) {

    override fun addColumns(item: Item<CodeClassTranslation>) {
        item.add(Label("langCode"))
        item.add(TextField<String>("name"))
        item.add(TextField<String>("description"))
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
