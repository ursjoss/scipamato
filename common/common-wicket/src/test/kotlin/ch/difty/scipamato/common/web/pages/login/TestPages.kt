package ch.difty.scipamato.common.web.pages.login

import ch.difty.scipamato.common.web.AbstractPage
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage
import org.apache.wicket.request.mapper.parameter.PageParameters

@WicketSignInPage
class TestLoginPage(parameters: PageParameters) : AbstractLoginPage<TestLoginPage>(parameters) {
    override val responsePage: TestLoginPage?
        get() = TestLoginPage(pageParameters)
}

open class TestLogoutPage(parameters: PageParameters) : AbstractLogoutPage<AbstractPage<*>>(parameters) {
    override val responsePage: AbstractPage<*>?
        get() = null
}
