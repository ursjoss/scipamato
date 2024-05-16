package ch.difty.scipamato.core.web.newsletter.list

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.web.authentication.LoginPage
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.core.web.paper.list.PaperListPage
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test
import java.time.LocalDate

private const val NEWSLETTER_ID = 1

internal class NewsletterTopicSortPageTest : BasePageTest<NewsletterTopicSortPage>() {

    private lateinit var newsletter: Newsletter

    private lateinit var topics: List<NewsletterNewsletterTopic>

    override fun setUpHook() {
        super.setUpHook()
        newsletter = Newsletter("18/06", LocalDate.now(), PublicationStatus.WIP).apply {
            id = NEWSLETTER_ID
        }
        topics = listOf(
            NewsletterNewsletterTopic(NEWSLETTER_ID, 1, 0, "topic1"),
            NewsletterNewsletterTopic(NEWSLETTER_ID, 2, 1, "topic2")
        )
        every { newsletterTopicServiceMock.getSortedNewsletterTopicsForNewsletter(NEWSLETTER_ID) } returns topics
        every { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) } returns Unit
    }

    override fun makePage(): NewsletterTopicSortPage = NewsletterTopicSortPage(Model.of(newsletter), null)

    override val pageClass: Class<NewsletterTopicSortPage>
        get() = NewsletterTopicSortPage::class.java

    override fun assertSpecificComponents() {
        val b = "form"
        tester.assertComponent(b, Form::class.java)
        var bb = "$b:sortable"
        tester.assertComponent(bb, Sortable::class.java)
        bb += ":items:"
        for (i in topics.indices) tester.assertLabel("$bb$i:item", "topic" + (i + 1))
        bb = "$b:submit"
        tester.assertComponent(bb, BootstrapAjaxButton::class.java)
        tester.assertLabel("$bb:label", "Save")
        bb = "$b:cancel"
        tester.assertComponent(bb, BootstrapAjaxButton::class.java)
        tester.assertLabel("$bb:label", "Cancel")
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
    }

    @Test
    fun startingPageWithNonNullModelWithNullId_throws() {
        newsletter.id = null
        invoking {
            tester.startPage(NewsletterTopicSortPage(Model.of(newsletter), null))
        } shouldThrow IllegalStateException::class withMessage
            "Cannot start page w/o non-null newsletter topic id in model"

        verify(exactly = 0) { newsletterTopicServiceMock.getSortedNewsletterTopicsForNewsletter(any()) }
        verify(exactly = 0) { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(any()) }
    }

    @Test
    fun startingPageWithNonNullModel_loadsSortedNewsletterTopics() {
        tester.startPage(NewsletterTopicSortPage(Model.of(newsletter), null))
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
        verify { newsletterTopicServiceMock.getSortedNewsletterTopicsForNewsletter(NEWSLETTER_ID) }
    }

    @Test
    fun clickSubmit_withNoPreviousPage_callsSave_andNavigatesToPaperListPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:submit", "click")
        tester.assertRenderedPage(PaperListPage::class.java)
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
        verify { newsletterTopicServiceMock.saveSortedNewsletterTopics(NEWSLETTER_ID, topics) }
    }

    @Test
    fun clickSubmit_withPreviousPage_callsSave_andNavigatesToPreviousPage() {
        tester.startPage(
            NewsletterTopicSortPage(Model.of(newsletter), LoginPage(PageParameters()).pageReference)
        )
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:submit", "click")
        tester.assertRenderedPage(LoginPage::class.java)
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
        verify { newsletterTopicServiceMock.saveSortedNewsletterTopics(NEWSLETTER_ID, topics) }
    }

    @Test
    fun clickSubmit_withSaveThrowing_addsErrorMessage_andStaysOnPage() {
        every { newsletterTopicServiceMock.saveSortedNewsletterTopics(NEWSLETTER_ID, topics) } throws
            RuntimeException("boom")
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:submit", "click")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Unexpected error: boom")
        tester.assertRenderedPage(NewsletterTopicSortPage::class.java)
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
        verify { newsletterTopicServiceMock.saveSortedNewsletterTopics(NEWSLETTER_ID, topics) }
    }

    @Test
    fun clickCancel_withNoPreviousPage_doesNotCallSave_butNavigatesToPaperListPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:cancel", "click")
        tester.assertRenderedPage(PaperListPage::class.java)
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
        verify(exactly = 0) { newsletterTopicServiceMock.saveSortedNewsletterTopics(any(), any()) }
    }

    @Test
    fun clickCancel_withPreviousPage_doesNotCallSave_butNavigatesToPreviousPage() {
        tester.startPage(
            NewsletterTopicSortPage(Model.of(newsletter), LoginPage(PageParameters()).pageReference)
        )
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:cancel", "click")
        tester.assertRenderedPage(LoginPage::class.java)
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(NEWSLETTER_ID) }
        verify(exactly = 0) { newsletterTopicServiceMock.saveSortedNewsletterTopics(any(), any()) }
    }
}
