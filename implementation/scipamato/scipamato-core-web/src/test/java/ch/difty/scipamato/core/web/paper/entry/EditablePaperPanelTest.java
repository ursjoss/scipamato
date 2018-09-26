package ch.difty.scipamato.core.web.paper.entry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.PageReference;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TagTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.core.pubmed.PubmedArticleService;
import ch.difty.scipamato.core.web.paper.common.PaperPanelTest;
import ch.difty.scipamato.core.web.paper.search.PaperSearchPage;

public class EditablePaperPanelTest extends PaperPanelTest<Paper, EditablePaperPanel> {

    private static final int     PMID            = 1234;
    private static final long    SEARCH_ORDER_ID = 5678L;
    private static final boolean SHOW_EXCLUDED   = false;

    @MockBean
    private PubmedArticleService pubmedArticleServiceMock;

    @Mock
    private PubmedArticleFacade pubmedArticleMock;
    @Mock
    private PageReference       callingPageMock;

    // used for referring to PaperSearchPage - not verifying
    @MockBean
    private SearchOrderService searchOrderServiceMock;

    @Override
    protected void setUpLocalHook() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(false);
        // when referring to PaperSearchPage
        when(searchOrderServiceMock.findById(5678L)).thenReturn(Optional.empty());
    }

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

        return new EditablePaperPanel(PANEL_ID, Model.of(p), callingPageMock, SEARCH_ORDER_ID, SHOW_EXCLUDED,
            Mode.EDIT) {
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

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, EditablePaperPanel.class);

        assertCommonComponents(b);

        b += ":form";
        assertTextFieldWithLabel(b + ":id", 1L, "ID");
        assertTextFieldWithLabel(b + ":number", 100L, "SciPaMaTo No.");
        assertTextFieldWithLabel(b + ":publicationYear", 2017, "Pub. Year");
        assertTextFieldWithLabel(b + ":pmId", 1234, "PMID");
        getTester().assertLabel(b + ":submit:label", "Save");
        assertTextFieldWithLabel(b + ":createdDisplayValue", "u1 (2017-02-01 13:34:45)", "Created");
        assertTextFieldWithLabel(b + ":modifiedDisplayValue", "u2 (2017-03-01 13:34:45)", "Last Modified");

        getTester().assertComponent(b + ":back", BootstrapButton.class);
        getTester().assertComponent(b + ":previous", BootstrapButton.class);
        getTester().assertComponent(b + ":next", BootstrapButton.class);
        // disabled as paperManagerMock is not mocked here
        getTester().isDisabled(b + ":previous");
        getTester().isDisabled(b + ":next");

        getTester().assertComponent(b + ":pubmedRetrieval", BootstrapAjaxLink.class);
        getTester().assertVisible(b + ":pubmedRetrieval");

        String bb = b + ":tabs:panelsContainer:panels:11:tab6Form";
        getTester().assertComponent(bb + ":dropzone", DropZoneFileUpload.class);
        getTester().assertVisible(bb + ":dropzone");
        getTester().assertComponent(bb + ":attachments", BootstrapDefaultDataTable.class);
        getTester().assertComponent(bb, Form.class);

        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void specificFields_areDisabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isDisabled("panel:form:id");
        getTester().isDisabled("panel:form:firstAuthorOverridden");
        getTester().isDisabled("panel:form:createdDisplayValue");
        getTester().isDisabled("panel:form:modifiedDisplayValue");
        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void assertSummaryLink() {
        getTester().startComponentInPage(makePanel());
        String path = "panel:form:summary";
        getTester().assertComponent(path, ResourceLink.class);
        getTester().assertVisible(path);
        getTester().assertEnabled(path);
        verifyCodeAndCodeClassCalls(1);
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
        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void assertSubmit() {
        getTester().startComponentInPage(makePanel());
        applyTestHackWithNestedMultiPartForms();
        getTester().submitForm("panel:form");
        verifyCodeAndCodeClassCalls(2);
        verify(newsletterServiceMock, times(2)).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void assertRequiredFields() {
        String b = "panel:form:";
        getTester().startComponentInPage(makePanel());

        getTester().assertRequired(b + "number");
        getTester().assertRequired(b + "authors");
        getTester().assertRequired(b + "firstAuthor");
        getTester().assertRequired(b + "title");
        getTester().assertRequired(b + "location");
        getTester().assertRequired(b + "publicationYear");
        getTester().assertRequired(b + "tabs:panelsContainer:panels:1:tab1Form:goals");

        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void firstAuthorChangeBehavior_withoutTriggering_hasFirstAuthoerOverriddenFalseAndFirstAuthorDisabled() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:";

        getTester().assertModelValue(formId + "firstAuthorOverridden", false);
        getTester().assertDisabled(formId + "firstAuthor");

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("authors")).isEqualTo("a");
        assertThat(formTester.getTextComponentValue("firstAuthor")).isEqualTo("fa");

        verifyCodeAndCodeClassCalls(2);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void firstAuthorChangeBehavior_withoutUpdatedAuthor_hasFirstAuthorOverriddenFalseAndFirstAuthorDisabled() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:";

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("authors")).isEqualTo("a");
        assertThat(formTester.getTextComponentValue("firstAuthor")).isEqualTo("fa");

        formTester.setValue("authors", "Darwin C.");
        getTester().executeAjaxEvent(formId + "authors", "change");

        assertThat(formTester.getTextComponentValue("authors")).isEqualTo("Darwin C.");
        assertThat(formTester.getTextComponentValue("firstAuthor")).isEqualTo("Darwin");

        getTester().assertComponentOnAjaxResponse(formId + "firstAuthor");

        verifyCodeAndCodeClassCalls(2);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void mainCodeOfCodeClass1ChangeBehavior_whenChangingCodesClass1_reflectsInMainCodeOfCodeClass() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:tabs:panelsContainer:panels:5:tab3Form:";
        getTester().assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1");
        getTester().assertModelValue(formId + "codesClass1", Collections.singletonList(newC(1, "F")));

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("mcocc1");

        getTester().executeAjaxEvent(formId + "codesClass1", "change");

        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1F");

        verifyCodeAndCodeClassCalls(2, 3);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void mainCodeOfCodeClass1ChangeBehavior_whenRemovingCodeOfClass1_clearsMainCodeOfCodeClass() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:tabs:panelsContainer:panels:5:tab3Form:";
        getTester().assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1");
        getTester().assertModelValue(formId + "codesClass1", Collections.singletonList(newC(1, "F")));

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("mcocc1");

        int[] indices = new int[2];
        indices[0] = 2;
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");

        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("");

        verifyCodeAndCodeClassCalls(2, 3);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void mainCodeOfCodeClass1ChangeBehavior_walkThroughStateChanges() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:tabs:panelsContainer:panels:5:tab3Form:";
        getTester().assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1");
        getTester().assertModelValue(formId + "codesClass1", Collections.singletonList(newC(1, "F")));

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("mcocc1");

        // first choice selected -> keep mainCode as is
        int[] indices = new int[2];
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1F");

        // add additional code to code class1 -> leave mainCodeOfCodeClass as is
        indices[1] = 1;
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1F");

        // only select the second option in codesOfCodeClass1 -> adjusts
        // mainCodeOfCodeClass1 accordingly
        indices = new int[1];
        indices[0] = 1;
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1G");

        // clear all codes of codeClass1 -> clear mainCodeOfCodeClass1
        indices = new int[0];
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("");

        getTester().assertComponentOnAjaxResponse(formId + "mainCodeOfCodeclass1");

        verifyCodeAndCodeClassCalls(2, 5);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withNoPmIdInScipamatoPaper_warns() {
        getTester().startComponentInPage(makePanel());

        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.empty());

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertErrorMessages("Could not retrieve an article with PMID " + PMID + " from PubMed.");

        verify(pubmedArticleServiceMock).getPubmedArticleWithPmid(PMID);

        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetAndEqual_informsAboutPerfectMatch() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages("All compared fields are matching those in PubMed.");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING));
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    private void fixPubmedRetrievalButtonClicked(String a, String fa, String t, String l, String py, String doi,
        String oa) {
        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.of(pubmedArticleMock));
        when(pubmedArticleMock.getPmId()).thenReturn(String.valueOf(PMID));
        when(pubmedArticleMock.getAuthors()).thenReturn(a);
        when(pubmedArticleMock.getFirstAuthor()).thenReturn(fa);
        when(pubmedArticleMock.getTitle()).thenReturn(t);
        when(pubmedArticleMock.getLocation()).thenReturn(l);
        when(pubmedArticleMock.getPublicationYear()).thenReturn(py);
        when(pubmedArticleMock.getDoi()).thenReturn(doi);
        when(pubmedArticleMock.getOriginalAbstract()).thenReturn(oa);
    }

    private void verifyPubmedRetrievalButtonClicked() {
        verify(pubmedArticleServiceMock).getPubmedArticleWithPmid(PMID);
        verify(pubmedArticleMock).getPmId();
        verify(pubmedArticleMock).getAuthors();
        verify(pubmedArticleMock).getFirstAuthor();
        verify(pubmedArticleMock).getTitle();
        verify(pubmedArticleMock).getLocation();
        verify(pubmedArticleMock).getPublicationYear();
        verify(pubmedArticleMock).getDoi();
        verify(pubmedArticleMock).getOriginalAbstract();

        verifyCodeAndCodeClassCalls(1);

        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptArticle_warnsAboutDifferingArticle() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("_a", "fa", "t", "l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Authors: _a");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptFirstAuthor_warnsAboutDifferingFirstAuthor() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "_fa", "t", "l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed First Author: _fa");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptTitle_warnsAboutDifferingTitle() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "fa", "_t", "l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Title: _t");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptLocation_warnsAboutDifferingLocation() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "_l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Location: _l");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptYear_warnsAboutDifferingYear() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2016", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Pub. Year: 2016");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptDoi_warnsAboutDifferingDoi() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "_doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed DOI: _doi");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptAbstract_doesNotWarnAboutAbstract() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "_oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING));
        getTester().assertInfoMessages("All compared fields are matching those in PubMed.");
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetAndAllDifferent_warnsAboutAllComparedFields() {
        getTester().startComponentInPage(makePanel());

        fixPubmedRetrievalButtonClicked("_a", "_fa", "_t", "_l", "2016", "_doi", "_oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Authors: _a", "PubMed First Author: _fa", "PubMed Title: _t", "PubMed Pub. Year: 2016",
            "PubMed Location: _l", "PubMed DOI: _doi");
        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void withNoPmId_PubmedRetrievalLinkIsNotEnabled() {
        getTester().startComponentInPage(makePanelWithEmptyPaper(null));
        getTester().assertDisabled(PANEL_ID + ":form:pubmedRetrieval");
        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithNoOtherValuesSet_setsThemFromPubmedIncludingOriginalAbstract() {
        getTester().startComponentInPage(makePanelWithEmptyPaper(PMID));

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages(
            "Some fields have changed (Authors, First Author, Title, Pub. Year, Location, DOI, Original Abstract). Click save if you want to keep the changes.");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING));
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    private void testAndVerifySingleFieldSet(EditablePaperPanel panel, String field) {
        getTester().startComponentInPage(panel);

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages(
            "Some fields have changed (" + field + "). Click save if you want to keep the changes.");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING));
        getTester().assertNoErrorMessage();

        verifyPubmedRetrievalButtonClicked();
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptAuthor_setsAuthor() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setAuthors(null);
        testAndVerifySingleFieldSet(panel, "Authors");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptFirstAuthor_setsFirstAuthor() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setFirstAuthor(null);
        testAndVerifySingleFieldSet(panel, "First Author");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptTitle_setsTitle() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setTitle(null);
        testAndVerifySingleFieldSet(panel, "Title");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptLocation_setsLocation() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setLocation(null);
        testAndVerifySingleFieldSet(panel, "Location");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptYear_setsYear() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setPublicationYear(null);
        testAndVerifySingleFieldSet(panel, "Pub. Year");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptDoi_setsDoi() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setDoi(null);
        testAndVerifySingleFieldSet(panel, "DOI");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptOriginalAbstract_setsOriginalAbstract() {
        EditablePaperPanel panel = makePanel();
        panel
            .getModelObject()
            .setOriginalAbstract(null);
        testAndVerifySingleFieldSet(panel, "Original Abstract");
    }

    @Test
    public void clickingOnPubmedRetrievalButton_withMatchingPmId_andWithNoOtherValuesSet_butWithInvalidYear_warnsAboutYearButSetsOtherFields() {
        getTester().startComponentInPage(makePanelWithEmptyPaper(PMID));

        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "invalid", "doi", "oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages(
            "Some fields have changed (Authors, First Author, Title, Location, DOI, Original Abstract). Click save if you want to keep the changes.");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING));
        getTester().assertErrorMessages("Unable to parse the year 'invalid'");

        verifyPubmedRetrievalButtonClicked();
    }

    private EditablePaperPanel makePanelWithEmptyPaper(Integer pmId) {
        Paper p = new Paper();
        p.setPmId(pmId);
        return new EditablePaperPanel(PANEL_ID, Model.of(p), null, null, SHOW_EXCLUDED, Mode.EDIT) {
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
    private EditablePaperPanel makePanelWith(Integer pmId, PageReference callingPage, Long searchOrderId,
        boolean showExcluded) {
        Paper p = new Paper();
        p.setId(1L);
        p.setPmId(pmId);
        return new EditablePaperPanel(PANEL_ID, Model.of(p), callingPage, searchOrderId, showExcluded, Mode.EDIT) {
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

    @Test
    public void withNoCallingPage_hasInvisibleBackButton() {
        getTester().startComponentInPage(makePanelWith(PMID, null, SEARCH_ORDER_ID, SHOW_EXCLUDED));

        getTester().assertInvisible(PANEL_ID + ":form:back");

        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void withNoSearchOrderId_hasInvisibleExcludeButton() {
        getTester().startComponentInPage(makePanelWith(PMID, callingPageMock, null, SHOW_EXCLUDED));

        getTester().assertInvisible(PANEL_ID + ":form:exclude");

        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingExclude_withBothSearchOrderIdAndPaperId_excludesPaperFromSearchOrder_andForwardsToPaperSearchPage() {
        getTester().startComponentInPage(makePanelWith(PMID, callingPageMock, SEARCH_ORDER_ID, false));
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form");

        formTester.submit("exclude");
        getTester().assertRenderedPage(PaperSearchPage.class);

        verify(paperServiceMock).excludeFromSearchOrder(SEARCH_ORDER_ID, 1L);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verifyCodeAndCodeClassCalls(2);
    }

    @Test
    public void clickingExclude_showingExcluded_reincludesPaperIntoSearchOrder_andForwardsToPaperSearchPage() {
        getTester().startComponentInPage(makePanelWith(PMID, callingPageMock, SEARCH_ORDER_ID, true));
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form");

        formTester.submit("exclude");
        getTester().assertRenderedPage(PaperSearchPage.class); // TODO consider not forwarding

        verify(paperServiceMock).reincludeIntoSearchOrder(SEARCH_ORDER_ID, 1L);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
        verifyCodeAndCodeClassCalls(2);
    }

    @Test
    public void startingPage_notShowingExclusions_adjustsIconAndTitleOfToggleInclusionsButton() {
        assertExcluded(false, "Exclude paper from current search", "glyphicon-ban-circle");
    }

    private void assertExcluded(boolean showingExclusion, String titleValue, String iconValue) {
        getTester().startComponentInPage(makePanelWith(PMID, callingPageMock, SEARCH_ORDER_ID, showingExclusion));

        String responseTxt = getTester()
            .getLastResponse()
            .getDocument();
        TagTester tagTester = TagTester.createTagByAttribute(responseTxt, "title", titleValue);
        assertThat(tagTester).isNotNull();
        assertThat(tagTester.getName()).isEqualTo("button");
        assertThat(tagTester.getValue()).contains("<i class=\"glyphicon " + iconValue + "\"></i>");
        verifyCodeAndCodeClassCalls(1);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void startingPageShowingExclusions_adjustsIconAndTitleOfToggleInclusionsButton() {
        assertExcluded(true, "Re-include paper into current search", "glyphicon-ok-circle");
    }

    @Test
    public void isAssociatedWithNewsletter_withNoNewsletterLink() {
        EditablePaperPanel p = makePanel();
        assertThat(p.isAssociatedWithNewsletter()).isFalse();
    }

    @Test
    public void isAssociatedWithNewsletter_withNewsletterLink() {
        EditablePaperPanel p = makePanel();
        p
            .getModelObject()
            .setNewsletterLink(new Paper.NewsletterLink(1, "i1", 1, 1, "t1", "hl"));
        assertThat(p.isAssociatedWithNewsletter()).isTrue();
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

}
