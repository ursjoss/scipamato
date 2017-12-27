package ch.difty.scipamato.core.web.pages.paper.list;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.filter.PaperFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.persistence.DefaultServiceResult;
import ch.difty.scipamato.core.persistence.ServiceResult;
import ch.difty.scipamato.core.pubmed.PubmedImporter;
import ch.difty.scipamato.core.web.pages.BasePageTest;
import ch.difty.scipamato.core.web.pages.paper.entry.PaperEntryPage;
import ch.difty.scipamato.core.web.panel.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.core.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperListPageTest extends BasePageTest<PaperListPage> {

    private static final String LC = "en_us";

    @MockBean
    private PubmedImporter pubmedImportService;

    @MockBean
    private CodeService           codeServiceMock;
    @MockBean
    private CodeClassService      codeClassServiceMock;
    @MockBean
    private ApplicationProperties applicationPropertiesMock;

    @Override
    protected void setUpHook() {
        when(applicationPropertiesMock.getBrand()).thenReturn("scipamato");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(paperSlimServiceMock, paperServiceMock, codeServiceMock, codeClassServiceMock,
            paperServiceMock, pubmedImportService);
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

        assertMenuEntries();

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

    private void assertMenuEntries() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);
        getTester().assertComponent("navbar", Navbar.class);
        assertPageLinkButton(0, "Left", NavbarButton.class, "Papers");
        assertPageLinkButton(1, "Left", NavbarButton.class, "Search");
        assertPageLinkButton(2, "Left", NavbarButton.class, "Synchronize");

        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/");
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc");
        assertPageLinkButton(2, "Right", NavbarButton.class, "Logout");
    }

    private void assertPageLinkButton(int index, String position, Class<? extends Component> expectedComponentClass,
            String expectedLabelText) {
        String path = "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + index
                + ":component";
        getTester().assertComponent(path, NavbarButton.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }

    private void assertExternalLink(final String path, final String link) {
        getTester().assertComponent(path, NavbarExternalLink.class);
        getTester().assertModelValue(path, link);
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
        verify(paperServiceMock, times(3)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withBlankContent_doesNotPersists() {
        String content = "";
        makePage().onXmlPasteModalPanelClose(content, mock(AjaxRequestTarget.class));

        verify(pubmedImportService, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(3)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withContent_persistsArticlesAndUpdatesNavigateable() {
        String content = "content";
        when(pubmedImportService.persistPubmedArticlesFromXml(content)).thenReturn(makeServiceResult());

        makePage().onXmlPasteModalPanelClose("content", mock(AjaxRequestTarget.class));

        verify(pubmedImportService).persistPubmedArticlesFromXml(content);
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        // The third call to findPageOfIds... is to update the Navigateable, the fourth
        // one because of the page redirect
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    private ServiceResult makeServiceResult() {
        ServiceResult serviceResult = new DefaultServiceResult();
        serviceResult.addInfoMessage("info");
        serviceResult.addWarnMessage("warn");
        serviceResult.addErrorMessage("error");
        return serviceResult;
    }
}
