package ch.difty.sipamato.web.panel.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.service.PubmedArticleService;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;

public class EditablePaperPanelTest extends PaperPanelTest<Paper, EditablePaperPanel> {

    private static final int PMID = 1234;

    @MockBean
    public PubmedArticleService pubmedArticleServiceMock;

    @Mock
    public PubmedArticleFacade pubmedArticleMock;

    @Override
    protected void tearDownLocalHook() {
        verifyNoMoreInteractions(pubmedArticleServiceMock, pubmedArticleMock);
    }

    @Override
    protected EditablePaperPanel makePanel() {
        Paper p = new Paper();
        p.setId(1l);
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

        return new EditablePaperPanel("panel", Model.of(p)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                // no-op
            }

        };
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "panel";
        getTester().assertComponent(b, EditablePaperPanel.class);

        assertCommonComponents(b);

        b += ":form";
        assertTextFieldWithLabel(b + ":id", 1l, "ID");
        assertTextFieldWithLabel(b + ":publicationYear", 2017, "Pub. Year");
        assertTextFieldWithLabel(b + ":pmId", 1234, "PMID");
        getTester().assertLabel(b + ":submit:label", "Save");
        assertTextFieldWithLabel(b + ":createdDisplayValue", "u1 (2017-02-01 13:34:45)", "Created");
        assertTextFieldWithLabel(b + ":modifiedDisplayValue", "u2 (2017-03-01 13:34:45)", "Last Modified");

