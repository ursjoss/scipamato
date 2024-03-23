package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.projection.NewsletterAssociation
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class ResultPanelInSearchModeTest : ResultPanelTest() {

    override val mode: Mode
        get() = Mode.SEARCH

    override fun assertTableRow(bb: String) {
        assertEditableTableRow(bb)
    }

    @Test
    fun clickingDeleteIconLink_() {
        assertClickingDeleteIconLink()
    }

    @Test
    fun startingPage_showingResults() {
        searchOrder.apply { isShowExcluded = false }
        assertExcludeIcon("fa-solid fa-ban fa-fw", "Exclude the paper from the search")
    }

    @Test
    fun startingPage_showingExclusions() {
        searchOrder.apply { isShowExcluded = true }
        assertExcludeIcon("fa-regular fa-circle-check fa-fw", "Re-include the paper into the search")
    }

    @Test
    fun startingPage_withPaperWithNoNewsletter_rendersAddToNewsletterLink() {
        paperSlim.newsletterAssociation.shouldBeNull()
        assertNewsletterIcon("fa-solid fa-square-plus fa-fw", "Add to current newsletter")
    }

    @Test
    fun startingPage_withPaperWithNewsletter_rendersAddToNewsletterLink() {
        val ns = NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.id, null)
        paperSlim.newsletterAssociation = ns
        assertNewsletterIcon("fa-regular fa-envelope fa-fw", "Newsletter 1802")
    }
}
