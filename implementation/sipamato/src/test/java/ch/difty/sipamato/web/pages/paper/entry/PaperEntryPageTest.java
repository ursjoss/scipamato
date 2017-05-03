package ch.difty.sipamato.web.pages.paper.entry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParserFactory;
import ch.difty.sipamato.service.CodeClassService;
import ch.difty.sipamato.service.CodeService;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.pages.SelfUpdatingPageTest;
import ch.difty.sipamato.web.panel.paper.PaperPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;

public class PaperEntryPageTest extends SelfUpdatingPageTest<PaperEntryPage> {

    @MockBean
    private PaperService serviceMock;

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
        return new PaperEntryPage(Model.of(new Paper()));
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
        assertTabPanelFields(2, 3, b, Paper.POPULATION_PLACE, Paper.POPULATION_PARTICIPANTS, Paper.POPULATION_DURATION, Paper.EXPOSURE_POLLUTANT, Paper.EXPOSURE_ASSESSMENT, Paper.METHOD_STUDY_DESIGN,
                Paper.METHOD_OUTCOME, Paper.METHOD_STATISTICS, Paper.METHOD_CONFOUNDERS, Paper.RESULT_EXPOSURE_RANGE, Paper.RESULT_EFFECT_ESTIMATE, Paper.RESULT_MEASURED_OUTCOME);
        assertTabPanelFields(3, 5, b, Paper.RESULT, Paper.COMMENT, Paper.INTERN, Paper.RESULT_EXPOSURE_RANGE, Paper.RESULT_EFFECT_ESTIMATE, Paper.RESULT_MEASURED_OUTCOME);
        assertTabPanelFieldsOfTab4(7, b, Paper.MAIN_CODE_OF_CODECLASS1, "codesClass1", "codesClass2", "codesClass3", "codesClass4", "codesClass5", "codesClass6", "codesClass7", "codesClass8");
        assertTabPanelFields(5, 9, b, Paper.ORIGINAL_ABSTRACT);
    }

    private void assertTabPanelFields(int tabId, int panelId, String b, String... fields) {
        assertTabPanel(panelId, b);
        final String bb = b + ":" + panelId + ":tab" + tabId + "Form";
        getTester().assertComponent(bb, Form.class);
        for (String f : fields) {
            assertLabeledTextArea(bb, f);
        }
    }

    private void assertTabPanelFieldsOfTab4(int panelId, String b, String... fields) {
        assertTabPanel(panelId, b);
        final String bb = b + ":" + panelId + ":tab4Form";
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
        when(serviceMock.saveOrUpdate(isA(Paper.class))).thenReturn(persistedPaperMock);

        getTester().startPage(makePage());
        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();
        verify(serviceMock).saveOrUpdate(isA(Paper.class));
    }

    @Test
    public void paperFailingValidation_showsAllValidationMessages() {
        getTester().startPage(makePage());
        getTester().submitForm("contentPanel:form");
        getTester().assertErrorMessages("'Authors' is required.", "'Title' is required.", "'Location' is required.", "'Pub. Year' is required.", "'No.' is required.", "'Goals' is required.");
    }

    @Test
    public void serviceThrowingError() {
        when(serviceMock.saveOrUpdate(isA(Paper.class))).thenThrow(new RuntimeException("foo"));

        getTester().startPage(makePage());

        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertErrorMessages("An unexpected error occurred when trying to save Paper [id 0]: foo");
        verify(serviceMock).saveOrUpdate(isA(Paper.class));
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
    public void serviceReturningNullPaperAfterSave_hasErrorMessage() {
        when(serviceMock.saveOrUpdate(isA(Paper.class))).thenReturn(null);

        getTester().startPage(makePage());
        FormTester formTester = makeSaveablePaperTester();
        formTester.submit();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save Paper [id 0]: ");

        verify(serviceMock).saveOrUpdate(isA(Paper.class));
    }

    @Test
    public void defaultModel_containsNaValuesAndCanSubmitWithoutErrors() {
        when(serviceMock.saveOrUpdate(isA(Paper.class))).thenReturn(persistedPaperMock);

        getTester().startPage(new PaperEntryPage(new PageParameters()));

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
        verify(serviceMock).saveOrUpdate(isA(Paper.class));
    }

}
