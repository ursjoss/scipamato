package ch.difty.scipamato.publ.web.authentication

import ch.difty.scipamato.publ.web.WicketTest
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

class LoginPageTest : WicketTest() {

    @Test
    fun assertPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        assertSpecificComponents()
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    private fun makePage(): LoginPage = LoginPage(PageParameters())

    private val pageClass: Class<LoginPage>
        get() = LoginPage::class.java

    private fun assertSpecificComponents() {
        val b = "form"
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b, "username")
        assertLabeledTextField(b, "password")
        tester.assertComponent("$b:signin", BootstrapButton::class.java)
        assertMenuEntries()
    }

    @Test
    fun whenSigningIn_withWrongCredentials_providesErrorMessage() {
        val session = tester.session as SecureWebSession
        session.signOut()
        tester.startPage(LoginPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("username", "testuser")
        formTester.setValue("password", "wrongpw")
        formTester.submit()
        tester.assertErrorMessages("The username and/or password were invalid.")
        tester.assertRenderedPage(pageClass)
    }

    private fun assertMenuEntries() {
        tester.assertComponent("_header_", HtmlHeaderContainer::class.java)
        tester.assertInvisible("navbar")
    }
}
