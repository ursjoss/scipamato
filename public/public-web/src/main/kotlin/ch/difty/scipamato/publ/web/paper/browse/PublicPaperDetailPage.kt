package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.common.web.TITLE_RESOURCE_TAG
import ch.difty.scipamato.common.web.component.SerializableSupplier
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.persistence.api.PublicPaperService
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.common.BasePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType
import org.apache.wicket.AttributeModifier
import org.apache.wicket.PageReference
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.annotation.mount.MountPath

@Suppress("SameParameterValue")
@MountPath("/paper/number/\${number}")
class PublicPaperDetailPage : BasePage<PublicPaper> {

    @SpringBean
    private lateinit var publicPaperService: PublicPaperService

    private val callingPageRef: PageReference?
    private val showBackButton: Boolean

    /**
     * Loads the page with the record specified by the 'id' passed in via
     * PageParameters. If the parameter 'no' contains a valid business key number
     * instead, the page will be loaded by number.
     */
    @JvmOverloads
    constructor(parameters: PageParameters, callingPageRef: PageReference? = null, showBackButton: Boolean = true) : super(parameters) {
        this.callingPageRef = callingPageRef
        this.showBackButton = showBackButton
        parameters.tryLoadingRecordFromNumber()
    }

    internal constructor(paperModel: IModel<PublicPaper>?, callingPageRef: PageReference?, showBackButton: Boolean = true) : super(paperModel) {
        this.callingPageRef = callingPageRef
        this.showBackButton = showBackButton
    }

    private fun PageParameters.tryLoadingRecordFromNumber() {
        val number = this[PublicPageParameters.NUMBER.parameterName].toLong(0L)
        if (number > 0)
            publicPaperService.findByNumber(number)?.let { model = Model.of(it) }
        if (model == null || modelObject == null)
            warn("Page parameter ${PublicPageParameters.NUMBER.parameterName} was missing or invalid. No paper loaded.")
    }

    override fun onInitialize() {
        super.onInitialize()
        queue(Form<Void>("form"))
        val pm = paperIdManager
        queue(newNavigationButton("previous", GlyphIconType.stepbackward, { pm.hasPrevious() }) {
            pm.previous()
            pm.itemWithFocus
        })
        queue(newNavigationButton("next", GlyphIconType.stepforward, { pm.hasNext() }) {
            pm.next()
            pm.itemWithFocus
        })
        makeAndQueueBackButton("back")
        queuePubmedLink("pubmed")
        queueTopic(newLabel("caption", model))
        queueTopic(null, newField("title", "title"))
        queueTopic(newLabel("reference"), newField("authors", "authors"),
            newField("title2", "title"),
            newField("location", "location"))
        queueTopic(newLabel("goals"), newField("goals", "goals"))
        queueTopic(newLabel("population"), newField("population", "population"))
        queueTopic(newLabel("methods"), newField("methods", "methods"))
        queueTopic(newLabel("result"), newField("result", "result"))
        queueTopic(newLabel("comment"), newField("comment", "comment"))
    }

    private fun queuePubmedLink(id: String) {
        if (modelObject != null) {
            val pmId = modelObject!!.pmId
            val href: IModel<String> = Model.of(properties.pubmedBaseUrl + pmId)
            val link: BootstrapExternalLink = object : BootstrapExternalLink(id, href, Buttons.Type.Default) {
                override fun onConfigure() {
                    super.onConfigure()
                    isVisible = pmId != null
                }
            }
            link.setTarget(BootstrapExternalLink.Target.blank)
            link.setLabel(StringResourceModel("$LINK_RESOURCE_PREFIX$id$LABEL_RESOURCE_TAG", this, null))
            link.add(
                AttributeModifier(
                    AM_TITLE,
                    StringResourceModel("$LINK_RESOURCE_PREFIX$id$TITLE_RESOURCE_TAG", this, null)
                ))
            queue(link)
        } else {
            queue(object : BootstrapExternalLink(id, Model.of(""), Buttons.Type.Default) {
                override fun onConfigure() {
                    super.onConfigure()
                    isVisible = false
                }
            })
        }
    }

    private fun newNavigationButton(
        id: String, icon: GlyphIconType, isEnabled: SerializableSupplier<Boolean>,
        idSupplier: SerializableSupplier<Long?>,
    ): BootstrapButton = object : BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
        override fun onSubmit() {
            val number = idSupplier.get()
            if (number != null) {
                val pp = pageParameters
                pp[PublicPageParameters.NUMBER.parameterName] = number
                setResponsePage(PublicPaperDetailPage(pp, callingPageRef))
            }
        }

        override fun onConfigure() {
            super.onConfigure()
            setEnabled(isEnabled.get())
        }
    }.apply<BootstrapButton> {
        defaultFormProcessing = false
        setIconType(icon)
        add(AttributeModifier(
            AM_TITLE,
            StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$TITLE_RESOURCE_TAG", this, null)
        ))
        setType(Buttons.Type.Primary)
    }

    private fun makeAndQueueBackButton(id: String) {
        object : BootstrapButton(id, StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$LABEL_RESOURCE_TAG"), Buttons.Type.Default) {
            override fun onSubmit() {
                if (callingPageRef != null) setResponsePage(callingPageRef.page) else setResponsePage(PublicPage::class.java)
            }
        }.apply {
            isVisible = showBackButton
            defaultFormProcessing = false
            add(AttributeModifier(
                AM_TITLE,
                StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$TITLE_RESOURCE_TAG", this@PublicPaperDetailPage, null)
            ))
        }.also {
            queue(it)
        }
    }

    private fun queueTopic(label: Label?, vararg fields: Label) {
        var hasValues = fields.isEmpty()
        fields.filter { it.defaultModelObject != null }
            .forEach { _ -> hasValues = true }
        for (f in fields) {
            f.isVisible = hasValues
            queue(f)
        }
        if (label != null) {
            label.isVisible = hasValues
            queue(label)
        }
    }

    private fun newLabel(idPart: String, parameterModel: IModel<*>? = null): Label =
        Label(
            "$idPart$LABEL_TAG",
            StringResourceModel("$idPart$LABEL_RESOURCE_TAG", this, parameterModel).string + if (parameterModel == null) ":" else ""
        )

    private fun newField(id: String, property: String): Label = Label(id, PropertyModel<Any>(model, property))

    companion object {
        private const val serialVersionUID = 1L
        private const val LINK_RESOURCE_PREFIX = "link."
        private const val BUTTON_RESOURCE_PREFIX = "button."
        private const val AM_TITLE = "title"
    }
}
