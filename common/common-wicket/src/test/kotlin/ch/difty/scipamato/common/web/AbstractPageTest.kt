package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.head.filter.HeaderResponseContainer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.settings.DebugSettings
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class AbstractPageTest : WicketBaseTest() {

    @MockkBean
    private lateinit var dateTimeServiceMock: DateTimeService

    private lateinit var page: AbstractPage<TestRecord>

    override fun setUpHook() {
        page = object : AbstractPage<TestRecord>(Model.of(TestRecord(1, "foo"))) {
            override val properties: ApplicationProperties
                get() = TestApplicationProperties()
        }
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(dateTimeServiceMock)
    }

    @Test
    fun test_withModelConstructor() {
        tester.startPage(page)
        tester.assertRenderedPage(AbstractPage::class.java)
    }

    @Test
    fun canGetDateTimeService() {
        page.dateTimeService shouldBeInstanceOf DateTimeService::class
    }

    @Test
    fun hasNavbarVisibleByDefault() {
        page.isNavbarVisible.shouldBeTrue()
    }

    @Test
    fun test_withPageParametersConstructor() {
        page = object : AbstractPage<TestRecord>(PageParameters()) {
            override val properties: ApplicationProperties
                get() = TestApplicationProperties()
            override val isNavbarVisible: Boolean
                get() = true
        }

        tester.startPage(page)
        tester.assertRenderedPage(AbstractPage::class.java)
    }

    @Test
    fun assertPage() {
        tester.startPage(TestAbstractPage(Model.of(TestRecord(1, "foo"))))
        tester.assertRenderedPage(TestAbstractPage::class.java)
        assertComponents()
    }

    private fun assertComponents() {
        tester.assertComponent("_header_", HtmlHeaderContainer::class.java)
        tester.assertLabel("_header_:pageTitle", "SciPaMaTo")

        tester.assertComponent("navbar", Navbar::class.java)
        tester.assertComponent("navbar:container:collapse:extraItems", RepeatingView::class.java)

        tester.assertLabel("navbar:container:collapseButton:toggleNavigationLabel", "Toggle Navigation")
        tester.assertLabel("navbar:container:brandName:brandLabel", "SciPaMaTo")

        tester.assertComponent("feedback", NotificationPanel::class.java)
        tester.assertComponent(AbstractPage.FOOTER_CONTAINER, HeaderResponseContainer::class.java)

        tester.assertInvisible("debug")

        tester.assertComponent("form", Form::class.java)
        tester.assertLabel("form:nameLabel", "Name")
        tester.assertComponent("form:name", TextField::class.java)
        tester.assertModelValue("form:name", "foo")
    }

    @Test
    fun gettingBrandName_withNullBrand_retrievesBrandFromProperties() {
        page.getBrandName(null) shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun gettingBrandName_withEmptyBrand_retrievesBrandFromProperties() {
        page.getBrandName("") shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun gettingBrandName_withNaBrand_retrievesBrandFromProperties() {
        page.getBrandName("n.a.") shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun gettingBrandName_withExplicitBrand_usesThat() {
        page.getBrandName("foo") shouldBeEqualTo "foo"
    }

    @Test
    fun queueingFieldAndLabel_withPropertyValidator_addsItToField() {
        val field = mockk<TextField<String>>(relaxed = true) {
            every { id } returns "testField"
        }
        val pv = mockk<PropertyValidator<TextField<String>>>()

        page.queueFieldAndLabel(field, pv)

        verify(exactly = 8) { field.id }
        verify { field.label = any<IModel<String>>() }
        verify { field.add(pv) }
        verify { field.isVisible }
        confirmVerified(field)
    }

    @Test
    fun queueingFieldAndLabel_withNoPropertyValidator_dowNotAddToField() {
        val field = mockk<TextField<String>>(relaxed = true) {
            every { id } returns "testField"
        }

        page.queueFieldAndLabel(field, null)

        verify(exactly = 8) { field.id }
        verify { field.label = any<IModel<String>>() }
        verify { field.isVisible }
        confirmVerified(field)
    }

    @Test
    fun submitting() {
        tester.startPage(TestAbstractPage(Model.of(TestRecord(1, "foo"))))
        tester.assertRenderedPage(TestAbstractPage::class.java)

        tester.assertModelValue("form:name", "foo")

        tester.executeAjaxEvent("form:respPageButton", "click")

        tester.assertRenderedPage(TestAbstractPage::class.java)
        tester.assertModelValue("form:name", "bar")
    }

    @Test
    fun withDebugEnabled() {
        val debugSettings = mockk<DebugSettings> {
            every { isDevelopmentUtilitiesEnabled } returns true
        }

        page = object : AbstractPage<TestRecord>(Model.of(TestRecord(1, "foo"))) {
            override val properties: ApplicationProperties
                get() = TestApplicationProperties()
            override val debugSettings: DebugSettings
                get() = debugSettings
        }

        tester.startPage(page)
        tester.assertRenderedPage(AbstractPage::class.java)

        verify { debugSettings.isDevelopmentUtilitiesEnabled }

        page.feedbackPanel shouldBeInstanceOf NotificationPanel::class
    }
}
