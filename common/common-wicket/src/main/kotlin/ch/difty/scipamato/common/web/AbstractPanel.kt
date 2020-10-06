package ch.difty.scipamato.common.web

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.panel.GenericPanel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.StringResourceModel

abstract class AbstractPanel<T> @JvmOverloads constructor(
    id: String,
    model: IModel<T>? = null,
    open val mode: Mode = Mode.VIEW,
) : GenericPanel<T>(id, model) {

    val isSearchMode: Boolean
        get() = mode === Mode.SEARCH
    val isEditMode: Boolean
        get() = mode === Mode.EDIT
    val isViewMode: Boolean
        get() = mode === Mode.VIEW

    val submitLinkResourceLabel: String
        get() = when (mode) {
            Mode.EDIT -> "button.save.label"
            Mode.SEARCH -> "button.search.label"
            Mode.VIEW -> "button.disabled.label"
        }

    @JvmOverloads
    fun queueFieldAndLabel(field: FormComponent<*>, pv: PropertyValidator<*>? = null) {
        field.labelled().apply {
            outputMarkupId = true
        }.also {
            queue(it)
        }
        if (isEditMode) pv?.let { field.add(it) }
    }

    protected fun queueCheckBoxAndLabel(field: CheckBoxX) {
        queue(field.labelled())
    }

    private fun FormComponent<*>.labelled(): FormComponent<*> {
        val fieldId = id
        val labelModel = StringResourceModel("$fieldId$LABEL_RESOURCE_TAG", this@AbstractPanel, null)
        label = labelModel
        this@AbstractPanel.queue(Label("$fieldId$LABEL_TAG", labelModel))
        return this
    }

    companion object {
        private const val serialVersionUID = 1L
        const val SELECT_ALL_RESOURCE_TAG = "multiselect.selectAll"
        const val DESELECT_ALL_RESOURCE_TAG = "multiselect.deselectAll"
    }
}

enum class Mode {
    EDIT, VIEW, SEARCH
}
