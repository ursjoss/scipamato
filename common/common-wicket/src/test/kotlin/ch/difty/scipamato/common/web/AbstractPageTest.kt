package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.boot.test.mock.mockito.MockBean

internal class AbstractPageTest : WicketBaseTest() {

    @MockBean
    private lateinit var dateTimeServiceMock: DateTimeService

    private lateinit var page: AbstractPage<TestRecord>

    override fun setUpHook() {
        page = object : AbstractPage<TestRecord>(Model.of(TestRecord(1, "foo"))) {
            override fun getProperties(): ApplicationProperties = TestApplicationProperties()
        }
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(dateTimeServiceMock)
    }

    @Test
    fun test_withModelConstructor() {
        tester.startPage(page)
        tester.assertRenderedPage(AbstractPage::class.java)
    }

    @Test
    fun canGetDateTimeService() {
        assertThat(page.dateTimeService).isInstanceOf(DateTimeService::class.java)
    }

    @Test
    fun hasNavbarVisibleByDefault() {
        assertThat(page.isNavbarVisible).isTrue()
    }

    @Test
    fun gettingNavbarAndFeedbackPanelBeforeInitialize_returnsNull() {
        assertThat(page.navBar == null).isTrue()
        assertThat(page.feedbackPanel == null).isTrue()
    }

    @Test
    fun test_withPageParametersConstructor() {
        page = object : AbstractPage<TestRecord>(PageParameters()) {
            override fun getProperties(): ApplicationProperties = TestApplicationProperties()
            override fun isNavbarVisible(): Boolean = true
        }

        tester.startPage<AbstractPage<TestRecord>>(page)
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
        tester.assertComponent("footer-container", HeaderResponseContainer::class.java)

        tester.assertInvisible("debug")

        tester.assertComponent("form", Form::class.java)
        tester.assertLabel("form:nameLabel", "Name")
        tester.assertComponent("form:name", TextField::class.java)
        tester.assertModelValue("form:name", "foo")
    }

    @Test
    fun gettingBrandName_withNullBrand_retrievesBrandFromProperties() {
        assertThat(page.getBrandName(null)).isEqualTo("SciPaMaTo")
    }

    @Test
    fun gettingBrandName_withEmptyBrand_retrievesBrandFromProperties() {
        assertThat(page.getBrandName("")).isEqualTo("SciPaMaTo")
    }

    @Test
    fun gettingBrandName_withNaBrand_retrievesBrandFromProperties() {
        assertThat(page.getBrandName("n.a.")).isEqualTo("SciPaMaTo")
    }

    @Test
    fun gettingBrandName_withExplicitBrand_usesThat() {
        assertThat(page.getBrandName("foo")).isEqualTo("foo")
    }

    @Test
    fun queueingFieldAndLabel_withPropertyValidator_addsItToField() {
        val field = Mockito.mock(TextField::class.java)
        `when`(field.id).thenReturn("testField")
        val pv = Mockito.mock(PropertyValidator::class.java)

        page.queueFieldAndLabel(field, pv)

        verify(field, times(8)).id
        verify(field).label = any<IModel<String>>()
        verify(field).add(pv)
        verify(field).isVisible
        verifyNoMoreInteractions(field)
    }

    @Test
    fun queueingFieldAndLabel_withNoPropertyValidator_dowNotAddToField() {
        val field = Mockito.mock(TextField::class.java)
        whenever(field.id).thenReturn("testField")

        page.queueFieldAndLabel(field, null)

        verify(field, times(8)).id
        verify(field).label = any<IModel<String>>()
        verify(field).isVisible
        verifyNoMoreInteractions(field)
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
        val debugSettings = Mockito.mock(DebugSettings::class.java)
        whenever(debugSettings.isDevelopmentUtilitiesEnabled).thenReturn(true)

        page = object : AbstractPage<TestRecord>(Model.of(TestRecord(1, "foo"))) {
            override fun getProperties(): ApplicationProperties = TestApplicationProperties()
            override fun getDebugSettings(): DebugSettings = debugSettings
        }

        tester.startPage(page)
        tester.assertRenderedPage(AbstractPage::class.java)

        verify(debugSettings).isDevelopmentUtilitiesEnabled
    }
}
