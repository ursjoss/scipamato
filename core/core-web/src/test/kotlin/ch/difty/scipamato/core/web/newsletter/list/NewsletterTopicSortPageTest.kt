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
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class NewsletterTopicSortPageTest : BasePageTest<NewsletterTopicSortPage>() {

    private lateinit var newsletter: Newsletter

    private lateinit var topics: List<NewsletterNewsletterTopic>

    override fun setUpHook() {
        super.setUpHook()
        newsletter = Newsletter("18/06", LocalDate.now(), PublicationStatus.WIP).apply {
            id = 1
        }
        topics = listOf(
            NewsletterNewsletterTopic(newsletter.id!!, 1, 0, "topic1"),
            NewsletterNewsletterTopic(newsletter.id!!, 2, 1, "topic2")
        )
        every { newsletterTopicServiceMock.getSortedNewsletterTopicsForNewsletter(newsletter.id!!) } returns topics
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
    }

    @Test
    fun startingPageWithNonNullModel__loadsSortedNewsletterTopics() {
        tester.startPage(NewsletterTopicSortPage(Model.of(newsletter), null))
        verify { newsletterTopicServiceMock.getSortedNewsletterTopicsForNewsletter(newsletter.id!!) }
    }

    @Test
    fun startingPageWithNonModel__loadsSortedNewsletterTopics() {
        tester.startPage(NewsletterTopicSortPage(null, null))
        verify(exactly = 0) { newsletterTopicServiceMock.getSortedNewsletterTopicsForNewsletter(any()) }
    }

    @Test
    fun clickSubmit_withNoPreviousPage_callsSave_andNavigatesToPaperListPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:submit", "click")
        tester.assertRenderedPage(PaperListPage::class.java)
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
        verify { newsletterTopicServiceMock.saveSortedNewsletterTopics(newsletter.id!!, topics) }
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
        verify { newsletterTopicServiceMock.saveSortedNewsletterTopics(newsletter.id!!, topics) }
    }

    @Test
    fun clickSubmit_withSaveThrowing_addsErrorMessage_andStaysOnPage() {
        every { newsletterTopicServiceMock.saveSortedNewsletterTopics(newsletter.id!!, topics) } throws
            RuntimeException("boom")
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:submit", "click")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Unexpected error: boom")
        tester.assertRenderedPage(NewsletterTopicSortPage::class.java)
        verify { newsletterTopicServiceMock.saveSortedNewsletterTopics(newsletter.id!!, topics) }
    }

    @Test
    fun clickCancel_withNoPreviousPage_doesNotCallSave_butNavigatesToPaperListPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.executeAjaxEvent("form:cancel", "click")
        tester.assertRenderedPage(PaperListPage::class.java)
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
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
        verify(exactly = 0) { newsletterTopicServiceMock.saveSortedNewsletterTopics(any(), any()) }
    }
}
