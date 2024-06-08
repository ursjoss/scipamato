package ch.difty.scipamato.common.web.pages.login

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.AbstractPage
import org.apache.wicket.markup.html.form.StatelessForm
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.annotation.mount.MountPath

@MountPath("logout")
abstract class AbstractLogoutPage<R : AbstractPage<*>>(
    parameters: PageParameters?,
) : AbstractPage<Unit>(parameters) {

    @SpringBean
    private lateinit var scipamatoProperties: ApplicationProperties

    override val properties: ApplicationProperties get() = scipamatoProperties
    override val isNavbarVisible: Boolean get() = false

    protected abstract val responsePage: R?

    override fun onInitialize() {
        super.onInitialize()
        queue(newForm("form"))
    }

    @Suppress("SameParameterValue")
    private fun newForm(id: String): StatelessForm<Unit> = object : StatelessForm<Unit>(id) {
        private val serialVersionUID: Long = 1L
        override fun onSubmit() {
            signOutAndInvalidate()
            responsePage?.let { setResponsePage(it) }
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
