package ch.difty.scipamato.core.web.paper.entry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.core.pubmed.PubmedArticleService;
import ch.difty.scipamato.core.web.paper.common.PaperPanelTest;

public abstract class EditablePaperPanelTest extends PaperPanelTest<Paper, EditablePaperPanel> {

    static final int     PMID            = 1234;
    static final long    SEARCH_ORDER_ID = 5678L;
    static final boolean SHOW_EXCLUDED   = false;

    @MockBean
    PubmedArticleService pubmedArticleServiceMock;

    @Mock
    PubmedArticleFacade pubmedArticleMock;
    @Mock
    PageReference       callingPageMock;

    // used for referring to PaperSearchPage - not verifying
    @MockBean
    SearchOrderService searchOrderServiceMock;

    @Override
    protected void tearDownLocalHook() {
        verifyNoMoreInteractions(pubmedArticleServiceMock, pubmedArticleMock, newsletterServiceMock);
    }

    @Override
    protected EditablePaperPanel makePanel() {
        Paper p = new Paper();
        p.setId(1L);
        p.setNumber(100L);
        p.setAuthors("a");
        p.setFirstAuthor("fa");
        p.setFirstAuthorOverridden(false);

        p.setTitle("t");
        p.setLocation("l");
        p.setPublicationYear(2017);

        p.setPmId(PMID);
        p.setDoi("doi");
        p.setCreated(LocalDateTime.parse("2017-02-01T13:34:45"));
        p.setCreatedByName("u1");
        p.setLastModified(LocalDateTime.parse("2017-03-01T13:34:45"));
        p.setLastModifiedByName("u2");

        p.setGoals("g");
        p.setPopulation("p");
        p.setMethods("m");

        p.setPopulationPlace("ppl");
        p.setPopulationParticipants("ppa");
        p.setPopulationDuration("pd");
        p.setExposurePollutant("ep");
        p.setExposureAssessment("ea");
        p.setMethodStudyDesign("msd");
        p.setMethodOutcome("mo");
        p.setMethodStatistics("ms");
        p.setMethodConfounders("mc");

        p.setResult("r");
        p.setComment("c");
        p.setIntern("i");
        p.setResultMeasuredOutcome("rmo");
        p.setResultExposureRange("rer");
        p.setResultEffectEstimate("ree");
        p.setConclusion("cc");

        p.addCode(newC(1, "F"));
        p.setMainCodeOfCodeclass1("mcocc1");
        p.addCode(newC(2, "A"));
        p.addCode(newC(3, "A"));
        p.addCode(newC(4, "A"));
        p.addCode(newC(5, "A"));
        p.addCode(newC(6, "A"));
        p.addCode(newC(7, "A"));
        p.addCode(newC(8, "A"));

        p.setOriginalAbstract("oa");

        return new EditablePaperPanel(PANEL_ID, Model.of(p), callingPageMock, SEARCH_ORDER_ID, SHOW_EXCLUDED, getMode(),
            Model.of(0)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                // no-op
            }

            @Override
            protected PaperEntryPage getResponsePage(Paper p, Long searchOrderId, boolean showingExclusions) {
                // no-op
                return null;
            }

        };
    }

    abstract Mode getMode();

    @Test
    public void specificFields_areDisabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isDisabled("panel:form:id");
        getTester().isDisabled("panel:form:firstAuthorOverridden");
        getTester().isDisabled("panel:form:createdDisplayValue");
        getTester().isDisabled("panel:form:modifiedDisplayValue");
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    EditablePaperPanel makePanelWithEmptyPaper(Integer pmId) {
        Paper p = new Paper();
        p.setPmId(pmId);
        return new EditablePaperPanel(PANEL_ID, Model.of(p), null, null, SHOW_EXCLUDED, getMode(), Model.of(0)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                // no-op
            }

            @Override
            protected PaperEntryPage getResponsePage(Paper p, Long searchOrderId, boolean showingExclusions) {
                // no-op
                return null;
            }
        };
    }

    @SuppressWarnings("SameParameterValue")
    EditablePaperPanel makePanelWith(Integer pmId, PageReference callingPage, Long searchOrderId,
        boolean showExcluded) {
        Paper p = new Paper();
        p.setId(1L);
        p.setPmId(pmId);
        return new EditablePaperPanel(PANEL_ID, Model.of(p), callingPage, searchOrderId, showExcluded, getMode(),
            Model.of(0)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                // no-op
            }

            @Override
            protected PaperEntryPage getResponsePage(Paper p, Long searchOrderId, boolean showingExclusions) {
                throw new RuntimeException("forward to calling page triggered");
            }
        };
    }

    @Test
    public void withNoCallingPage_hasInvisibleBackButton() {
        getTester().startComponentInPage(makePanelWith(PMID, null, SEARCH_ORDER_ID, SHOW_EXCLUDED));

        getTester().assertInvisible(PANEL_ID + ":form:back");

        if (getMode() != Mode.VIEW)
            verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void isAssociatedWithNewsletter_withNoNewsletterLink() {
        EditablePaperPanel p = makePanel();
        assertThat(p.isAssociatedWithNewsletter()).isFalse();
    }

    @Test
    public void isAssociatedWithWipNewsletter_withNoNewsletterLink_isFalse() {
        EditablePaperPanel p = makePanel();
        assertThat(p.isAssociatedWithWipNewsletter()).isFalse();
    }

    @Test
    public void isAssociatedWithWipNewsletter_withNewsletterLinkInNonWipStatus_isFalse() {
        EditablePaperPanel p = makePanel();
        p
            .getModelObject()
            .setNewsletterLink(new Paper.NewsletterLink(1, "i1", PublicationStatus.PUBLISHED.getId(), 1, "t1", "hl"));
        assertThat(p.isAssociatedWithWipNewsletter()).isFalse();
    }

    @Test
    public void isAssociatedWithWipNewsletter_withNewsletterLinkInWipStatus_isTrue() {
        EditablePaperPanel p = makePanel();
        p
            .getModelObject()
            .setNewsletterLink(new Paper.NewsletterLink(1, "i1", PublicationStatus.WIP.getId(), 1, "t1", "hl"));
        assertThat(p.isAssociatedWithWipNewsletter()).isTrue();
    }

    @Test
    public void assertSummaryLink() {
        getTester().startComponentInPage(makePanel());
        String path = "panel:form:summary";
        getTester().assertComponent(path, ResourceLink.class);
        getTester().assertVisible(path);
        getTester().assertEnabled(path);
        if (getMode() != Mode.VIEW)
            verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void assertSummaryShortLink() {
        getTester().startComponentInPage(makePanel());
        String path = "panel:form:summaryShort";
        getTester().assertComponent(path, ResourceLink.class);
        getTester().assertVisible(path);
        getTester().assertEnabled(path);
        if (getMode() != Mode.VIEW)
            verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

}
