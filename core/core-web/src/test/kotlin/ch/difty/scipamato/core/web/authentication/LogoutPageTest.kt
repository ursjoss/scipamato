package ch.difty.scipamato.core.web.authentication

import ch.difty.scipamato.core.web.WicketTest
import io.mockk.unmockkAll
import org.amshove.kluent.shouldBeFalse
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class LogoutPageTest : WicketTest() {

    @AfterEach
    fun tearDown() {
        tester.destroy()
        unmockkAll()
    }

    @Test
    fun assertPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        assertSpecificComponents()
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    private fun makePage(): LogoutPage = LogoutPage(PageParameters())

    private val pageClass: Class<LogoutPage>
        get() = LogoutPage::class.java

    private fun assertSpecificComponents() {
        tester.assertComponent(FORM, Form::class.java)
    }

    @Test
    @Disabled("TODO restore")
    fun submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        tester.startPage(makePage())
        tester.session.isSessionInvalidated.shouldBeFalse()
        tester.submitForm(FORM)
        tester.assertRenderedPage(LoginPage::class.java)
    }

    companion object {
        private const val FORM = "form"
    }
}
