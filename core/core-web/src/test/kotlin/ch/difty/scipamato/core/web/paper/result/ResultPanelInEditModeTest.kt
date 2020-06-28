package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.projection.NewsletterAssociation
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter
import org.apache.wicket.util.tester.TagTester
import org.junit.jupiter.api.Test

internal class ResultPanelInEditModeTest : ResultPanelTest() {

    override val mode: Mode
        get() = Mode.EDIT

    override fun assertTableRow(bb: String) {
        assertEditableTableRow(bb)
    }

    @Test
    fun clickingDeleteIconLink_() {
        assertClickingDeleteIconLink()
    }

    @Test
    fun startingPage_showingResults() {
        every { searchOrderMock.isShowExcluded } returns false
        assertExcludeIcon("fas fa-ban fa-fw", "Exclude the paper from the search")
    }

    @Test
    fun startingPage_showingExclusions() {
        every { searchOrderMock.isShowExcluded } returns true
        assertExcludeIcon("far fa-check-circle fa-fw", "Re-include the paper into the search")
    }

    @Test
    fun startingPage_withPaperWithNoNewsletter_rendersAddToNewsletterLink() {
        paperSlim.newsletterAssociation.shouldBeNull()
        assertNewsletterIcon("fas fa-plus-square fa-fw", "Add to current newsletter")
    }

    @Test
    fun startingPage_withPaperWithNoNewsletter_andNoNewsletterInWip_rendersBlankLink() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        paperSlim.newsletterAssociation.shouldBeNull()
        tester.startComponentInPage(newNonSearchRelevantResultPanel())
        val responseTxt = tester.lastResponse.document
        TagTester.createTagByAttribute(responseTxt, "class", "fas fa-plus-square fa-fw").shouldBeNull()
        TagTester.createTagByAttribute(responseTxt, "class", "far fa-envelope fa-fw").shouldBeNull()
        TagTester.createTagByAttribute(responseTxt, "class", "far fa-envelope-open fa-fw").shouldBeNull()
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun startingPage_withPaperWithNewsletterInStatusWip_rendersRemoveFromNewsletterLink() {
        val ns = NewsletterAssociation(1, "1802", PublicationStatus.WIP.id, null)
        paperSlim.newsletterAssociation = ns
        assertNewsletterIcon("far fa-envelope-open fa-fw", "Remove from current newsletter")
    }

    @Test
    fun startingPage_withPaperWithPublishedNewsletter_rendersAssociatedWithNewsletterLink() {
        val ns = NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.id, null)
        paperSlim.newsletterAssociation = ns
        assertNewsletterIcon("far fa-envelope fa-fw", "Newsletter 1802")
    }

    @Test
    fun startingPage_withPaperWithNoNewsletter_whenAddingToNewsletter_mergesPaperIntoWipNewsletter() {
        paperSlim.newsletterAssociation.shouldBeNull()
        clickNewsletterLink()
        verify { newsletterServiceMock.mergePaperIntoWipNewsletter(paperSlim.id!!) }
    }

    private fun clickNewsletterLink() {
        tester.startComponentInPage(newNonSearchRelevantResultPanel())
        tester.clickLink("$PANEL_ID:table:body:rows:1:cells:6:cell:link")
        verify(exactly = 2) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify(exactly = 2) { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
        tester.assertComponentOnAjaxResponse("$PANEL_ID:table")
    }

    @Test
    fun startingPage_withPaperWithNewsletterInStatusWip_whenRemovingFromNewsletter_removesPaperFromWipNewsletter() {
        val ns = NewsletterAssociation(1, "1802", PublicationStatus.WIP.id, null)
        paperSlim.newsletterAssociation = ns
        clickNewsletterLink()
        verify { newsletterServiceMock.removePaperFromWipNewsletter(paperSlim.id!!) }
    }

    @Test
    fun startingPage_withPaperWithNoNewsletter_andWithNoWipNewsletterReady_whenAddingToNewsletter_issuesWarning() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        paperSlim.newsletterAssociation.shouldBeNull()
        clickNewsletterLink()
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(300),
            "There is no newsletter in status 'In Progress'."
        )
    }

    @Test
    fun startingPage_withPaperWithPublishedNewsletter_whenClickingIcon_issuesWarning() {
        val ns = NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.id, null)
        paperSlim.newsletterAssociation = ns
        clickNewsletterLink()
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(300),
            "Newsletter 1802 has been closed and cannot be modified."
        )
    }
}
