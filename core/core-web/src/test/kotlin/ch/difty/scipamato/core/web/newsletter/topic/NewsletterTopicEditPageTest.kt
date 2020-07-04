@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.newsletter.topic

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.authentication.LogoutPage
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.repeater.RefreshingView
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException

@Suppress("PrivatePropertyName")
internal class NewsletterTopicEditPageTest : BasePageTest<NewsletterTopicEditPage>() {

    private val ntt_de = NewsletterTopicTranslation(1, "de", "thema1", 1)
    private val ntt_en = NewsletterTopicTranslation(2, "en", "topic1", 1)
    private val ntt_fr = NewsletterTopicTranslation(3, "fr", "sujet1", 1)
    private val ntd = NewsletterTopicDefinition(1, "de", 1, ntt_de, ntt_en, ntt_fr)

    private lateinit var newsletterTopicDefinitionDummy: NewsletterTopicDefinition

    public override fun setUpHook() {
        newsletterTopicDefinitionDummy = mockk()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(newsletterTopicServiceMock)
    }

    override fun makePage(): NewsletterTopicEditPage = NewsletterTopicEditPage(Model.of(ntd), null)

    override val pageClass: Class<NewsletterTopicEditPage>
        get() = NewsletterTopicEditPage::class.java

    public override fun assertSpecificComponents() {
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        var bb = b + "header"
        tester.assertLabel(bb + "Label", "Newsletter Topic")
        bb += "Panel:"
        tester.assertComponent(bb + "back", BootstrapButton::class.java)
        tester.assertComponent(bb + "submit", BootstrapButton::class.java)
        tester.assertComponent(bb + "delete", BootstrapButton::class.java)
        bb = b + "translations"
        tester.assertLabel(bb + "Label", "Topic Translations")
        bb = b + "translationsPanel:translations"
        tester.assertComponent(bb, RefreshingView::class.java)
        bb += ":"
        assertTranslation(bb, 1, "de", "thema1")
        assertTranslation(bb, 2, "en", "topic1")
        assertTranslation(bb, 3, "fr", "sujet1")
    }

    private fun assertTranslation(bb: String, idx: Int, langCode: String, title: String) {
        tester.assertLabel("$bb$idx:langCode", langCode)
        tester.assertComponent("$bb$idx:title", TextField::class.java)
        tester.assertModelValue("$bb$idx:title", title)
    }

    @Test
    fun submitting_withSuccessfulServiceCall_addsInfoMsg() {
        every { newsletterTopicServiceMock.saveOrUpdate(any()) } returns ntd
        runSubmitTest()
        tester.assertInfoMessages(
            "Successfully saved NewsletterTopic [id 1]: DE: '1806'; EN: 'topic1'; FR: 'sujet1'."
        )
        tester.assertNoErrorMessage()
    }

    private fun runSubmitTest() {
        tester.startPage(NewsletterTopicEditPage(Model.of(ntd), null))
        val formTester = tester.newFormTester("form")
        formTester.setValue("translationsPanel:translations:1:title", "1806")
        assertTranslation("form:translationsPanel:translations:", 1, "de", "thema1")
        formTester.submit("headerPanel:submit")
        assertTranslation("form:translationsPanel:translations:", 4, "de", "1806")
        verify { newsletterTopicServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        every { newsletterTopicServiceMock.saveOrUpdate(any()) } returns null
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Could not save NewsletterTopic [id 1].")
    }

    @Test
    fun submitting_withOptimisticLockingException_addsErrorMsg() {
        every { newsletterTopicServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The tblName with id 1 has been modified concurrently " +
                "by another user. Please reload it and apply your changes once more."
        )
    }

    @Test
    fun submitting_withDuplicateKeyException_addsErrorMsg() {
        every { newsletterTopicServiceMock.saveOrUpdate(any()) } throws
            DuplicateKeyException("some message about duplicate key stuff")
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("some message about duplicate key stuff")
    }

    @Test
    fun submitting_withOtherException_addsErrorMsg() {
        every { newsletterTopicServiceMock.saveOrUpdate(any()) } throws RuntimeException("fooMsg")
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "An unexpected error occurred when trying to save NewsletterTopic [id 1]: fooMsg"
        )
    }

    @Test
    fun submittingDelete_delegatesDeleteToService() {
        every { newsletterTopicServiceMock.delete(any(), any()) } returns newsletterTopicDefinitionDummy

        tester.startPage(NewsletterTopicEditPage(Model.of(ntd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")

        verify { newsletterTopicServiceMock.delete(1, 1) }
        verify(exactly = 0) { newsletterTopicServiceMock.saveOrUpdate(any()) }
        verify { newsletterTopicServiceMock.countByFilter(any()) }

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
    }

    @Test
    fun submitting_withForeignKeyConstraintViolationException_addsErrorMsg() {
        val msg = "...whatever..."
        every { newsletterTopicServiceMock.delete(any(), any()) } throws DataIntegrityViolationException(msg)

        tester.startPage(NewsletterTopicEditPage(Model.of(ntd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")

        verify { newsletterTopicServiceMock.delete(any(), any()) }

        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Unable to delete this record as it is still used in other places.")
    }

    @Test
    fun clickingBackButton_withPageWithoutCallingPageRef_forwardsToNewsletterTopicListPage() {
        tester.startPage(NewsletterTopicEditPage(Model.of(ntd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(NewsletterTopicListPage::class.java)

        // from NewsletterListPage
        verify { newsletterTopicServiceMock.countByFilter(any()) }
    }

    @Test
    fun clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        tester.startPage(NewsletterTopicEditPage(Model.of(ntd), LogoutPage(PageParameters()).pageReference))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(LogoutPage::class.java)
    }
}
