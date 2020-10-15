package ch.difty.scipamato.publ.web.authentication

import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage
import ch.difty.scipamato.publ.web.paper.browse.PublicPage
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.wicketstuff.annotation.mount.MountPath

@MountPath("login")
@WicketSignInPage
class LoginPage(
    parameters: PageParameters?,
) : AbstractLoginPage<PublicPage>(parameters) {

    override val responsePage: PublicPage
        get() = PublicPage(pageParameters)

    companion object {
        private const val serialVersionUID = 1L
    }
}
