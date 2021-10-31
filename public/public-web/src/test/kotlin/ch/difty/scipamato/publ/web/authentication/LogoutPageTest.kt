package ch.difty.scipamato.publ.web.authentication

import ch.difty.scipamato.publ.web.WicketTest
import ch.difty.scipamato.publ.web.paper.browse.PublicPage
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import org.amshove.kluent.shouldBeFalse
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

class LogoutPageTest : WicketTest() {

    override fun setUpHook() {
        super.setUpHook()
        login(USERNAME, PASSWORD)
    }

    protected fun login(username: String?, password: String?) {
        val session = tester.session as SecureWebSession
        session.signOut()
        tester.startPage(LoginPage::class.java)
        val formTester = tester.newFormTester(FORM)
        formTester.setValue("username", username)
        formTester.setValue("password", password)
        formTester.submit()
        tester.assertNoErrorMessage()
        tester.assertRenderedPage(PublicPage::class.java)
    }

    @Test
    fun assertPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        assertSpecificComponents()
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    protected fun makePage(): LogoutPage = LogoutPage(PageParameters())

    protected val pageClass: Class<LogoutPage>
        get() = LogoutPage::class.java

    protected fun assertSpecificComponents() {
        tester.assertComponent(FORM, Form::class.java)
    }

    @Test
    fun submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        tester.startPage(makePage())
        tester.session.isSessionInvalidated.shouldBeFalse()
        tester.submitForm(FORM)
        tester.assertRenderedPage(LoginPage::class.java)
    }

    companion object {
        private const val FORM = "form"
        private const val USERNAME = "admin"
        private const val PASSWORD = "admin"
    }
}
