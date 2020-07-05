package ch.difty.scipamato.common.web.pages.login

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.PASSWORD
import ch.difty.scipamato.common.web.USERNAME
import ch.difty.scipamato.common.web.WicketBaseTest
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.form.RequiredTextField
import org.apache.wicket.markup.html.form.StatelessForm
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

internal class AbstractLoginPageTest : WicketBaseTest() {

    private lateinit var page: AbstractLoginPage<*>

    @MockkBean(relaxed = true)
    private lateinit var applicationProperties: ApplicationProperties

    @MockkBean
    private lateinit var dateTimeService: DateTimeService

    override fun setUpHook() {
        page = TestLoginPage(PageParameters())
    }

    @Test
    fun assertPage() {
        tester.startPage(page)
        tester.assertRenderedPage(AbstractLoginPage::class.java)

        tester.assertInvisible("navbar")
        tester.assertComponent("feedback", NotificationPanel::class.java)
        tester.assertComponent("form", StatelessForm::class.java)
        tester.assertLabel("form:usernameLabel", "Username")
        tester.assertComponent("form:username", RequiredTextField::class.java)
        tester.assertLabel("form:passwordLabel", "Password")
        tester.assertComponent("form:password", PasswordTextField::class.java)
        tester.assertLabel("form:signin:label", "Sign in")
    }

    @Test
    fun submitting_withoutLoginData_rendersErrorMessages() {
        tester.startPage(page)
        tester.assertRenderedPage(AbstractLoginPage::class.java)
        tester.submitForm("form")

        tester.assertRenderedPage(AbstractLoginPage::class.java)

        tester.assertErrorMessages("'Username' is required.", "'Password' is required.")
    }

    @Test
    fun submitting_withInvalidCredentials_doesNotLoginAndRendersErrorMessage() {
        tester.startPage(page)
        tester.assertRenderedPage(TestLoginPage::class.java)

        val formTester = tester.newFormTester("form")
        formTester.setValue("username", USERNAME)
        formTester.setValue("password", "Invalid")

        formTester.submit()

        tester.assertErrorMessages("The username and/or password were invalid.")
    }

    @Test
    fun submitting_withValidCredentials_doesLoginAndForwardsToTestHomePage() {
        tester.startPage(page)
        tester.assertRenderedPage(TestLoginPage::class.java)

        val formTester = tester.newFormTester("form")
        formTester.setValue("username", USERNAME)
        formTester.setValue("password", PASSWORD)

        formTester.submit()

        tester.assertNoErrorMessage()
    }
}
