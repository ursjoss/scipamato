package ch.difty.scipamato.common.web.pages.login

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.common.web.WicketBaseTest
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel
import org.apache.wicket.markup.html.form.StatelessForm
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

internal class AbstractLogoutPageTest : WicketBaseTest() {

    private lateinit var page: AbstractLogoutPage<*>

    @MockkBean(relaxed = true)
    private lateinit var applicationProperties: ApplicationProperties

    @MockkBean
    private lateinit var dateTimeService: DateTimeService

    override fun setUpHook() {
        page = TestLogoutPage(PageParameters())
    }

    @Test
    fun assertPage() {
        tester.startPage(page)
        tester.assertRenderedPage(AbstractLogoutPage::class.java)

        tester.assertInvisible("navbar")
        tester.assertComponent("feedback", NotificationPanel::class.java)
        tester.assertComponent("form", StatelessForm::class.java)
    }

    @Test
    fun submit_withResponsePage() {
        val parameters = PageParameters()
        val page2 = object : TestLogoutPage(parameters) {
            override val responsePage: AbstractPage<*>?
                get() = TestLoginPage(parameters)
        }
        tester.startPage(page2)
        tester.assertRenderedPage(AbstractLogoutPage::class.java)
        tester.submitForm("form")

        tester.assertRenderedPage(TestLoginPage::class.java)
        tester.assertNoErrorMessage()
    }

    @Test
    fun submitting_withoutLogoutData_rendersErrorMessages() {
        tester.startPage(page)
        tester.assertRenderedPage(AbstractLogoutPage::class.java)
        tester.submitForm("form")

        tester.assertRenderedPage(AbstractLogoutPage::class.java)

        tester.assertNoErrorMessage()
    }
}
