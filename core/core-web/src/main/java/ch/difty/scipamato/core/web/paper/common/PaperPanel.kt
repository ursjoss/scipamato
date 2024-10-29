package ch.difty.scipamato.core.web.paper.common

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.FieldEnumType
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.common.web.component.SerializableSupplier
import ch.difty.scipamato.core.AttachmentAware
import ch.difty.scipamato.core.NewsletterAware
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.CodeBoxAware
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.CoreEntity
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import ch.difty.scipamato.core.entity.Paper.PaperFields
import ch.difty.scipamato.core.entity.PaperAttachment
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.web.common.BasePanel
import ch.difty.scipamato.core.web.common.SelfUpdateBroadcastingBehavior
import ch.difty.scipamato.core.web.common.SelfUpdateEvent
import ch.difty.scipamato.core.web.model.CodeClassModel
import ch.difty.scipamato.core.web.model.CodeModel
import ch.difty.scipamato.core.web.model.NewsletterTopicModel
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.BootstrapFileInput
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconType
import org.apache.wicket.AttributeModifier
import org.apache.wicket.PageReference
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.behavior.AttributeAppender
import org.apache.wicket.event.Broadcast
import org.apache.wicket.event.IEvent
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab
import org.apache.wicket.extensions.markup.html.tabs.ITab
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator
import org.apache.wicket.markup.html.form.validation.IFormValidator
import org.apache.wicket.markup.html.link.ResourceLink
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.ChainingModel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.validation.IValidator

private const val CODES_CLASS_BASE_NAME = "codesClass"