        getTester().assertComponent(b + ":pubmedRetrieval", BootstrapAjaxLink.class);
        getTester().assertVisible(b + ":pubmedRetrieval");
    }

    @Test
    public void specificFields_areDisabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isDisabled("panel:form:id");
        getTester().isDisabled("panel:form:firstAuthorOverridden");
        getTester().isDisabled("panel:form:createdDisplayValue");
        getTester().isDisabled("panel:form:modifiedDisplayValue");
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void assertSummaryLink() {
        getTester().startComponentInPage(makePanel());
        getTester().assertComponent("panel:form:summaryLink", ResourceLink.class);
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void assertSubmit() {
        getTester().startComponentInPage(makePanel());
        getTester().submitForm("panel:form");
        verifyCodeAndCodeClassCalls(2);
    }

    @Test
    public void assertRequiredFields() {
        String b = "panel:form:";
        getTester().startComponentInPage(makePanel());

        getTester().assertRequired(b + "authors");
        getTester().assertRequired(b + "firstAuthor");
        getTester().assertRequired(b + "title");
        getTester().assertRequired(b + "location");
        getTester().assertRequired(b + "publicationYear");
        getTester().assertRequired(b + "tabs:panelsContainer:panels:1:tab1Form:goals");

        verifyCodeAndCodeClassCalls(1);
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
    }

    @Test
    public void firstAuthorChangeBehavior_withoutUpdatedAuthor_hasFirstAuthoerOverriddenFalseAndFirstAuthorDisabled() {
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
    }

    @Test
    public void mainCodeOfCodeClass1ChangeBehavior_whenChangingCodesClass1_reflectsInMainCodeOfCodeClass() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:tabs:panelsContainer:panels:5:tab3Form:";
        getTester().assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1");
        getTester().assertModelValue(formId + "codesClass1", Arrays.asList(newC(1, "F")));

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("mcocc1");

        getTester().executeAjaxEvent(formId + "codesClass1", "change");

        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1F");

        verifyCodeAndCodeClassCalls(2, 3);
    }

    @Test
    public void mainCodeOfCodeClass1ChangeBehavior_whenRemovingCodeOfClass1_clearsMainCodeOfCodeClass() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:tabs:panelsContainer:panels:5:tab3Form:";
        getTester().assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1");
        getTester().assertModelValue(formId + "codesClass1", Arrays.asList(newC(1, "F")));

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("mcocc1");

        int[] indices = new int[2];
        indices[0] = 2;
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");

        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("");

        verifyCodeAndCodeClassCalls(2, 3);
    }

    @Test
    public void mainCodeOfCodeClass1ChangeBehavior_walkThroughStateChanges() {
        getTester().startComponentInPage(makePanel());

        String formId = "panel:form:tabs:panelsContainer:panels:5:tab3Form:";
        getTester().assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1");
        getTester().assertModelValue(formId + "codesClass1", Arrays.asList(newC(1, "F")));

        FormTester formTester = getTester().newFormTester(formId);
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("mcocc1");

        // first choice selected -> keep mainCode as is
        int[] indices = new int[2];
        indices[0] = 0;
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1F");

        // add additional code to code class1 -> leave mainCodeOfCodeClass as is
        indices[1] = 1;
        formTester.selectMultiple("codesClass1", indices, true);
        getTester().executeAjaxEvent(formId + "codesClass1", "change");
        assertThat(formTester.getTextComponentValue("mainCodeOfCodeclass1")).isEqualTo("1F");

        // only select the second option in codesOfCodeClass1 -> adjusts mainCodeOfCodeClass1 accordingly
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
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_withNoPmIdInSipamatoPaper_warns() {
        getTester().startComponentInPage(makePanel());
        getTester().debugComponentTrees();

        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.empty());

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertErrorMessages("Could not retrieve an article with PMID " + PMID + " from PubMed.");

        verify(pubmedArticleServiceMock).getPubmedArticleWithPmid(PMID);

        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_withMatchingPmId_andWithAllValuesSetAndEqual_informsAboutPerfectMatch() {
        getTester().startComponentInPage(makePanel());
        getTester().debugComponentTrees();

        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.of(pubmedArticleMock));
        when(pubmedArticleMock.getPmId()).thenReturn(String.valueOf(PMID));
        when(pubmedArticleMock.getAuthors()).thenReturn("a");
        when(pubmedArticleMock.getFirstAuthor()).thenReturn("fa");
        when(pubmedArticleMock.getTitle()).thenReturn("t");
        when(pubmedArticleMock.getLocation()).thenReturn("l");
        when(pubmedArticleMock.getPublicationYear()).thenReturn("2017");
        when(pubmedArticleMock.getDoi()).thenReturn("doi");
        when(pubmedArticleMock.getOriginalAbstract()).thenReturn("oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages("All compared fields are matching those in PubMed.");

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
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_withMatchingPmId_andWithAllValuesSetAndAllDifferent_warnsAboutAllComparedFields() {
        getTester().startComponentInPage(makePanel());
        getTester().debugComponentTrees();

        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.of(pubmedArticleMock));
        when(pubmedArticleMock.getPmId()).thenReturn(String.valueOf(PMID));
        when(pubmedArticleMock.getAuthors()).thenReturn("_a");
        when(pubmedArticleMock.getFirstAuthor()).thenReturn("_fa");
        when(pubmedArticleMock.getTitle()).thenReturn("_t");
        when(pubmedArticleMock.getLocation()).thenReturn("_l");
        when(pubmedArticleMock.getPublicationYear()).thenReturn("_2017");
        when(pubmedArticleMock.getDoi()).thenReturn("_doi");
        when(pubmedArticleMock.getOriginalAbstract()).thenReturn("_oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed Authors: _a", "PubMed First Author: _fa", "PubMed Title: _t",
                "PubMed Pub. Year: _2017", "PubMed Location: _l", "PubMed DOI: _doi");

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
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_withNullPmId_warnsAboutNotBeingAbleToRetrieveArticle() {
        getTester().startComponentInPage(makePanelWithEmptyPaper(null));
        getTester().debugComponentTrees();

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertErrorMessages("Cannot retrieve the PubMed article without first specifying the PMID.");

        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_withMatchingPmId_andWithNoOtherValuesSet_setsThemFromPubmedIncludingOriginalAbstract() {
        getTester().startComponentInPage(makePanelWithEmptyPaper(PMID));
        getTester().debugComponentTrees();

        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.of(pubmedArticleMock));
        when(pubmedArticleMock.getPmId()).thenReturn(String.valueOf(PMID));
        when(pubmedArticleMock.getAuthors()).thenReturn("a");
        when(pubmedArticleMock.getFirstAuthor()).thenReturn("fa");
        when(pubmedArticleMock.getTitle()).thenReturn("t");
        when(pubmedArticleMock.getLocation()).thenReturn("l");
        when(pubmedArticleMock.getPublicationYear()).thenReturn("2017");
        when(pubmedArticleMock.getDoi()).thenReturn("doi");
        when(pubmedArticleMock.getOriginalAbstract()).thenReturn("oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages("Authors: a", "First Author: fa", "Title: t", "Pub. Year: 2017", "Location: l", "DOI: doi", "Original Abstract: oa",
                "Above fields have changed. Click save if you want to keep the changes.");

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
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_withMatchingPmId_andWithNoOtherValuesSetPutInvalidYear_warnsAboutYearButSetsOtherFields() {
        getTester().startComponentInPage(makePanelWithEmptyPaper(PMID));
        getTester().debugComponentTrees();

        when(pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID)).thenReturn(Optional.of(pubmedArticleMock));
        when(pubmedArticleMock.getPmId()).thenReturn(String.valueOf(PMID));
        when(pubmedArticleMock.getAuthors()).thenReturn("a");
        when(pubmedArticleMock.getFirstAuthor()).thenReturn("fa");
        when(pubmedArticleMock.getTitle()).thenReturn("t");
        when(pubmedArticleMock.getLocation()).thenReturn("l");
        when(pubmedArticleMock.getPublicationYear()).thenReturn("invalid");
        when(pubmedArticleMock.getDoi()).thenReturn("doi");
        when(pubmedArticleMock.getOriginalAbstract()).thenReturn("oa");

        getTester().executeAjaxEvent(PANEL_ID + ":form:pubmedRetrieval", "click");
        getTester().assertInfoMessages("Authors: a", "First Author: fa", "Title: t", "Location: l", "DOI: doi", "Original Abstract: oa",
                "Above fields have changed. Click save if you want to keep the changes.");
        getTester().assertErrorMessages("Unable to parse the year 'invalid'");

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
    }

    protected EditablePaperPanel makePanelWithEmptyPaper(Integer pmId) {
        Paper p = new Paper();
        p.setPmId(pmId);
        return new EditablePaperPanel("panel", Model.of(p)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                // no-op
            }

        };
    }
}
