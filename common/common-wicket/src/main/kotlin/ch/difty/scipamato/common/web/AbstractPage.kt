package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.component.SerializableSupplier
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.devutils.debugbar.DebugBar
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.MetaDataHeaderItem
import org.apache.wicket.markup.head.PriorityHeaderItem
import org.apache.wicket.markup.head.filter.HeaderResponseContainer
import org.apache.wicket.markup.html.GenericWebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.panel.EmptyPanel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.settings.DebugSettings
import org.apache.wicket.spring.injection.annot.SpringBean

@Suppress("SameParameterValue")
abstract class AbstractPage<T> : GenericWebPage<T> {

    @SpringBean
    lateinit var dateTimeService: DateTimeService
        private set
    lateinit var feedbackPanel: NotificationPanel
        private set
    lateinit var navBar: Navbar
        private set

    open val debugSettings: DebugSettings
        get() = application.debugSettings

    /**
     * Override if you do not want to show the navbar or only conditionally.
     * @return whether to show the Navbar or not.
     */
    open val isNavbarVisible: Boolean
        get() = true

    protected val isSignedIn: Boolean
        get() = AuthenticatedWebSession.get().isSignedIn

    protected abstract val properties: ApplicationProperties

    private val title: IModel<String>
        get() = Model.of(properties.titleOrBrand)

    constructor(parameters: PageParameters?) : super(parameters)
    constructor(model: IModel<T>?) : super(model)

    override fun renderHead(response: IHeaderResponse) {
        response.apply {
            super.renderHead(this)
            render(PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("charset", "utf-8")))
            render(PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("X-UA-Compatible", "IE=edge")))
            render(PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("viewport", "width=device-width, initial-scale=1")))
            render(PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("Content-Type", "text/html; charset=UTF-8")))
        }
    }

    override fun onInitialize() {
        super.onInitialize()
        createAndAddTitle("pageTitle")
        createAndAddNavBar("navbar")
        createAndAddFeedbackPanel("feedback")
        createAndAddDebugBar("debug")
        createAndAddFooterContainer(FOOTER_CONTAINER)
    }

    private fun createAndAddTitle(id: String) {
        queue(Label(id, title))
    }

    private fun createAndAddNavBar(id: String) {
        navBar = newNavbar(id).also {
            queue(it)
        }
        extendNavBar()
    }

    /**
     * Override if you need to extend the [Navbar]
     */
    private fun extendNavBar() {
        // no default implementation
    }

    private fun createAndAddFeedbackPanel(label: String) {
        feedbackPanel = NotificationPanel(label).apply {
            outputMarkupId = true
        }.also {
            queue(it)
        }
    }

    private fun createAndAddDebugBar(label: String) {
        if (debugSettings.isDevelopmentUtilitiesEnabled)
            queue(DebugBar(label).positionBottom())
        else queue(EmptyPanel(label).setVisible(false))
    }

    private fun createAndAddFooterContainer(id: String) {
        queue(HeaderResponseContainer(id, id))
    }

    private fun newNavbar(markupId: String): Navbar =
        object : Navbar(markupId) {
            override fun onConfigure() {
                super.onConfigure()
                isVisible = isNavbarVisible
            }
        }.apply {
            fluid()
            position = Navbar.Position.STATIC_TOP
            setBrandName(Model.of(getBrandName(properties.brand)))
            setInverted(true)
        }.also {
            addLinksTo(it)
        }

    fun getBrandName(brand: String?): String =
        if (brand == null || brand.isBlank() || "n.a." == brand)
            StringResourceModel("brandname", this, null).string
        else brand

    protected open fun addLinksTo(nb: Navbar) {}

    @JvmOverloads
    fun queueFieldAndLabel(field: FormComponent<*>, pv: PropertyValidator<*>? = null) {
        val id = field.id
        val labelModel = StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)
        val label = Label(id + LABEL_TAG, labelModel)
        queue(label)
        field.label = labelModel
        queue(field)
        pv?.let { field.add(it) }
        label.isVisible = field.isVisible
    }

    protected fun newResponsePageButton(
        id: String,
        responsePage: SerializableSupplier<AbstractPage<*>?>,
    ): BootstrapAjaxButton =
        object : BootstrapAjaxButton(
            id,
            StringResourceModel(id + LABEL_RESOURCE_TAG, this@AbstractPage, null),
            Buttons.Type.Default
        ) {
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                setResponsePage(responsePage.get())
            }

            override fun onConfigure() {
                super.onConfigure()
                isEnabled = setResponsePageButtonEnabled()
            }
        }

    /**
     * Controls the enabled status of the response page button. Override if needed.
     *
     * @return true if response page button shall be enabled (default). false otherwise.
     */
    protected open fun setResponsePageButtonEnabled(): Boolean = true

    protected fun queuePanelHeadingFor(id: String) {
        queue(
            Label(
                id + LABEL_TAG,
                StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null)
            )
        )
    }

    protected fun signIn(username: String?, password: String?): Boolean =
        AuthenticatedWebSession.get().signIn(username, password)

    protected fun signOutAndInvalidate() {
        AuthenticatedWebSession.get().invalidate()
    }

    companion object {
        private const val serialVersionUID = 1L
        const val FOOTER_CONTAINER = "footer-container"
    }
}
