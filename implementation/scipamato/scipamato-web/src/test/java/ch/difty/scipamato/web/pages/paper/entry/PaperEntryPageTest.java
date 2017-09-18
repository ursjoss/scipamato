package ch.difty.scipamato.web.pages.paper.entry;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.logic.parsing.AuthorParserFactory;
import ch.difty.scipamato.persistence.CodeClassService;
import ch.difty.scipamato.persistence.CodeService;
import ch.difty.scipamato.persistence.OptimisticLockingException;
import ch.difty.scipamato.persistence.OptimisticLockingException.Type;
import ch.difty.scipamato.web.pages.SelfUpdatingPageTest;
import ch.difty.scipamato.web.panel.paper.PaperPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;

public class PaperEntryPageTest extends SelfUpdatingPageTest<PaperEntryPage> {

    @MockBean
    private AuthorParserFactory authorParserFactoryMock;

    @MockBean
    private CodeService codeServiceMock;

    @MockBean
    private CodeClassService codeClassServiceMock;

    @Mock
    private Paper persistedPaperMock;

    @Override
    protected PaperEntryPage makePage() {
        return new PaperEntryPage(Model.of(new Paper()), null);
    }

    @Override
    protected Class<PaperEntryPage> getPageClass() {
        return PaperEntryPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "contentPanel";
        getTester().assertComponent(b, PaperPanel.class);

        b += ":form";
        getTester().assertComponent(b, Form.class);

        assertLabeledTextArea(b, Paper.AUTHORS);
        assertLabeledTextField(b, Paper.FIRST_AUTHOR);
        assertLabeledCheckBoxX(b, Paper.FIRST_AUTHOR_OVERRIDDEN);
        assertLabeledTextArea(b, Paper.TITLE);
        assertLabeledTextField(b, Paper.LOCATION);

        assertLabeledTextField(b, Paper.ID);
        assertLabeledTextField(b, Paper.PUBL_YEAR);
        assertLabeledTextField(b, Paper.PMID);
        assertLabeledTextField(b, Paper.DOI);

        b += ":tabs";
        getTester().assertComponent(b, ClientSideBootstrapTabbedPanel.class);
        b += ":panelsContainer:panels";
        assertTabPanelFields(1, 1, b, Paper.GOALS, Paper.POPULATION, Paper.METHODS, Paper.POPULATION_PLACE, Paper.POPULATION_PARTICIPANTS, Paper.POPULATION_DURATION, Paper.EXPOSURE_POLLUTANT,
                Paper.EXPOSURE_ASSESSMENT, Paper.METHOD_STUDY_DESIGN, Paper.METHOD_OUTCOME, Paper.METHOD_STATISTICS, Paper.METHOD_CONFOUNDERS);
        assertTabPanelFields(2, 3, b, Paper.RESULT, Paper.COMMENT, Paper.INTERN, Paper.RESULT_EXPOSURE_RANGE, Paper.RESULT_EFFECT_ESTIMATE, Paper.RESULT_MEASURED_OUTCOME);
        assertTabPanelFieldsOfTab3(5, b, Paper.MAIN_CODE_OF_CODECLASS1, "codesClass1", "codesClass2", "codesClass3", "codesClass4", "codesClass5", "codesClass6", "codesClass7", "codesClass8");
        assertTabPanelFields(4, 7, b, Paper.POPULATION_PLACE, Paper.POPULATION_PARTICIPANTS, Paper.POPULATION_DURATION, Paper.EXPOSURE_POLLUTANT, Paper.EXPOSURE_ASSESSMENT, Paper.METHOD_STUDY_DESIGN,
                Paper.METHOD_OUTCOME, Paper.METHOD_STATISTICS, Paper.METHOD_CONFOUNDERS, Paper.RESULT_EXPOSURE_RANGE, Paper.RESULT_EFFECT_ESTIMATE, Paper.RESULT_MEASURED_OUTCOME);
        assertTabPanelFields(5, 9, b, Paper.ORIGINAL_ABSTRACT);
        assertTabPanelFields(6, 11, b);
    }

    private void assertTabPanelFields(int tabId, int panelId, String b, String... fields) {
        assertTabPanel(panelId, b);
        final String bb = b + ":" + panelId + ":tab" + tabId + "Form";
        getTester().assertComponent(bb, Form.class);
        for (String f : fields) {
            assertLabeledTextArea(bb, f);
        }
    }

