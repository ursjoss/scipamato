package ch.difty.scipamato.core.web.paper.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.util.tester.TagTester;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.projection.NewsletterAssociation;
import ch.difty.scipamato.core.entity.search.SearchOrder;

class ResultPanelInEditModeTest extends ResultPanelTest {

    @Override
    Mode getMode() {
        return Mode.EDIT;
    }

    @Override
    void assertTableRow(String bb) {
        assertEditableTableRow(bb);
    }

    @Test
    void clickingDeleteIconLink_() {
        assertClickingDeleteIconLink();
    }

    @Test
    void startingPage_showingResults() {
        when(searchOrderMock.isShowExcluded()).thenReturn(false);
        assertExcludeIcon("fas fa-ban fa-fw", "Exclude the paper from the search");
    }

    @Test
    void startingPage_showingExclusions() {
        when(searchOrderMock.isShowExcluded()).thenReturn(true);
        assertExcludeIcon("far fa-check-circle fa-fw", "Re-include the paper into the search");
    }

    @Test
    void startingPage_withPaperWithNoNewsletter_rendersAddToNewsletterLink() {
        assertThat(paperSlim.getNewsletterAssociation()).isNull();
        assertNewsletterIcon("fas fa-plus-square fa-fw", "Add to current newsletter");
    }

    @Test
    void startingPage_withPaperWithNoNewsletter_andNoNewsletterInWip_rendersBlankLink() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        assertThat(paperSlim.getNewsletterAssociation()).isNull();

        getTester().startComponentInPage(newNonSearchRelevantResultPanel());

        String responseTxt = getTester()
            .getLastResponse()
            .getDocument();

        assertThat(TagTester.createTagByAttribute(responseTxt, "class", "fas fa-plus-square fa-fw")).isNull();
        assertThat(TagTester.createTagByAttribute(responseTxt, "class", "far fa-envelope fa-fw")).isNull();
        assertThat(TagTester.createTagByAttribute(responseTxt, "class", "far fa-envelope-open fa-fw")).isNull();

        verify(paperSlimServiceMock, times(1)).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationRequest.class));
    }

    @Test
    void startingPage_withPaperWithNewsletterInStatusWip_rendersRemoveFromNewsletterLink() {
        NewsletterAssociation ns = new NewsletterAssociation(1, "1802", PublicationStatus.WIP.getId(), null);
        paperSlim.setNewsletterAssociation(ns);
        assertNewsletterIcon("far fa-envelope-open fa-fw", "Remove from current newsletter");
    }

    @Test
    void startingPage_withPaperWithPublishedNewsletter_rendersAssociatedWithNewsletterLink() {
        NewsletterAssociation ns = new NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.getId(), null);
        paperSlim.setNewsletterAssociation(ns);
        assertNewsletterIcon("far fa-envelope fa-fw", "Newsletter 1802");
    }

    @Test
    void startingPage_withPaperWithNoNewsletter_whenAddingToNewsletter_mergesPaperIntoWipNewsletter() {
        assertThat(paperSlim.getNewsletterAssociation()).isNull();

        clickNewsletterLink();
        verify(newsletterServiceMock).mergePaperIntoWipNewsletter(paperSlim.getId());
    }

    private void clickNewsletterLink() {
        getTester().startComponentInPage(newNonSearchRelevantResultPanel());

        getTester().clickLink(PANEL_ID + ":table:body:rows:1:cells:6:cell:link");

        verify(paperSlimServiceMock, times(2)).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock, times(2)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
        verify(paperServiceMock, times(2)).findPageOfIdsBySearchOrder(isA(SearchOrder.class),
            isA(PaginationRequest.class));

        getTester().assertComponentOnAjaxResponse(PANEL_ID + ":table");
    }

    @Test
    void startingPage_withPaperWithNewsletterInStatusWip_whenRemovingFromNewsletter_removesPaperFromWipNewsletter() {
        NewsletterAssociation ns = new NewsletterAssociation(1, "1802", PublicationStatus.WIP.getId(), null);
        paperSlim.setNewsletterAssociation(ns);

        clickNewsletterLink();
        verify(newsletterServiceMock).removePaperFromWipNewsletter(paperSlim.getId());
    }

    @Test
    void startingPage_withPaperWithNoNewsletter_andWithNoWipNewsletterReady_whenAddingToNewsletter_issuesWarning() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        assertThat(paperSlim.getNewsletterAssociation()).isNull();

        clickNewsletterLink();

        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(300),
            "There is no newsletter in status 'In Progress'.");
    }

    @Test
    void startingPage_withPaperWithPublishedNewsletter_whenClickingIcon_issuesWarning() {
        NewsletterAssociation ns = new NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.getId(), null);
        paperSlim.setNewsletterAssociation(ns);

        clickNewsletterLink();

        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(300),
            "Newsletter 1802 has been closed and cannot be modified.");
    }
}
