package ch.difty.scipamato.web.pages.paper.list;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.filter.PaperFilter;
import ch.difty.scipamato.paging.PaginationRequest;
import ch.difty.scipamato.persistence.CodeClassService;
import ch.difty.scipamato.persistence.CodeService;
import ch.difty.scipamato.persistence.DefaultServiceResult;
import ch.difty.scipamato.persistence.PubmedImporter;
import ch.difty.scipamato.persistence.ServiceResult;
import ch.difty.scipamato.web.pages.BasePageTest;
import ch.difty.scipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.scipamato.web.panel.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.web.panel.result.ResultPanel;
import ch.difty.scipamato.config.ApplicationProperties;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperListPageTest extends BasePageTest<PaperListPage> {

    private static final String LC = "en_us";

    @MockBean
    private PubmedImporter pubmedImportService;

    @MockBean
    private CodeService codeServiceMock;
    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private ApplicationProperties applicationPropertiesMock;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(paperSlimServiceMock, paperServiceMock, codeServiceMock, codeClassServiceMock, paperServiceMock, pubmedImportService);
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

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
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

        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchForm");
        formTester.submit("newPaper");

        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
        verify(paperServiceMock).findLowestFreeNumberStartingFrom(minimumNumber);
        // from PaperEntryPage
        verify(codeClassServiceMock).find(LC);
        for (CodeClassId ccid : CodeClassId.values())
            verify(codeServiceMock).findCodesOfClass(ccid, LC);
        verify(paperServiceMock, times(3)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
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

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withNullContent_doesNotPersists() {
        String content = null;
        makePage().onXmlPasteModalPanelClose(content, mock(AjaxRequestTarget.class));

        verify(pubmedImportService, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withBlankContent_doesNotPersists() {
        String content = "";
        makePage().onXmlPasteModalPanelClose(content, mock(AjaxRequestTarget.class));

        verify(pubmedImportService, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withContent_persistsArticles() {
        String content = "content";
        when(pubmedImportService.persistPubmedArticlesFromXml(content)).thenReturn(makeServiceResult());

        makePage().onXmlPasteModalPanelClose("content", mock(AjaxRequestTarget.class));

        verify(pubmedImportService).persistPubmedArticlesFromXml(content);
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    private ServiceResult makeServiceResult() {
        ServiceResult serviceResult = new DefaultServiceResult();
        serviceResult.addInfoMessage("info");
        serviceResult.addWarnMessage("warn");
        serviceResult.addErrorMessage("error");
        return serviceResult;
    }
}