@Suppress("SameParameterValue", "TooManyFunctions")
abstract class PaperPanel<T>(
    id: String,
    model: IModel<T>?,
    mode: Mode,
    val callingPage: PageReference?,
    protected val tabIndexModel: IModel<Int>,
) : BasePanel<T>(
    id,
    model,
    mode
) where T : CodeBoxAware, T : NewsletterAware, T : AttachmentAware {

    private var summaryLink: ResourceLink<Unit>? = null
    private var summaryShortLink: ResourceLink<Unit>? = null

    var authors: TextArea<String>? = null
    var firstAuthor: TextField<String>? = null
    var title: TextArea<Any>? = null
    var location: TextField<Any>? = null
    var publicationYear: TextField<Any>? = null
    var pmId: TextField<Any>? = null
    var doi: TextField<Any>? = null
    var originalAbstract: TextArea<String>? = null

    private var pubmedRetrieval: BootstrapAjaxLink<Unit>? = null
    private var attachments: DataTable<PaperAttachment, String>? = null
    private var submit: BootstrapButton? = null
    private val newsletterTopicChoice = NewsletterTopicModel(localization)

    private lateinit var form: Form<T>

    internal constructor(
        id: String,
        model: IModel<T>?,
        mode: Mode,
    ) : this(
        id = id,
        model = model,
        mode = mode,
        callingPage = null,
        tabIndexModel = Model.of<Int>(0)
    )

    protected fun getAttachments(): DataTable<PaperAttachment, String> = attachments!!

    public override fun onInitialize() {
        super.onInitialize()
        form = object : Form<T>("form", CompoundPropertyModel(model)) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit() {
                super.onSubmit()
                onFormSubmit()
            }
        }.apply { setMultiPart(true) }.also { queue(it) }
        queueHeaderFields()
        queueTabPanel("tabs")
        queuePubmedRetrievalLink("pubmedRetrieval")
    }

    protected abstract fun onFormSubmit()

    fun getForm(): Form<T> = form

    @Suppress("LongMethod")
    private fun queueHeaderFields() {
        queueAuthorComplex(
            PaperFields.AUTHORS.fieldName,
            PaperFields.FIRST_AUTHOR.fieldName,
            PaperFields.FIRST_AUTHOR_OVERRIDDEN.fieldName
        )
        title = TextArea(PaperFields.TITLE.fieldName)
        val pm = paperIdManager
        queue(newNavigationButton("previous", FontAwesome6IconType.backward_step_s, { pm.hasPrevious() }) {
            pm.previous()
            pm.itemWithFocus
        })
        queue(newNavigationButton("next", FontAwesome6IconType.forward_step_s, { pm.hasNext() }) {
            pm.next()
            pm.itemWithFocus
        })
        queueFieldAndLabel(title!!, PropertyValidator<Any>())
        location = TextField(PaperFields.LOCATION.fieldName)
        queueFieldAndLabel(location!!, PropertyValidator<Any>())
        publicationYear = TextField(PaperFields.PUBL_YEAR.fieldName)
        queueFieldAndLabel(publicationYear!!, PropertyValidator<Any>())
        pmId = TextField(PaperFields.PMID.fieldName)
        pmId!!.add(newPmIdChangeBehavior())
        queueFieldAndLabel(pmId!!)
        doi = TextField(PaperFields.DOI.fieldName)
        queueFieldAndLabel(doi!!, PropertyValidator<Any>())
        addDisableBehavior(title!!, location!!, publicationYear!!, pmId!!, doi!!)
        queueNumberField(PaperFields.NUMBER.fieldName)
        val id = newSelfUpdatingTextField<Int>(IdScipamatoEntity.IdScipamatoEntityFields.ID.fieldName)
        id.isEnabled = isSearchMode
        queueFieldAndLabel(id)
        val created = newSelfUpdatingTextField<String>(CoreEntity.CoreEntityFields.CREATED_DV.fieldName)
        created.isEnabled = isSearchMode
        queueFieldAndLabel(created)
        val modified = newSelfUpdatingTextField<String>(CoreEntity.CoreEntityFields.MODIFIED_DV.fieldName)
        modified.isEnabled = isSearchMode
        queueFieldAndLabel(modified)
        makeAndQueueBackButton("back") { paperIdManager.isModified }
        queue(newExcludeButton("exclude"))
        makeAndQueueSubmitButton("submit")
        summaryLink = makeSummaryLink("summary")
        form.addOrReplace(summaryLink)
        summaryShortLink = makeSummaryShortLink("summaryShort")
        form.addOrReplace(summaryShortLink)
        considerAddingMoreValidation()
        val addRemoveNewsletter: BootstrapAjaxLink<Unit> = object :
            BootstrapAjaxLink<Unit>("modAssociation", Buttons.Type.Primary) {
            override fun onInitialize() {
                super.onInitialize()
                add(ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Buttons.Size.Medium))
            }

            override fun onClick(target: AjaxRequestTarget) {
                modifyNewsletterAssociation(target)
            }

            override fun onConfigure() {
                super.onConfigure()
                isVisible = shallBeVisible()
                isEnabled = shallBeEnabled()
                add(AttributeModifier(TITLE_ATTR, titleResourceModel))
                setIconType(iconType)
            }

            // Hide if in EditMode
            // Otherwise show if we have an open newsletter or if it is already assigned to a (closed) newsletter
            private fun shallBeVisible() = isEditMode && (isaNewsletterInStatusWip() || isAssociatedWithNewsletter)
            private fun shallBeEnabled() = !isAssociatedWithNewsletter || isAssociatedWithWipNewsletter
            private val serialVersionUID: Long = 1L

            private val titleResourceModel: StringResourceModel
                get() = StringResourceModel("modNewsletterAssociation-$titleKey.title", this, this@PaperPanel.model)
            private val titleKey: String
                get() {
                    if (!isAssociatedWithNewsletter) return "add"
                    return if (isAssociatedWithWipNewsletter) "del" else "closed"
                }

            // Show the + icon if not assigned to a newsletter yet
            // Otherwise: Show the open envelope if assigned to current, closed envelope if assigned to closed nl.
            private val iconType: IconType
                get() = if (!isAssociatedWithNewsletter)
                    FontAwesome6IconType.square_plus_s
                else if (isAssociatedWithWipNewsletter)
                    FontAwesome6IconType.envelope_open_r
                else FontAwesome6IconType.envelope_r
        }
        addRemoveNewsletter.outputMarkupPlaceholderTag = true
        queue(addRemoveNewsletter)
    }

    private fun <U> newSelfUpdatingTextField(id: String): TextField<U> = object :
        TextField<U>(id) {
        private val serialVersionUID: Long = 1L
        override fun onEvent(event: IEvent<*>) {
            super.onEvent(event)
            if (event.payload.javaClass == SelfUpdateEvent::class.java) {
                (event.payload as SelfUpdateEvent).target.add(this)
                event.dontBroadcastDeeper()
            }
        }
    }

    private fun queueNumberField(id: String) {
        val labelModel: IModel<String> = Model.of(
            "${firstWordOfBrand()}-${StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null).string}")
        queue(Label("$id$LABEL_TAG", labelModel))
        TextField<Int>(id).apply {
            label = labelModel
            outputMarkupId = true
            if (isEditMode) add(PropertyValidator<Int>() as IValidator<Int>)
        }.also {
            queue(it)
            addDisableBehavior(it)
        }
    }

    private fun firstWordOfBrand(): String {
        val brand = properties.brand
        val divider = " "
        return if (brand.contains(divider)) brand.substring(0, brand.indexOf(divider)) else brand
    }

    /**
     * If more validators are required, override this
     */
    protected open fun considerAddingMoreValidation() {
        // no default implementation
    }

    private fun newPmIdChangeBehavior(): OnChangeAjaxBehavior = object :
        OnChangeAjaxBehavior() {
        private val serialVersionUID: Long = 1L
        override fun onUpdate(target: AjaxRequestTarget) {
            target.add(pubmedRetrieval)
        }
    }

    private fun queueTabPanel(tabId: String) {
        mutableListOf<ITab>().apply {
            add(object : AbstractTab(StringResourceModel("tab1$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel1(panelId, form.model)
            })
            add(object : AbstractTab(StringResourceModel("tab2$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel2(panelId, form.model)
            })
            add(object : AbstractTab(StringResourceModel("tab3$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel3(panelId, form.model)
            })
            add(object : AbstractTab(StringResourceModel("tab4$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel4(panelId, form.model)
            })
            add(object : AbstractTab(StringResourceModel("tab5$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel5(panelId, form.model)
            })
            add(object : AbstractTab(StringResourceModel("tab6$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel6(panelId, form.model)
            })
            add(object : AbstractTab(StringResourceModel("tab7$LABEL_RESOURCE_TAG", this@PaperPanel, null)) {
                private val serialVersionUID: Long = 1L
                override fun getPanel(panelId: String): Panel = TabPanel7(panelId, form.model)
            })
        }.also {
            queue(BootstrapTabbedPanel(tabId, it, tabIndexModel))
        }
    }

    /*
     * The author's field determines the firstAuthor field, but only unless overridden.
     * Changes in the author field (if not overridden) or in the override checkbox
     * can have an effect on the firstAuthor field (enabled, content)
     */
    private fun queueAuthorComplex(authorsId: String, firstAuthorId: String, firstAuthorOverriddenId: String) {
        authors = TextArea(authorsId)
        authors!!.escapeModelStrings = false
        queueFieldAndLabel(authors!!, PropertyValidator<Any>())
        val firstAuthorOverriddenModel = PropertyModel<Boolean>(model, firstAuthorOverriddenId)
        val firstAuthorOverridden = CheckBoxX(firstAuthorOverriddenId, firstAuthorOverriddenModel)
        firstAuthorOverridden
            .config
            .withThreeState(isSearchMode)
            .withUseNative(true)
        queueCheckBoxAndLabel(firstAuthorOverridden)
        firstAuthor = makeFirstAuthor(firstAuthorId, firstAuthorOverridden)
        firstAuthor!!.outputMarkupId = true
        queueFieldAndLabel(firstAuthor!!)
        addAuthorBehavior(authors!!, firstAuthorOverridden, firstAuthor!!)
        addDisableBehavior(authors!!, firstAuthor!!)
    }

    private fun addDisableBehavior(vararg components: FormComponent<*>) {
        if (isEditMode || isSearchMode) for (fc in components) {
            fc.add(object : AjaxFormComponentUpdatingBehavior("input") {
                private val serialVersionUID: Long = 1L
                override fun onUpdate(target: AjaxRequestTarget) {
                    disableButton(target, submit!!)
                }
            })
        }
    }

    private fun disableButton(target: AjaxRequestTarget, vararg buttons: BootstrapButton) {
        for (b in buttons) {
            if (b.isEnabled) {
                b.isEnabled = false
                target.add(b)
            }
        }
    }

    /**
     * override if special behavior is required
     *
     * @param firstAuthorId the firstAuthorId as string
     * @param firstAuthorOverridden the checkbox for firstAuthorOverridden
     * @return the first author TextField
     */
    protected open fun makeFirstAuthor(firstAuthorId: String, firstAuthorOverridden: CheckBox): TextField<String> =
        TextField(firstAuthorId)

    /**
     * override if special behavior is required
     *
     * @param authors text area for the authors field
     * @param firstAuthorOverridden checkbox for firstAuthorOverridden
     * @param firstAuthor text field for firstAuthor
     */
    protected open fun addAuthorBehavior(
        authors: TextArea<String>, firstAuthorOverridden: CheckBox,
        firstAuthor: TextField<String>,
    ) {
        // not default implementation
    }

    protected abstract fun newNavigationButton(
        id: String,
        icon: IconType,
        isEnabled: SerializableSupplier<Boolean>,
        idSupplier: SerializableSupplier<Long?>,
    ): BootstrapButton

    private fun makeAndQueueBackButton(id: String, forceRequerySupplier: SerializableSupplier<Boolean>) {
        object : BootstrapButton(id, StringResourceModel("button.back.label"), Buttons.Type.Default) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit() {
                if (java.lang.Boolean.TRUE == forceRequerySupplier.get())
                    restartSearchInPaperSearchPage()
                else if (callingPage != null && callingPage.page != null)
                    setResponsePage(callingPage.page)
            }

            override fun onConfigure() {
                super.onConfigure()
                isVisible = callingPage != null
            }
        }.apply {
            defaultFormProcessing = false
            add(AttributeModifier(TITLE_ATTR, StringResourceModel("button.back.title", this@PaperPanel, null).string))
        }.also {
            queue(it)
        }
    }

    protected abstract fun restartSearchInPaperSearchPage()
    protected abstract fun newExcludeButton(id: String): BootstrapButton
    private fun makeAndQueueSubmitButton(id: String) {
        submit = object : BootstrapButton(id, StringResourceModel(submitLinkResourceLabel), Buttons.Type.Primary) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit() {
                super.onSubmit()
                onFormSubmit()
                doOnSubmit()
            }

            /**
             * Refresh the summary link to use the updated model
             */
            override fun onAfterSubmit() {
                super.onAfterSubmit()
                summaryLink = makeSummaryLink("summary")
                form.addOrReplace(summaryLink)
                summaryShortLink = makeSummaryShortLink("summaryShort")
                form.addOrReplace(summaryShortLink)
            }

            override fun onEvent(event: IEvent<*>) {
                super.onEvent(event)
                enableButton(this, !isViewMode, event)
            }
        }.apply {
            defaultFormProcessing = true
            isEnabled = !isViewMode
            setVisible(!isViewMode)
        }.also {
            queue(it)
        }
    }

    protected abstract fun doOnSubmit()
    private fun enableButton(button: BootstrapButton, enabled: Boolean, event: IEvent<*>) {
        if (event
                .payload
                .javaClass == SelfUpdateEvent::class.java && button.isVisible) {
            button.isEnabled = enabled
            (event.payload as SelfUpdateEvent)
                .target
                .add(button)
            event.dontBroadcastDeeper()
        }
    }

    private fun makeSummaryLink(id: String): ResourceLink<Unit> {
        return makePdfResourceLink(id, summaryDataSource)
    }

    /**
     * @return [PaperSummaryDataSource]
     */
    protected abstract val summaryDataSource: PaperSummaryDataSource?
    private fun makeSummaryShortLink(id: String): ResourceLink<Unit> {
        return makePdfResourceLink(id, summaryShortDataSource)
    }

    /**
     * @return [PaperSummaryShortDataSource]
     */
    protected abstract val summaryShortDataSource: PaperSummaryShortDataSource?
    private fun makePdfResourceLink(id: String, dataSource: JasperPaperDataSource<*>?): ResourceLink<Unit> {
        val button = "button."
        val link: ResourceLink<Unit> = object : ResourceLink<Unit>(id, dataSource) {
            private val serialVersionUID: Long = 1L
            override fun onInitialize() {
                super.onInitialize()
                if (isVisible) add(ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Buttons.Size.Medium))
            }

            override fun onEvent(event: IEvent<*>) {
                super.onEvent(event)
                if (event
                        .payload
                        .javaClass == SelfUpdateEvent::class.java) {
                    if (isVisible) {
                        isEnabled = false
                        add(
                            AttributeModifier(
                                TITLE_ATTR,
                                StringResourceModel("$button$id.title.disabled", this, null).string,
                            )
                        )
                        (event.payload as SelfUpdateEvent)
                            .target
                            .add(this)
                    }
                    event.dontBroadcastDeeper()
                }
            }
        }
        link.outputMarkupId = true
        link.outputMarkupPlaceholderTag = true
        link.body = StringResourceModel("$button$id.label")
        link.isVisible = !isSearchMode
        link.add(AttributeModifier(TITLE_ATTR, StringResourceModel("$button$id.title", this, null).string))
        return link
    }

    private abstract inner class AbstractTabPanel(id: String, model: IModel<*>) : Panel(id, model) {
        private val serialVersionUID: Long = 1L
        abstract fun tabIndex(): Int
        fun queueTo(fieldType: FieldEnumType): TextArea<String> = queueTo(fieldType, false, null)

        fun queueTo(fieldType: FieldEnumType, pv: PropertyValidator<*>?) {
            queueTo(fieldType, false, pv)
        }

        fun queueNewFieldTo(fieldType: FieldEnumType) {
            queueTo(fieldType, true, null)
        }

        fun queueTo(fieldType: FieldEnumType, newField: Boolean, pv: PropertyValidator<*>?): TextArea<String> {
            val id = fieldType.fieldName
            val field = makeField(id, newField)
            field.outputMarkupId = true
            val labelModel = StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)
            queue(Label(id + LABEL_TAG, labelModel))
            field.label = labelModel
            if (newField) {
                addNewFieldSpecificAttributes(field)
            }
            if (pv != null && isEditMode) {
                field.add(pv)
            }
            addDisableBehavior(field)
            queue(field)
            return field
        }

        /**
         * The new fields are present on the page more than once, they need to be able
         * to handle the [NewFieldChangeEvent].
         */
        fun makeField(id: String, newField: Boolean): TextArea<String> =
            if (!newField) {
                TextArea(id)
            } else {
                object : TextArea<String>(id) {
                    private val serialVersionUID: Long = 1L
                    override fun onEvent(event: IEvent<*>) {
                        if (event.payload.javaClass == NewFieldChangeEvent::class.java) {
                            (event.payload as NewFieldChangeEvent).considerAddingToTarget(this)
                            event.dontBroadcastDeeper()
                        }
                    }
                }
            }

        /**
         * New fields need to broadcast the [NewFieldChangeEvent] and have a
         * special visual indication that they are a new field.
         */
        private fun addNewFieldSpecificAttributes(field: TextArea<String>) {
            field.add(AttributeAppender("class", " newField"))
            field.add(object : AjaxFormComponentUpdatingBehavior(CHANGE) {
                private val serialVersionUID: Long = 1L
                override fun onUpdate(target: AjaxRequestTarget) {
                    val id = field.id
                    val markupId = field.markupId
                    send(page, Broadcast.BREADTH, NewFieldChangeEvent(target, id, markupId))
                }
            })
        }

        override fun onConfigure() {
            super.onConfigure()
            tabIndexModel.setObject(tabIndex())
        }

        fun queueSearchOnlyTextFieldName(field: FieldEnumType) {
            val labelModel = StringResourceModel("${field.fieldName}$LABEL_RESOURCE_TAG", this, null)
            object : TextField<String>(field.fieldName) {
                private val serialVersionUID: Long = 1L
                override fun onConfigure() {
                    super.onConfigure()
                    isVisible = isSearchMode
                }
            }.apply {
                outputMarkupId = true
                label = labelModel
            }.also {
                queue(it)
                queue(object : Label("${field.fieldName}$LABEL_TAG", labelModel) {
                    private val serialVersionUID: Long = 1L
                    override fun onConfigure() {
                        super.onConfigure()
                        isVisible = isSearchMode
                    }
                })
            }
        }
    }

    private inner class TabPanel1(id: String, model: IModel<T>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 0

        override fun onInitialize() {
            super.onInitialize()
            val tab1Form: Form<T> = object : Form<T>("tab1Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab1Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab1Form)
            queueTo(PaperFields.GOALS, PropertyValidator<Any>())
            queueTo(PaperFields.POPULATION)
            queueTo(PaperFields.METHODS)
            queueNewFieldTo(PaperFields.POPULATION_PLACE)
            queueNewFieldTo(PaperFields.POPULATION_PARTICIPANTS)
            queueNewFieldTo(PaperFields.POPULATION_DURATION)
            queueNewFieldTo(PaperFields.EXPOSURE_POLLUTANT)
            queueNewFieldTo(PaperFields.EXPOSURE_ASSESSMENT)
            queueNewFieldTo(PaperFields.METHOD_STUDY_DESIGN)
            queueNewFieldTo(PaperFields.METHOD_OUTCOME)
            queueNewFieldTo(PaperFields.METHOD_STATISTICS)
            queueNewFieldTo(PaperFields.METHOD_CONFOUNDERS)
        }
    }

    private inner class TabPanel2(id: String, model: IModel<T>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 1
        override fun onInitialize() {
            super.onInitialize()
            val tab2Form: Form<T> = object : Form<T>("tab2Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab2Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab2Form)
            queueTo(PaperFields.RESULT)
            queueTo(PaperFields.COMMENT)
            queueTo(PaperFields.INTERN)
            queueNewFieldTo(PaperFields.RESULT_MEASURED_OUTCOME)
            queueNewFieldTo(PaperFields.RESULT_EXPOSURE_RANGE)
            queueNewFieldTo(PaperFields.RESULT_EFFECT_ESTIMATE)
            queueNewFieldTo(PaperFields.CONCLUSION)
        }
    }

    private inner class TabPanel3 constructor(id: String, model: IModel<T?>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 2
        override fun onInitialize() {
            super.onInitialize()
            val tab3Form: Form<T> = object : Form<T>("tab3Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab3Form.isMultiPart = true
            tab3Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab3Form)
            val codeClassModel = CodeClassModel(localization)
            val codeClasses = codeClassModel.getObject()
            makeCodeClass1Complex(codeClasses, tab3Form)
            makeCodeClassComplex(CodeClassId.CC2, codeClasses)
            makeCodeClassComplex(CodeClassId.CC3, codeClasses)
            makeCodeClassComplex(CodeClassId.CC4, codeClasses)
            makeCodeClassComplex(CodeClassId.CC5, codeClasses)
            makeCodeClassComplex(CodeClassId.CC6, codeClasses)
            makeCodeClassComplex(CodeClassId.CC7, codeClasses)
            makeCodeClassComplex(CodeClassId.CC8, codeClasses)
            queueSearchOnlyTextFieldName(PaperFields.CODES_EXCLUDED)
        }

        private fun makeCodeClass1Complex(codeClasses: List<CodeClass>, form: Form<T>) {
            val mainCodeOfCodeClass1 = TextField<String>(PaperFields.MAIN_CODE_OF_CODECLASS1.fieldName)
            val codeClass1 = makeCodeClassComplex(CodeClassId.CC1, codeClasses)
            addCodeClass1ChangeBehavior(mainCodeOfCodeClass1, codeClass1)
            addMainCodeOfClass1(mainCodeOfCodeClass1)
            if (isEditMode) form.add(CodeClass1ConsistencyValidator(codeClass1, mainCodeOfCodeClass1) as IFormValidator)
            addDisableBehavior(codeClass1)
        }

        private fun addMainCodeOfClass1(field: TextField<String>) {
            val id = field.id
            val labelModel = StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null)
            queue(Label(id + LABEL_TAG, labelModel))
            field.add(PropertyValidator<String>() as IValidator<String>)
            field.outputMarkupId = true
            field.label = labelModel
            field.isEnabled = isSearchMode
            queue(field)
        }

        private fun makeCodeClassComplex(codeClassId: CodeClassId, codeClasses: List<CodeClass?>): BootstrapMultiSelect<Code> {
            val id = codeClassId.id
            val className = codeClasses.filterNotNull()
                .filter { it.id != null && it.id == id }.map { it.name }.firstOrNull()
                ?: codeClassId.name
            queue(Label("$CODES_CLASS_BASE_NAME${id}Label", Model.of(className)))
            val model: ChainingModel<List<Code>> = object : ChainingModel<List<Code>>(this@PaperPanel.model) {
                private val serialVersionUID: Long = 1L

                @Suppress("UNCHECKED_CAST")
                val modelObject: CodeBoxAware
                    get() = (target as IModel<CodeBoxAware>).`object`

                override fun getObject(): List<Code> = modelObject.getCodesOf(codeClassId)
                override fun setObject(codes: List<Code>) {
                    modelObject.clearCodesOf(codeClassId)
                    if (codes.isNotEmpty())
                        modelObject.addCodes(codes)
                }
            }
            val choices = CodeModel(codeClassId, localization)
            val choiceRenderer: IChoiceRenderer<Code> = ChoiceRenderer(
                CoreEntity.CoreEntityFields.DISPLAY_VALUE.fieldName,
                Code.CodeFields.CODE.fieldName,
            )
            val noneSelectedModel = StringResourceModel("codes.noneSelected", this, null)
            val selectAllModel = StringResourceModel(SELECT_ALL_RESOURCE_TAG, this, null)
            val deselectAllModel = StringResourceModel(DESELECT_ALL_RESOURCE_TAG, this, null)
            val config = BootstrapSelectConfig()
                .withMultiple(true)
                .withActionsBox(choices.getObject().size > properties.multiSelectBoxActionBoxWithMoreEntriesThan)
                .withSelectAllText(selectAllModel.string)
                .withDeselectAllText(deselectAllModel.string)
                .withNoneSelectedText(noneSelectedModel.getObject())
                .withLiveSearch(true)
                .withLiveSearchStyle("contains")
            val multiSelect = BootstrapMultiSelect(CODES_CLASS_BASE_NAME + id, model, choices, choiceRenderer).with(config)
            multiSelect.add(AttributeModifier("data-width", "100%"))
            queue(multiSelect)
            addDisableBehavior(multiSelect)
            return multiSelect
        }
    }

    private inner class TabPanel4(id: String, model: IModel<T?>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 3
        override fun onInitialize() {
            super.onInitialize()
            val tab4Form: Form<T> = object : Form<T>("tab4Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab4Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab4Form)
            queueNewFieldTo(PaperFields.METHOD_STUDY_DESIGN)
            queueNewFieldTo(PaperFields.METHOD_OUTCOME)
            queueNewFieldTo(PaperFields.POPULATION_PLACE)
            queueNewFieldTo(PaperFields.POPULATION_PARTICIPANTS)
            queueNewFieldTo(PaperFields.POPULATION_DURATION)
            queueNewFieldTo(PaperFields.EXPOSURE_POLLUTANT)
            queueNewFieldTo(PaperFields.EXPOSURE_ASSESSMENT)
            queueNewFieldTo(PaperFields.METHOD_STATISTICS)
            queueNewFieldTo(PaperFields.METHOD_CONFOUNDERS)
            queueNewFieldTo(PaperFields.RESULT_MEASURED_OUTCOME)
            queueNewFieldTo(PaperFields.RESULT_EXPOSURE_RANGE)
            queueNewFieldTo(PaperFields.RESULT_EFFECT_ESTIMATE)
            queueNewFieldTo(PaperFields.CONCLUSION)
        }
    }

    private inner class TabPanel5(id: String, model: IModel<T?>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 4

        override fun onInitialize() {
            super.onInitialize()
            val tab5Form: Form<T> = object : Form<T>("tab5Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab5Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab5Form)
            originalAbstract = queueTo(PaperFields.ORIGINAL_ABSTRACT)
            addDisableBehavior(originalAbstract!!)
        }
    }

    private inner class TabPanel6(id: String, model: IModel<T?>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 5

        override fun onInitialize() {
            super.onInitialize()
            val tab6Form: Form<T> = object : Form<T>("tab6Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab6Form.outputMarkupId = true
            tab6Form.isMultiPart = true
            tab6Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab6Form)
            queue(newFileInput())
            attachments = newAttachmentTable("attachments")
            queue(attachments)

            queueSearchOnlyTextFieldName(PaperFields.ATTACHMENT_NAME_MASK)
            queueHasAttachments(PaperFields.HAS_ATTACHMENTS)
        }

        private fun queueHasAttachments(field: FieldEnumType) {
            val labelModel = StringResourceModel("${field.fieldName}$LABEL_RESOURCE_TAG", this, null)
            val checkBoxModel = PropertyModel<Boolean>(model, field.fieldName)
            object : CheckBoxX(field.fieldName, checkBoxModel) {
                private val serialVersionUID: Long = 1L
                override fun onConfigure() {
                    super.onConfigure()
                    isVisible = isSearchMode
                    add(AttributeModifier(TITLE_ATTR, checkBoxModel))
                }
            }.apply {
                config
                    .withThreeState(true)
                    .withUseNative(true)
                outputMarkupId = true
                label = labelModel
            }.also {
                queue(it)
                queue(object : Label("${field.fieldName}$LABEL_TAG", labelModel) {
                    private val serialVersionUID: Long = 1L
                    override fun onConfigure() {
                        super.onConfigure()
                        isVisible = isSearchMode
                    }
                })
            }
        }
    }

    private inner class TabPanel7(id: String, model: IModel<T?>) : AbstractTabPanel(id, model) {
        private val serialVersionUID: Long = 1L
        override fun tabIndex(): Int = 6
        override fun onInitialize() {
            super.onInitialize()
            val tab7Form: Form<T> = object : Form<T>("tab7Form", CompoundPropertyModel(model)) {
                private val serialVersionUID: Long = 1L
                override fun onSubmit() {
                    super.onSubmit()
                    onFormSubmit()
                }
            }
            tab7Form.add(SelfUpdateBroadcastingBehavior(page))
            queue(tab7Form)
            queueHeadline(PaperFields.NEWSLETTER_HEADLINE)
            makeAndQueueNewsletterTopicSelectBox("newsletterTopic")
            queueSearchOnlyTextFieldName(PaperFields.NEWSLETTER_ISSUE)
        }

        private fun queueHeadline(fieldType: FieldEnumType) {
            val id = fieldType.fieldName
            val field: TextArea<String> = object : TextArea<String>(id) {
                private val serialVersionUID: Long = 1L
                override fun onConfigure() {
                    super.onConfigure()
                    isEnabled = isSearchMode || isAssociatedWithNewsletter
                }
            }
            field.outputMarkupId = true
            val labelModel = StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)
            queue(Label(id + LABEL_TAG, labelModel))
            field.label = labelModel
            addDisableBehavior(field)
            queue(field)
        }

        private fun makeAndQueueNewsletterTopicSelectBox(id: String) {
            val model: ChainingModel<NewsletterTopic> = object : ChainingModel<NewsletterTopic>(this@PaperPanel.model) {
                private val serialVersionUID: Long = 1L

                @Suppress("UNCHECKED_CAST")
                val modelObject: NewsletterAware
                    get() = (target as IModel<NewsletterAware>).`object`
                private val topics = newsletterTopicChoice.load()
                override fun getObject(): NewsletterTopic? =
                    modelObject.newsletterTopicId?.let {
                        topics.first { it.id == modelObject.newsletterTopicId }
                    }

                override fun setObject(topic: NewsletterTopic) {
                    modelObject.setNewsletterTopic(topic)
                }
            }
            val choiceRenderer: IChoiceRenderer<NewsletterTopic> = ChoiceRenderer(
                NewsletterTopic.NewsletterTopicFields.TITLE.fieldName,
                NewsletterTopic.NewsletterTopicFields.ID.fieldName
            )
            val noneSelectedModel = StringResourceModel("$id.noneSelected", this, null)
            val config = BootstrapSelectConfig()
                .withNoneSelectedText(noneSelectedModel.getObject())
                .withLiveSearch(true)
            val topic = object : BootstrapSelect<NewsletterTopic>(id, model, newsletterTopicChoice, choiceRenderer) {
                private val serialVersionUID: Long = 1L
                override fun onConfigure() {
                    super.onConfigure()
                    isEnabled = isSearchMode || isAssociatedWithNewsletter
                }
            }.with(config)
            topic.isNullValid = true
            queue(topic)
            queue(Label(id + LABEL_TAG, StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)))
            addDisableBehavior(topic)
        }
    }

    abstract val isAssociatedWithNewsletter: Boolean
    abstract val isAssociatedWithWipNewsletter: Boolean
    abstract fun isaNewsletterInStatusWip(): Boolean
    abstract fun modifyNewsletterAssociation(target: AjaxRequestTarget)
    abstract fun newAttachmentTable(id: String): DataTable<PaperAttachment, String>
    abstract fun newFileInput(): BootstrapFileInput

    /**
     * override if needed
     *
     * @param mainCodeOfCodeClass1
     * text field with mainCode of code class1
     * @param codeClass1
     * bootstrap multi-select for the codes of code class 1
     */
    protected open fun addCodeClass1ChangeBehavior(
        mainCodeOfCodeClass1: TextField<String>,
        codeClass1: BootstrapMultiSelect<Code>,
    ) {
        // override to add code class change behavior
    }

    internal class CodeClass1ConsistencyValidator(
        codeClass1: BootstrapMultiSelect<Code>,
        mainCodeOfCodeClass1: TextField<String>,
    ) : AbstractFormValidator() {
        private val components: Array<FormComponent<*>> = arrayOf(codeClass1, mainCodeOfCodeClass1)
        override fun getDependentFormComponents(): Array<FormComponent<*>> = components

        @Suppress("UNCHECKED_CAST")
        override fun validate(form: Form<*>) {
            val codeClass1 = components[0] as BootstrapMultiSelect<Code>
            val mainCode = components[1]
            if (!codeClass1.modelObject.isEmpty() && mainCode.modelObject == null) {
                val key = resourceKey()
                error(mainCode, "$key.mainCodeOfCodeclass1Required")
            }
        }

        companion object {
            private const val serialVersionUID = 1L
        }

    }

    private fun queuePubmedRetrievalLink(linkId: String) {
        pubmedRetrieval = object : BootstrapAjaxLink<Unit>(linkId, Buttons.Type.Primary) {
            private val serialVersionUID: Long = 1L
            override fun onInitialize() {
                super.onInitialize()
                add(ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Buttons.Size.Medium))
            }

            override fun onClick(target: AjaxRequestTarget) {
                getPubmedArticleAndCompare(target)
            }

            override fun onConfigure() {
                super.onConfigure()
                isVisible = isEditMode
                if (hasPubMedId()) {
                    isEnabled = true
                    add(AttributeModifier(
                        TITLE_ATTR,
                        StringResourceModel("pubmedRetrieval.title", this, null).string,
                    ))
                } else {
                    isEnabled = false
                    add(AttributeModifier(
                        TITLE_ATTR,
                        StringResourceModel("pubmedRetrieval.title.disabled", this, null).string,
                    ))
                }
            }
        }.apply {
            outputMarkupPlaceholderTag = true
            setLabel(StringResourceModel("pubmedRetrieval.label", this, null))
        }.also {
            queue(it)
        }
    }

    protected abstract fun hasPubMedId(): Boolean

    /**
     * override to do something with the pasted content
     *
     * @param target
     * the ajax request target
     */
    protected open fun getPubmedArticleAndCompare(target: AjaxRequestTarget) {
        // no default implementation
    }

    companion object {
        private const val serialVersionUID = 1L
        protected const val TITLE_ATTR = "title"
        private const val CHANGE = "change"
    }
}
