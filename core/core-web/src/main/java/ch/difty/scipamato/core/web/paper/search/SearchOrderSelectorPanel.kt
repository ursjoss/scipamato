package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.AbstractPanel
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.CoreEntity
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchOrder.SearchOrderFields
import ch.difty.scipamato.core.persistence.SearchOrderService
import ch.difty.scipamato.core.web.common.BasePanel
import ch.difty.scipamato.core.web.model.SearchOrderModel
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean

// HARDCODED static number of search orders to be visible in the select box.
// Might need to become more dynamic
private const val SEARCH_ORDER_MAX = 200

/**
 * Panel offering the user the option of:
 *
 *
 *  * selecting from previously saved search orders via a select box
 *  * changing the name and/or global flag of search orders
 *  * changing whether excluded papers are excluded or selected
 *  * saving new or modified search orders
 *
 *
 *
 * Once a modification has been issued, the panel will issue a
 * [SearchOrderChangeEvent] to the page. The page and other panels within
 * the page can then react to the new or modified selection.
 *
 * @author u.joss
 */
@Suppress("SameParameterValue")
class SearchOrderSelectorPanel internal constructor(
    id: String,
    model: IModel<SearchOrder?>?,
    mode: Mode
) : BasePanel<SearchOrder?>(id, model, mode) {

    @SpringBean
    private lateinit var searchOrderService: SearchOrderService
    private lateinit var form: Form<SearchOrder?>
    private lateinit var searchOrder: BootstrapSelect<SearchOrder?>
    private lateinit var name: TextField<String?>
    private lateinit var global: CheckBoxX
    private lateinit var showExcluded: AjaxCheckBox
    private lateinit var showExcludedLabel: Label

    override fun onInitialize() {
        super.onInitialize()
        queueForm("form")
    }

    private fun queueForm(id: String) {
        form = Form(id, CompoundPropertyModel(model)).also {
            queue(it)
        }
        makeAndQueueSearchOrderSelectBox("searchOrder")
        makeAndQueueName(SearchOrderFields.NAME.fieldName)
        makeAndQueueGlobalCheckBox(SearchOrderFields.GLOBAL.fieldName)
        makeAndQueueNewButton("new")
        makeAndQueueDeleteButton("delete")
        makeAndQueueShowExcludedCheckBox(SearchOrderFields.SHOW_EXCLUDED.fieldName)
    }

    private fun makeAndQueueSearchOrderSelectBox(id: String) {
        val choices = SearchOrderModel(activeUser.id!!, SEARCH_ORDER_MAX)
        val choiceRenderer: IChoiceRenderer<SearchOrder?> = ChoiceRenderer(
            CoreEntity.CoreEntityFields.DISPLAY_VALUE.fieldName,
            IdScipamatoEntity.IdScipamatoEntityFields.ID.fieldName)
        val noneSelectedModel = StringResourceModel("$id.noneSelected", this, null)
        val config = BootstrapSelectConfig()
            .withNoneSelectedText(noneSelectedModel.getObject())
            .withLiveSearch(true)
        searchOrder = BootstrapSelect(id, model, choices, choiceRenderer).with(config).apply {
            add(object : AjaxFormComponentUpdatingBehavior(CHANGE) {
                override fun onUpdate(target: AjaxRequestTarget) {
                    modelChanged()
                    target.add(global)
                    target.add(name)
                    target.add(showExcluded)
                    target.add(showExcludedLabel)
                    send(page, Broadcast.BREADTH, SearchOrderChangeEvent(target))
                }
            })
            add(AttributeModifier("data-width", "fit"))
        }.also { queue(it) }
    }

    private fun makeAndQueueName(id: String) {
        name = object : TextField<String?>(id) {
            override fun onConfigure() {
                super.onConfigure()
                isEnabled = isUserEntitled
            }
        }.apply {
            convertEmptyInputStringToNull = true
            outputMarkupId = true
            add(object : AjaxFormComponentUpdatingBehavior(CHANGE) {
                override fun onUpdate(target: AjaxRequestTarget) {
                    saveOrUpdate()
                    target.apply {
                        add(name)
                        add(global)
                        add(showExcluded)
                        add(showExcludedLabel)
                    }
                    send(page, Broadcast.BREADTH, SearchOrderChangeEvent(target))
                }
            })
        }.also { queue(it) }

        StringResourceModel(id + AbstractPanel.LABEL_RESOURCE_TAG, this, null).also {
            queue(Label(id + AbstractPanel.LABEL_TAG, it))
        }
    }

    private fun makeAndQueueGlobalCheckBox(id: String) {
        global = object : CheckBoxX(id) {
            override fun onConfigure() {
                super.onConfigure()
                isEnabled = !isViewMode && isUserEntitled
            }

            override fun onChange(value: Boolean?, target: AjaxRequestTarget) {
                super.onChange(value, target)
                saveOrUpdate()
                target.add(searchOrder)
            }
        }.apply {
            outputMarkupId = true
            config.withThreeState(false).withUseNative(true)
        }.also {
            queueCheckBoxAndLabel(it)
        }
    }

    private fun makeAndQueueNewButton(id: String) {
        queue(object : AjaxSubmitLink(id, form) {
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                target.apply {
                    add(name)
                    add(global)
                    add(showExcluded)
                    add(showExcludedLabel)
                }
                send(page, Broadcast.BREADTH, SearchOrderChangeEvent(target).requestingNewSearchOrder())
            }
        }.apply {
            add(ButtonBehavior())
            body = StringResourceModel("button.new.label")
            defaultFormProcessing = false
        })
    }

    private fun saveOrUpdate() {
        searchOrderService.saveOrUpdate(modelObject!!)?.let {
            form.defaultModelObject = it
        }
    }

    private val isModelSelected: Boolean
        get() = modelObject?.id != null

    private val isUserEntitled: Boolean
        get() = modelObject != null &&
            if (isViewMode)
                !modelObject!!.isGlobal && modelObject!!.owner == activeUser.id
            else
                modelObject!!.owner == activeUser.id

    private fun hasExclusions(): Boolean =
        isModelSelected && modelObject!!.excludedPaperIds.isNotEmpty()

    private fun makeAndQueueDeleteButton(id: String) {
        queue(object : AjaxSubmitLink(id, form) {
            override fun onConfigure() {
                super.onConfigure()
                isEnabled = isModelSelected && isUserEntitled
            }

            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                modelObject?.let {
                    searchOrderService.remove(it)
                    setResponsePage(PaperSearchPage(PageParameters()))
                }
            }
        }.apply {
            add(ButtonBehavior())
            body = StringResourceModel("button.delete.label")
            defaultFormProcessing = false
            add(ConfirmationBehavior())
            outputMarkupId = true
        })
    }

    private fun makeAndQueueShowExcludedCheckBox(id: String) {
        showExcluded = object : AjaxCheckBox(id) {
            override fun onConfigure() {
                super.onConfigure()
                if (isVisible && !hasExclusions()) modelObject = false
                isVisible = hasExclusions()
            }

            override fun onUpdate(target: AjaxRequestTarget) {
                target.apply {
                    add(showExcluded)
                    add(showExcludedLabel)
                    send(page, Broadcast.BREADTH, ToggleExclusionsEvent(this))
                }
            }
        }.apply {
            outputMarkupPlaceholderTag = true
        }.also { queue(it) }

        showExcludedLabel = object : Label(
            id + AbstractPanel.LABEL_TAG,
            StringResourceModel(id + AbstractPanel.LABEL_RESOURCE_TAG, this, null)
        ) {
            override fun onConfigure() {
                super.onConfigure()
                isVisible = hasExclusions()
            }
        }.apply {
            outputMarkupPlaceholderTag = true
        }.also { queue(it) }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val CHANGE = "change"
    }
}
