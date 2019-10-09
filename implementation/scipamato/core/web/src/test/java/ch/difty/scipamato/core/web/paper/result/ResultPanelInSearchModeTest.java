package ch.difty.scipamato.core.web.paper.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.projection.NewsletterAssociation;

class ResultPanelInSearchModeTest extends ResultPanelTest {

    @Override
    Mode getMode() {
        return Mode.SEARCH;
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
    void startingPage_withPaperWithNewsletter_rendersAddToNewsletterLink() {
        NewsletterAssociation ns = new NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.getId(), null);
        paperSlim.setNewsletterAssociation(ns);
        assertNewsletterIcon("far fa-envelope fa-fw", "Newsletter 1802");
    }

}
