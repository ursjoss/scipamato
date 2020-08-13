package ch.difty.scipamato.core.web.newsletter.edit

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage
import ch.difty.scipamato.core.web.paper.result.EDIT_LINK
import ch.difty.scipamato.core.web.paper.result.ResultPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.LocalDateTextField
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.Model
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.ArrayList
import java.util.Optional

internal class NewsletterEditPageTest : BasePageTest<NewsletterEditPage>() {

    private val nl = Newsletter
        .builder()
        .issue("1804")
        .issueDate(LocalDate.parse("2018-04-01"))
        .publicationStatus(PublicationStatus.CANCELLED)
        .build()

    @AfterEach
    fun tearDown() {
        confirmVerified(newsletterServiceMock)
    }

    override fun makePage(): NewsletterEditPage = NewsletterEditPage(Model.of(nl))

    override val pageClass: Class<NewsletterEditPage>
        get() = NewsletterEditPage::class.java

    public override fun assertSpecificComponents() {
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        tester.assertLabel(b + "issueLabel", "Issue")
        tester.assertComponent(b + "issue", TextField::class.java)
        tester.assertLabel(b + "issueDateLabel", "Issue Date")
        tester.assertComponent(b + "issueDate", LocalDateTextField::class.java)
        tester.assertLabel(b + "publicationStatusLabel", "Publication Status")
        tester.assertComponent(b + "publicationStatus", BootstrapSelect::class.java)
        tester.assertEnabled(b + "publicationStatus")
        tester.assertComponent(b + "submit", BootstrapButton::class.java)
        tester.assertComponent("resultPanel", ResultPanel::class.java)
    }

    @Test
    fun submitting_callsService() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        every { newsletterServiceMock.saveOrUpdate(any()) } returns nl
        tester.startPage(NewsletterEditPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("issue", "1806")
        formTester.submit("submit")
        tester.assertInfoMessages("Successfully saved Newsletter [id 0]: 1804 ({2}).")
        tester.assertNoErrorMessage()
        verify { newsletterServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withServiceReturningNull_addsErrorMessage() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        every { newsletterServiceMock.saveOrUpdate(any()) } returns null
        tester.startPage(NewsletterEditPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("issue", "1806")
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save Newsletter [id 0]: ")
        verify { newsletterServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withServiceReturningOptimisticLockingException() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        every { newsletterServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("newsletter", OptimisticLockingException.Type.UPDATE)
        tester.startPage(NewsletterEditPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("issue", "1806")
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The newsletter with id 0 has been modified concurrently by another user. Please reload it and apply your changes once more."
        )
        verify { newsletterServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withIllegalArgumentException() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        every { newsletterServiceMock.saveOrUpdate(any()) } throws
            IllegalArgumentException("newsletter.onlyOneInStatusWipAllowed")
        tester.startPage(NewsletterEditPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("issue", "1806")
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "Another newsletter is already in status WIP. You cannot save the current newsletter in the same status."
        )
        verify { newsletterServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withOtherException() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        every { newsletterServiceMock.saveOrUpdate(any()) } throws
            RuntimeException("boom")
        tester.startPage(NewsletterEditPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("issue", "1806")
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save Newsletter [id 0]: boom")
        verify { newsletterServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun callingWithoutModel_restrictsToCreatingNewWIPNewsletter() {
        tester.startPage(NewsletterEditPage())
        tester.assertRenderedPage(pageClass)
        tester.assertDisabled("form:publicationStatus")
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    @Test
    fun fieldsIssueAndIssueDate_areOnlyEnabledIfNewsletterIsInProgress() {
        for (ps in PublicationStatus.values()) {
            val nl = Newsletter
                .builder()
                .issue("1804")
                .issueDate(LocalDate.parse("2018-04-01"))
                .publicationStatus(ps)
                .build()
            tester.startPage(NewsletterEditPage(Model.of(nl)))
            tester.assertRenderedPage(NewsletterEditPage::class.java)
            val b = "form:"
            if (ps.isInProgress) {
                tester.assertEnabled(b + "issue")
                tester.assertEnabled(b + "issueDate")
            } else {
                tester.assertDisabled(b + "issue")
                tester.assertDisabled(b + "issueDate")
            }
        }
    }

    @Test
    fun clickingTitleInResultPanel_opensPaperEntryPage() {
        val papers: MutableList<PaperSlim> = ArrayList()
        val ps = PaperSlim()
        ps.id = 1L
        ps.number = 2L
        ps.title = "some title"
        ps.publicationYear = 2019
        ps.firstAuthor = "Foo"
        papers.add(ps)
        val p = Paper()
        p.id = ps.id
        every { paperSlimServiceMock.countByFilter(any()) } returns papers.size
        every { paperSlimServiceMock.findPageByFilter(any(), any()) } returns papers
        every { paperServiceMock.findByNumber(ps.number, "en_us") } returns Optional.of(p)
        tester.startPage(makePage())
        tester.assertRenderedPage(NewsletterEditPage::class.java)
        tester.clickLink("resultPanel:$EDIT_LINK")
        tester.assertRenderedPage(PaperEntryPage::class.java)
        verify(exactly = 2) { paperSlimServiceMock.countByFilter(any()) }
        verify(exactly = 1) { paperSlimServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 3) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify { paperServiceMock.findByNumber(ps.number, "en_us") }
    }
}