    private void assertTabPanelFieldsOfTab3(int panelId, String b, String... fields) {
        assertTabPanel(panelId, b);
        final String bb = b + ":" + panelId + ":tab3Form";
        getTester().assertComponent(bb, Form.class);
        int fieldCount = 0;
        for (String f : fields) {
            if (fieldCount++ == 0) {
                assertLabeledTextField(bb, f);
            } else {
                assertLabeledMultiSelect(bb, f);
            }
        }
    }

    private void assertTabPanel(int i, String b) {
        final String bb = ":" + i;
        getTester().assertComponent(b + bb, Panel.class);
    }

    @Test
    public void submitting_shouldActuallyKickoffOnSubmitInTest() {
        when(paperServiceMock.saveOrUpdate(isA(Paper.class))).thenReturn(persistedPaperMock);

        getTester().startPage(makePage());
        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();
        verify(paperServiceMock).saveOrUpdate(isA(Paper.class));
    }

    @Test
    public void paperFailingValidation_showsAllValidationMessages() {
        getTester().startPage(makePage());
        applyTestHackWithNstedMultiPartForms();
        getTester().submitForm("contentPanel:form");
        getTester().assertErrorMessages("'Authors' is required.", "'Title' is required.", "'Location' is required.", "'Pub. Year' is required.", "'No.' is required.", "'Goals' is required.");
    }

    // See https://issues.apache.org/jira/browse/WICKET-2790
    private void applyTestHackWithNstedMultiPartForms() {
        MockHttpServletRequest servletRequest = getTester().getRequest();
        servletRequest.setUseMultiPartContentType(true);
    }

    @Test
    public void serviceThrowingError() {
        when(paperServiceMock.saveOrUpdate(isA(Paper.class))).thenThrow(new RuntimeException("foo"));

        getTester().startPage(makePage());

        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertErrorMessages("An unexpected error occurred when trying to save Paper [id 0]: foo");
        verify(paperServiceMock).saveOrUpdate(isA(Paper.class));
    }

    private FormTester makeSaveablePaperTester() {
        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.setValue("number", "100");
        formTester.setValue("authors", "Poe EA.");
        formTester.setValue("title", "Title");
        formTester.setValue("location", "loc");
        formTester.setValue("publicationYear", "2017");
        formTester.setValue("tabs:panelsContainer:panels:1:tab1Form:goals", "goals");
        return formTester;
    }

    @Test
    public void serviceThrowingOptimisticLockingException() {
        when(paperServiceMock.saveOrUpdate(isA(Paper.class))).thenThrow(new OptimisticLockingException("paper", "rcd", Type.UPDATE));

        getTester().startPage(makePage());

        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertErrorMessages("The paper with id 0 has been modified concurrently by another user. Please reload it and apply your changes once more.");
        verify(paperServiceMock).saveOrUpdate(isA(Paper.class));
    }

    @Test
    public void serviceReturningNullPaperAfterSave_hasErrorMessage() {
        when(paperServiceMock.saveOrUpdate(isA(Paper.class))).thenReturn(null);

        getTester().startPage(makePage());
        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save Paper [id 0]: ");

        verify(paperServiceMock).saveOrUpdate(isA(Paper.class));
    }

    @Test
    public void defaultModel_containsNaValuesAndCanSubmitWithoutErrors() {
        when(paperServiceMock.saveOrUpdate(isA(Paper.class))).thenReturn(persistedPaperMock);
        when(paperServiceMock.findLowestFreeNumberStartingFrom(7l)).thenReturn(19l);

        getTester().startPage(new PaperEntryPage(new PageParameters(), null));

        FormTester formTester = getTester().newFormTester("contentPanel:form");

        assertThat(formTester.getTextComponentValue(Paper.NUMBER)).isNotNull();
        assertThat(formTester.getTextComponentValue(Paper.AUTHORS)).isNotNull();
        assertThat(formTester.getTextComponentValue(Paper.FIRST_AUTHOR)).isNotNull();
        assertThat(formTester.getTextComponentValue(Paper.TITLE)).isNotNull();
        assertThat(formTester.getTextComponentValue(Paper.LOCATION)).isNotNull();
        assertThat(formTester.getTextComponentValue(Paper.PUBL_YEAR)).isNotNull();
        assertThat(formTester.getTextComponentValue("tabs:panelsContainer:panels:1:tab1Form:goals")).isNotNull();

        formTester.submit();

        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();
        verify(paperServiceMock).saveOrUpdate(isA(Paper.class));
        verify(paperServiceMock).findLowestFreeNumberStartingFrom(7l);
    }

}
