package ch.difty.scipamato.common.web.pages.login

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.extensions.markup.html.bootstrap.spinner.SpinnerBehavior
import lombok.extern.slf4j.Slf4j
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.form.RequiredTextField
import org.apache.wicket.markup.html.form.StatelessForm
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean

private val log = logger()

/**
 * @param <R>type of the response page
 */
@Suppress("SameParameterValue")
@Slf4j
abstract class AbstractLoginPage<R : AbstractPage<*>>(
    parameters: PageParameters?,
) : AbstractPage<Void>(parameters) {

    @SpringBean
    private lateinit var scipamatoProperties: ApplicationProperties

    private val username: String? = null
    private val password: String? = null

    override val properties: ApplicationProperties get() = scipamatoProperties
    override val isNavbarVisible: Boolean get() = false
    protected abstract val responsePage: R?

    override fun onInitialize() {
        super.onInitialize()
        if (isSignedIn) continueToOriginalDestination()
        queue(newLoginForm("form"))
    }

    private fun newLoginForm(id: String): StatelessForm<Void> = object : StatelessForm<Void>(id) {
        override fun onSubmit() {
            if (signIn(username, password)) {
                log.info("User '$username' logged in successfully")
                setResponsePage(responsePage)
            } else {
                log.warn("Unsuccessful login attempt by user '$username'.")
                error(getString("msg.login.failure"))
            }
        }
    }.apply<StatelessForm<Void>> {
        defaultModel = CompoundPropertyModel(this@AbstractLoginPage)
    }.also {
        queue(newHeader("header"))
        queueFieldAndLabel(RequiredTextField<String>("username"))
        queueFieldAndLabel(PasswordTextField("password"))
        queue(newButton("signin"))
    }

    private fun newHeader(id: String): Label = Label(id, StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null))

    private fun newButton(id: String): BootstrapButton = BootstrapButton(
        id,
        StringResourceModel("$id.value", this, null),
        Buttons.Type.Primary
    ).apply {
        add(SpinnerBehavior())
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
