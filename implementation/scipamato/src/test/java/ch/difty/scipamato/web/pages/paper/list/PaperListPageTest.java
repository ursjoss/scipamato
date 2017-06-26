package ch.difty.scipamato.web.pages.paper.list;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.paging.PaginationRequest;
import ch.difty.scipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.scipamato.service.CodeClassService;
import ch.difty.scipamato.service.CodeService;
import ch.difty.scipamato.service.DefaultServiceResult;
import ch.difty.scipamato.service.Localization;
import ch.difty.scipamato.service.PaperService;
import ch.difty.scipamato.service.PubmedImporter;
import ch.difty.scipamato.service.ServiceResult;
import ch.difty.scipamato.web.pages.BasePageTest;
import ch.difty.scipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.scipamato.web.panel.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperListPageTest extends BasePageTest<PaperListPage> {

    @MockBean
    private PaperService paperServiceMock;
    @MockBean
    private PubmedImporter pubmedImportService;

    @MockBean
    private CodeService codeServiceMock;
    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private ApplicationProperties applicationPropertiesMock;
    @MockBean
    private Localization localizationMock;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(paperSlimRepoMock, codeServiceMock, codeClassServiceMock, paperServiceMock, pubmedImportService);
    }

    @Override
    protected PaperListPage makePage() {
        return new PaperListPage(null);
    }

    @Override
    protected Class<PaperListPage> getPageClass() {
        return PaperListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchForm("searchForm");
        assertPateModal("xmlPasteModal");
        assertResultPanel("resultPanel");

        verify(paperSlimRepoMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(Mockito.isA(PaperFilter.class), Mockito.isA(PaginationRequest.class));
    }

    private void assertSearchForm(String b) {
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "number");
        assertLabeledTextField(b, "authorsSearch");
        assertLabeledTextField(b, "methodsSearch");
        assertLabeledTextField(b, "fieldSearch");
        assertLabeledTextField(b, "pubYearFrom");
        assertLabeledTextField(b, "pubYearUntil");

        getTester().assertComponent(b + ":newPaper", BootstrapAjaxButton.class);
        getTester().assertComponent(b + ":showXmlPasteModalLink", BootstrapAjaxLink.class);
    }

    private void assertPateModal(String id) {
        getTester().assertComponent(id, ModalWindow.class);
        getTester().assertInvisible(id + ":content");
    }

    private void assertResultPanel(String b) {
        getTester().assertComponent(b, ResultPanel.class);
        getTester().assertComponent(b + ":table", BootstrapDefaultDataTable.class);
    }

    @Test
    public void clickingNewPaper_forwardsToPaperEntryPage() {
        final long minimumNumber = 7;
        final long freeNumber = 21;
        when(applicationPropertiesMock.getMinimumPaperNumberToBeRecycled()).thenReturn(minimumNumber);
        when(paperServiceMock.findLowestFreeNumberStartingFrom(minimumNumber)).thenReturn(freeNumber);
        when(localizationMock.getLocalization()).thenReturn("de");

        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchForm");
        formTester.submit("newPaper");

        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperSlimRepoMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
        verify(paperServiceMock).findLowestFreeNumberStartingFrom(minimumNumber);
        // from PaperEntryPage
        verify(codeClassServiceMock).find("de");
        for (CodeClassId ccid : CodeClassId.values())
            verify(codeServiceMock).findCodesOfClass(ccid, "de");
        verify(localizationMock, times(9)).getLocalization();
        verify(paperServiceMock, times(3)).findPageOfIdsByFilter(Mockito.isA(PaperFilter.class), Mockito.isA(PaginationRequest.class));
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_opensModalWindow() {
        getTester().startPage(getPageClass());

        String b = "searchForm:";
        getTester().executeAjaxEvent("searchForm:showXmlPasteModalLink", "click");

        b = "xmlPasteModal";
        getTester().assertComponent(b, ModalWindow.class);
        b += ":content";
        getTester().isVisible(b);
        getTester().assertComponent(b, XmlPasteModalPanel.class);
        b += ":form";
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":content", TextArea.class);
        getTester().assertComponent(b + ":submit", BootstrapAjaxButton.class);

        verify(paperSlimRepoMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(Mockito.isA(PaperFilter.class), Mockito.isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose() {
        AjaxRequestTarget targetMock = mock(AjaxRequestTarget.class);
        ServiceResult serviceResult = new DefaultServiceResult();
        serviceResult.addInfoMessage("info");
        serviceResult.addWarnMessage("warn");
        serviceResult.addErrorMessage("error");
        when(pubmedImportService.persistPubmedArticlesFromXml("content")).thenReturn(serviceResult);

        makePage().onXmlPasteModalPanelClose("content", targetMock);

        verify(pubmedImportService).persistPubmedArticlesFromXml("content");
        verify(paperSlimRepoMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(Mockito.isA(PaperFilter.class), Mockito.isA(PaginationRequest.class));
    }
}
