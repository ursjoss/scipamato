package ch.difty.scipamato.core.web.paper.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.projection.NewsletterAssociation;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ResultPanelInEditModeTest extends ResultPanelTest {

    @Override
    protected Mode getMode() {
        return Mode.EDIT;
    }

    @Override
    protected void assertTableRow(String bb) {
        assertEditableTableRow(bb);
    }

    @Test
    public void clickingDeleteIconLink_() {
        assertClickingDeleteIconLink();
    }

    @Test
    public void startingPage_showingResults() {
        when(searchOrderMock.isShowExcluded()).thenReturn(false);
        assertExcludeIcon("fa fa-fw fa-ban", "Exclude the paper from the search");
    }

    @Test
    public void startingPage_showingExclusions() {
        when(searchOrderMock.isShowExcluded()).thenReturn(true);
        assertExcludeIcon("fa fa-fw fa-check-circle-o", "Re-include the paper into the search");
    }

    @Test
    public void startingPage_withPaperWithNoNewsletter_rendersAddToNewsletterLink() {
        assertThat(paperSlim.getNewsletterAssociation()).isNull();
        assertNewsletterIcon("fa fa-fw fa-plus-square-o", "Add to current newsletter");
    }

    @Test
    public void startingPage_withPaperWithNewsletter_rendersAddToNewsletterLink() {
        NewsletterAssociation ns = new NewsletterAssociation(1, "1802", PublicationStatus.PUBLISHED.getId(), null);
        paperSlim.setNewsletterAssociation(ns);
        assertNewsletterIcon("fa fa-fw fa-envelope-o", "Newsletter 1802");
    }

}
