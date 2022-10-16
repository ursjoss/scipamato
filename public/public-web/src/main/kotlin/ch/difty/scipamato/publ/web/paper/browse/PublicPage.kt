package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.common.web.TITLE_RESOURCE_TAG
import ch.difty.scipamato.common.web.component.SerializableConsumer
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.CodeClass
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.web.common.BasePage
import ch.difty.scipamato.publ.web.model.CodeClassModel
import ch.difty.scipamato.publ.web.model.CodeModel
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.apache.wicket.AttributeModifier
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab
import org.apache.wicket.extensions.markup.html.tabs.ITab
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.wicketstuff.annotation.mount.MountPath

@WicketHomePage
@MountPath("/")
@Suppress("SameParameterValue", "UnnecessaryAbstractClass", "TooManyFunctions")
class PublicPage(parameters: PageParameters) : BasePage<Void>(parameters) {

    private val filter: PublicPaperFilter = PublicPaperFilter()
    private val dataProvider = PublicPaperProvider(filter, RESULT_PAGE_SIZE)
    private var isQueryingInitialized = false

    override fun onInitialize() {
        super.onInitialize()
        makeAndQueueFilterForm("searchForm")
        makeAndQueueResultTable("results")
    }

    private fun makeAndQueueFilterForm(id: String) {
        object : FilterForm<PublicPaperFilter>(id, dataProvider) {
            override fun onSubmit() {
                super.onSubmit()
                updateNavigateable()
            }
        }.also {
            queue(it)
            addTabPanel(it, "tabs")
            queueQueryButton("query", it)
        }
        queueClearSearchButton("clear")
        queueHelpLink("help")
    }

    private fun addTabPanel(
        filterForm: FilterForm<PublicPaperFilter>,
        tabId: String,
    ) {
        mutableListOf<ITab>().apply {
            add(object : AbstractTab(StringResourceModel("tab1$LABEL_RESOURCE_TAG", this@PublicPage, null)) {
                override fun getPanel(panelId: String): Panel = TabPanel1(panelId, Model.of(filter))
            })
            add(object : AbstractTab(StringResourceModel("tab2$LABEL_RESOURCE_TAG", this@PublicPage, null)) {
                override fun getPanel(panelId: String): Panel = TabPanel2(panelId, Model.of(filter))
            })
        }.also {
            filterForm.add(BootstrapTabbedPanel(tabId, it))
        }
    }

    private inner class TabPanel1(
        id: String,
        model: IModel<PublicPaperFilter>,
    ) : AbstractTabPanel(id, model) {
        override fun onInitialize() {
            super.onInitialize()
            queue(Form<Any>("tab1Form"))
            queue(SimpleFilterPanel("simpleFilterPanel", Model.of(filter), languageCode))
        }
    }

    private inner class TabPanel2(
        id: String,
        model: IModel<PublicPaperFilter>,
    ) : AbstractTabPanel(id, model) {
        override fun onInitialize() {
            super.onInitialize()
            val form = Form<Any>("tab2Form")

            queue(form)
            queue(SimpleFilterPanel("simpleFilterPanel", Model.of(filter), languageCode))

            CodeClassModel(languageCode).getObject().apply {
                makeCodeClassComplex(form, CodeClassId.CC1, this)
                makeCodeClassComplex(form, CodeClassId.CC2, this)
                makeCodeClassComplex(form, CodeClassId.CC3, this)
                makeCodeClassComplex(form, CodeClassId.CC4, this)
                makeCodeClassComplex(form, CodeClassId.CC5, this)
                makeCodeClassComplex(form, CodeClassId.CC6, this)
                makeCodeClassComplex(form, CodeClassId.CC7, this)
                makeCodeClassComplex(form, CodeClassId.CC8, this)
            }
        }

        private fun makeCodeClassComplex(
            form: Form<Any>,
            codeClassId: CodeClassId,
            codeClasses: List<CodeClass>,
        ) {
            val id = codeClassId.id
            val componentId = "$CODES_CLASS_BASE_NAME$id"
            val className = codeClasses
                .filter { it.codeClassId == id }
                .map { it.name }
                .firstOrNull() ?: codeClassId.name
            val model = PropertyModel.of<List<Code>>(filter, componentId)
            val choices = CodeModel(codeClassId, languageCode)
            val choiceRenderer = ChoiceRenderer<Code>("displayValue", "code")
            val config = BootstrapSelectConfig()
                .withMultiple(true)
                .withActionsBox(choices.getObject().size > properties.multiSelectBoxActionBoxWithMoreEntriesThan)
                .withSelectAllText(StringResourceModel(SELECT_ALL_RESOURCE_TAG, this, null).string)
                .withDeselectAllText(StringResourceModel(DESELECT_ALL_RESOURCE_TAG, this, null).string)
                .withNoneSelectedText(StringResourceModel(CODES_NONE_SELECT_RESOURCE_TAG, this, null).string)
                .withLiveSearch(true)
                .withLiveSearchStyle("startsWith")

            form.add(Label("$componentId$LABEL_TAG", Model.of(className)))
            form.add(BootstrapMultiSelect(componentId, model, choices, choiceRenderer).with(config))
        }
    }

