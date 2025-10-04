package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.common.web.TITLE_RESOURCE_TAG
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.persistence.api.PublicPaperService
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.common.BasePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconType
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
import kotlin.reflect.KProperty1

@Suppress("SameParameterValue", "TooManyFunctions")
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
     *
     * @JvmOverloads required for using the path via mount path
     */
    @JvmOverloads
    constructor(
        parameters: PageParameters,
        callingPageRef: PageReference? = null,
        showBackButton: Boolean = true,
    ) : super(parameters) {
        this.callingPageRef = callingPageRef
        this.showBackButton = showBackButton
        parameters.tryLoadingRecordFromNumber()
    }

    internal constructor(
        paperModel: IModel<PublicPaper>?,
        callingPageRef: PageReference?,
        showBackButton: Boolean = true,
    ) : super(paperModel) {
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

        queue(Form<Unit>("form"))

        queue(
            newNavigationButton(
                "previous",
                FontAwesome7IconType.backward_step_s,
                @JvmSerializableLambda { paperIdManager.hasPrevious() },
                @JvmSerializableLambda {
                    paperIdManager.previous()
                    paperIdManager.itemWithFocus
                },
            )
        )
        queue(
            newNavigationButton(
                "next",
                FontAwesome7IconType.forward_step_s,
                @JvmSerializableLambda { paperIdManager.hasNext() },
                @JvmSerializableLambda {
                    paperIdManager.next()
                    paperIdManager.itemWithFocus
                }
            ))
        queue(newBackButton("back"))
        queue(newPubmedLink("pubmed"))

        queueTopic(newLabel("caption", model))
        PublicPaper::title.asQueuedTopic(label = null)
        queueTopic(newLabel("reference"),
            PublicPaper::authors.asReadOnlyField(),
            PublicPaper::title.asReadOnlyField(id = "title2"),
            PublicPaper::location.asReadOnlyField()
        )
        PublicPaper::goals.asQueuedTopic()
        PublicPaper::population.asQueuedTopic()
        PublicPaper::methods.asQueuedTopic()
        PublicPaper::result.asQueuedTopic()
        PublicPaper::comment.asQueuedTopic()
    }

    private fun <V> KProperty1<PublicPaper, V>.asQueuedTopic(label: String? = name) {
        queueTopic(label?.let { newLabel(it) }, asReadOnlyField())
    }

    private fun newNavigationButton(
        id: String,
        icon: IconType,
        getEnabled: () -> Boolean,
        getId: () -> Long?,
    ) = object : BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
        private val serialVersionUID: Long = 1
        override fun onSubmit() {
            getId()?.let {
                pageParameters[PublicPageParameters.NUMBER.parameterName] = it
                setResponsePage(PublicPaperDetailPage(pageParameters, callingPageRef, showBackButton))
            }
        }

        override fun onConfigure() {
            super.onConfigure()
            isEnabled = getEnabled()
        }
    }.apply<BootstrapButton> {
        defaultFormProcessing = false
        setIconType(icon)
        add(AttributeModifier(AM_TITLE, id.toTitleResourceModel(BUTTON_RESOURCE_PREFIX)))
        setType(Buttons.Type.Primary)
    }

    private fun newBackButton(id: String) = object : BootstrapButton(
        id,
        StringResourceModel("$BUTTON_RESOURCE_PREFIX$id$LABEL_RESOURCE_TAG"),
        Buttons.Type.Default,
    ) {
        private val serialVersionUID: Long = 1
        override fun onSubmit() {
            callingPageRef?.let { pr ->
                setResponsePage(pr.page)
            } ?: setResponsePage(PublicPage::class.java)
        }
    }.apply {
        isVisible = showBackButton
        defaultFormProcessing = false
        add(AttributeModifier(AM_TITLE, id.toTitleResourceModel(BUTTON_RESOURCE_PREFIX)))
    }

    private fun newPubmedLink(id: String): BootstrapExternalLink = if (modelObject != null) {
        val pmId = modelObject.pmId
        newExternalLink(id, href = "${properties.pubmedBaseUrl}$pmId") @JvmSerializableLambda { pmId != null }.apply {
            setTarget(BootstrapExternalLink.Target.blank)
            setLabel(id.toLabelResourceModel(LINK_RESOURCE_PREFIX))
            add(AttributeModifier(AM_TITLE, id.toTitleResourceModel(LINK_RESOURCE_PREFIX)))
        }
    } else newExternalLink(id)

    private fun newExternalLink(
        id: String,
        href: String = "",
        getVisibility: () -> Boolean = @JvmSerializableLambda { false },
    ) =
        object : BootstrapExternalLink(id, Model.of(href), Buttons.Type.Default) {
            private val serialVersionUID: Long = 1
            override fun onConfigure() {
                super.onConfigure()
                isVisible = getVisibility()
            }
        }

    private fun queueTopic(label: Label?, vararg fields: Label) {
        fun Label.setVisibleAndQueue(visible: Boolean) {
            isVisible = visible
            this@PublicPaperDetailPage.queue(this)
        }

        val show = fields.isEmpty() || fields.mapNotNull { it.defaultModelObject }.isNotEmpty()
        for (f in fields) f.setVisibleAndQueue(show)
        label?.setVisibleAndQueue(show)
    }

    private fun newLabel(idPart: String, parameterModel: IModel<*>? = null) = Label(
        "$idPart$LABEL_TAG",
        StringResourceModel("$idPart$LABEL_RESOURCE_TAG", this@PublicPaperDetailPage, parameterModel).string +
            if (parameterModel == null) ":" else "",
    )

    private fun <V> KProperty1<PublicPaper, V>.asReadOnlyField(id: String = name) =
        Label(id, PropertyModel<Any>(this@PublicPaperDetailPage.model, name))


    private fun String.toLabelResourceModel(prefix: String) = newStringResourceModel(this, prefix, LABEL_RESOURCE_TAG)
    private fun String.toTitleResourceModel(prefix: String) = newStringResourceModel(this, prefix, TITLE_RESOURCE_TAG)
    private fun newStringResourceModel(id: String, prefix: String, tag: String) = StringResourceModel(
        "$prefix$id$tag", this@PublicPaperDetailPage, null
    )

    companion object {
        private const val serialVersionUID = 1L
        private const val LINK_RESOURCE_PREFIX = "link."
        private const val BUTTON_RESOURCE_PREFIX = "button."
        private const val AM_TITLE = "title"
    }
}
