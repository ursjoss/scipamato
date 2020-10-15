package ch.difty.scipamato.publ.web.authentication

import ch.difty.scipamato.common.web.pages.login.AbstractLogoutPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.wicketstuff.annotation.mount.MountPath

@MountPath("logout")
class LogoutPage(
    parameters: PageParameters?,
) : AbstractLogoutPage<LoginPage>(parameters) {

    override val responsePage: LoginPage
        get() = LoginPage(PageParameters())

    companion object {
        private const val serialVersionUID = 1L
    }
}