    private abstract class AbstractTabPanel(id: String, model: IModel<*>?) : Panel(id, model) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    private fun queueQueryButton(
        id: String,
        filterForm: FilterForm<PublicPaperFilter>,
    ) {
        val labelModel = StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$LABEL_RESOURCE_TAG", this, null)
        object : BootstrapButton(id, labelModel, Buttons.Type.Primary) {
            override fun onSubmit() {
                super.onSubmit()
                isQueryingInitialized = true
            }
        }.also {
            queue(it)
            filterForm.defaultButton = it
        }
    }

    private fun queueClearSearchButton(id: String) {
        val labelModel = StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$LABEL_RESOURCE_TAG", this, null)
        val titleModel = StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$TITLE_RESOURCE_TAG", this, null)
        object : BootstrapButton(id, labelModel, Buttons.Type.Default) {
            override fun onSubmit() {
                super.onSubmit()
                setResponsePage(PublicPage(pageParameters))
            }
        }.apply { add(AttributeModifier("title", titleModel)) }
            .also { queue(it) }
    }

    private fun queueHelpLink(id: String) {
        object : BootstrapExternalLink(id, StringResourceModel("$id.url", this, null)) {
        }.apply {
            setTarget(BootstrapExternalLink.Target.blank)
            setLabel(StringResourceModel(id + LABEL_RESOURCE_TAG, this, null))
        }.also {
            queue(it)
        }
    }

    private fun makeAndQueueResultTable(id: String) {
        object : BootstrapDefaultDataTable<PublicPaper, String>(id, makeTableColumns(), dataProvider,
            dataProvider.rowsPerPage.toLong()) {
            override fun onConfigure() {
                super.onConfigure()
                isVisible = isQueryingInitialized
            }

            override fun onAfterRender() {
                super.onAfterRender()
                paperIdManager.initialize(this@PublicPage.dataProvider.findAllPaperNumbersByFilter())
            }
        }.apply {
            outputMarkupId = true
            add(TableBehavior().striped().hover())
        }.also {
            queue(it)
        }
    }

    private fun makeTableColumns() = mutableListOf<IColumn<PublicPaper, String>>().apply {
        add(makePropertyColumn("authorsAbbreviated", "authors"))
        add(makeClickableColumn("title", this@PublicPage::onTitleClick))
        add(makePropertyColumn("journal", "location"))
        add(makePropertyColumn("publicationYear", "publicationYear"))
    }

    private fun makePropertyColumn(fieldType: String, sortField: String) = PropertyColumn<PublicPaper, String>(
        StringResourceModel("$COLUMN_HEADER${fieldType}", this, null),
        sortField,
        fieldType,
    )

    private fun makeClickableColumn(
        fieldType: String,
        consumer: SerializableConsumer<IModel<PublicPaper>>,
    ) = ClickablePropertyColumn(
        displayModel = StringResourceModel("$COLUMN_HEADER$fieldType", this, null),
        property = fieldType,
        action = consumer,
        sort = fieldType,
        inNewTab = true,
    )

    /*
     * Note: The PaperIdManger manages the number in scipamato-public, not the id
     */
    private fun onTitleClick(m: IModel<PublicPaper>) {
        m.getObject().number?.let { paperIdManager.setFocusToItem(it) }
        setResponsePage(PublicPaperDetailPage(m, page.pageReference, showBackButton = false))
    }

    /**
     * Have the provider provide a list of all paper numbers (business key) matching the current filter.
     * Construct a navigateable with this list and set it into the session
     */
    private fun updateNavigateable() {
        paperIdManager.initialize(dataProvider.findAllPaperNumbersByFilter())
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val RESULT_PAGE_SIZE = 20
        private const val COLUMN_HEADER = "column.header."
        private const val CODES_CLASS_BASE_NAME = "codesOfClass"
        private const val CODES_NONE_SELECT_RESOURCE_TAG = "codes.noneSelected"
        private const val BUTTON_RESOURCE_PREFIX = "button."
    }
}
