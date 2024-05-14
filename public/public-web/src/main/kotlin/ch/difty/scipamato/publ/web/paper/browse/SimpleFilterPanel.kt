package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.web.AbstractPanel
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.entity.PopulationCode
import ch.difty.scipamato.publ.entity.StudyDesignCode
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.web.model.KeywordModel
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.event.Broadcast
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.EnumChoiceRenderer
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import kotlin.reflect.KProperty1

/**
 * The SimpleFilterPanel is added to the PublicPage twice and should be kept synchronized.
 * The fields are therefore change-aware, i.e. send an event upon changes letting other fields update.
 */
open class SimpleFilterPanel(
    id: String,
    model: IModel<PublicPaperFilter>,
    private val languageCode: String,
) : AbstractPanel<PublicPaperFilter>(id, model) {

    override fun onInitialize() {
        super.onInitialize()
        PublicPaperFilter::methodsMask.queueAsTextFieldWithLabel("methodsSearch")
        PublicPaperFilter::authorMask.queueAsTextFieldWithLabel("authorsSearch")
        PublicPaperFilter::publicationYearFrom.queueAsTextFieldWithLabel("pubYearFrom")
        PublicPaperFilter::publicationYearUntil.queueAsTextFieldWithLabel("pubYearUntil")
        PublicPaperFilter::populationCodes.addCodesComplex("populationCodes", PopulationCode.entries, "160px")
        PublicPaperFilter::studyDesignCodes.addCodesComplex("studyDesignCodes", StudyDesignCode.entries, "220px")
        PublicPaperFilter::keywords.queueKeywordMultiselect("keywords")
        PublicPaperFilter::titleMask.queueAsTextFieldWithLabel("titleSearch")
    }

    private fun <V> KProperty1<PublicPaperFilter, V>.queueAsTextFieldWithLabel(id: String) {
        object : TextField<String>(id, PropertyModel.of(this@SimpleFilterPanel.model, name)) {
            private val serialVersionUID: Long = 1
            override fun onEvent(event: IEvent<*>) {
                handleChangeEvent(event, this)
            }
        }.apply<TextField<String>> {
            add(object : AjaxFormComponentUpdatingBehavior(CHANGE) {
                private val serialVersionUID: Long = 1
                override fun onUpdate(target: AjaxRequestTarget) {
                    sendChangeEvent(target, this@apply)
                }
            })
            label = StringResourceModel("$id$LABEL_RESOURCE_TAG", this@SimpleFilterPanel, null)
        }.also {
            queue(it)
        }
        queue(Label("$id$LABEL_TAG", StringResourceModel("$id$LABEL_RESOURCE_TAG", this@SimpleFilterPanel, null)))
    }

    private fun sendChangeEvent(target: AjaxRequestTarget, component: FormComponent<*>) {
        send(page, Broadcast.BREADTH, SimpleFilterPanelChangeEvent(target, component.id, component.markupId))
    }

    // package private access for testing
    open fun handleChangeEvent(event: IEvent<*>, component: FormComponent<*>) {
        if (event.payload is SimpleFilterPanelChangeEvent) {
            (event.payload as SimpleFilterPanelChangeEvent).considerAddingToTarget(component)
            event.dontBroadcastDeeper()
        }
    }

    private fun <C : Enum<C>, V> KProperty1<PublicPaperFilter, V>.addCodesComplex(
        id: String,
        values: List<C>,
        width: String,
    ) {
        StringResourceModel("$id$LABEL_RESOURCE_TAG", this@SimpleFilterPanel, null).also {
            queue(Label("$id$LABEL_TAG", it))
        }

        val config = BootstrapSelectConfig()
            .withMultiple(true)
            .withLiveSearch(true)
            .withLiveSearchStyle("startsWith")
            .withSelectAllText(StringResourceModel(SELECT_ALL_RESOURCE_TAG, this@SimpleFilterPanel, null).string)
            .withDeselectAllText(StringResourceModel(DESELECT_ALL_RESOURCE_TAG, this@SimpleFilterPanel, null).string)
            .withNoneSelectedText(
                StringResourceModel(CODES_NONE_SELECT_RESOURCE_TAG, this@SimpleFilterPanel, null).string
            )

        val model = PropertyModel.of<Collection<C>>(this@SimpleFilterPanel.model, name)
        @Suppress("SpreadOperator")
        object : BootstrapMultiSelect<C>(id, model, values, EnumChoiceRenderer(this@SimpleFilterPanel)) {
            private val serialVersionUID: Long = 1
            override fun onEvent(event: IEvent<*>) {
                handleChangeEvent(event, this)
            }
        }.with(config).apply {
            add(object : AjaxFormComponentUpdatingBehavior(CHANGE) {
                private val serialVersionUID: Long = 1
                override fun onUpdate(target: AjaxRequestTarget) {
                    sendChangeEvent(target, this@apply)
                }
            })
            add(AttributeModifier(AM_DATA_WIDTH, width))
        }.also {
            queue(it)
        }
    }

    @Suppress("SameParameterValue")
    private fun KProperty1<PublicPaperFilter, List<Keyword>?>.queueKeywordMultiselect(id: String) {
        val model = PropertyModel.of<List<Keyword>>(this@SimpleFilterPanel.model, name)
        val choiceRenderer = ChoiceRenderer<Keyword>("displayValue", "id")
        val config = BootstrapSelectConfig()
            .withMultiple(true)
            .withActionsBox(true)
            .withSelectAllText(StringResourceModel(SELECT_ALL_RESOURCE_TAG, this@SimpleFilterPanel, null).string)
            .withDeselectAllText(StringResourceModel(DESELECT_ALL_RESOURCE_TAG, this@SimpleFilterPanel, null).string)
            .withNoneSelectedText(
                StringResourceModel(KEYWORDS_NONE_SELECT_RESOURCE_TAG, this@SimpleFilterPanel, null).string
            )
            .withLiveSearch(true)
            .withLiveSearchStyle("startsWith")
        object : BootstrapMultiSelect<Keyword>(id, model, KeywordModel(languageCode), choiceRenderer) {
            private val serialVersionUID: Long = 1
            override fun onEvent(event: IEvent<*>) {
                handleChangeEvent(event, this)
            }
        }.with(config).apply {
            add(object : AjaxFormComponentUpdatingBehavior(CHANGE) {
                private val serialVersionUID: Long = 1
                override fun onUpdate(target: AjaxRequestTarget) {
                    sendChangeEvent(target, this@apply)
                }
            })
        }.also {
            queue(it)
        }
        queue(Label("$id$LABEL_TAG", StringResourceModel("$id$LABEL_RESOURCE_TAG", this@SimpleFilterPanel, null)))
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val CODES_NONE_SELECT_RESOURCE_TAG = "codes.noneSelected"
        private const val KEYWORDS_NONE_SELECT_RESOURCE_TAG = "keywords.noneSelected"
        private const val AM_DATA_WIDTH = "data-width"
        private const val CHANGE = "change"
    }
}
