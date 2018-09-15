package ch.difty.scipamato.core.web.paper.list;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.logic.parsing.AuthorParserFactory;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.persistence.DefaultServiceResult;
import ch.difty.scipamato.core.persistence.ServiceResult;
import ch.difty.scipamato.core.pubmed.PubmedImporter;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.common.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;
import ch.difty.scipamato.core.web.security.TestUserDetailsService;

@SuppressWarnings("SameParameterValue")
public class PaperListPageTest extends BasePageTest<PaperListPage> {

    private static final String LC = "en_us";

    @MockBean
    private PubmedImporter            pubmedImportService;
    @MockBean
    private CodeService               codeServiceMock;
    @MockBean
    private CodeClassService          codeClassServiceMock;
    @MockBean
    private ApplicationCoreProperties applicationPropertiesMock;

    @MockBean
    private AuthorParserFactory authorParserFactoryMock;

    @Override
    protected void setUpHook() {
        when(applicationPropertiesMock.getBrand()).thenReturn("scipamato");
    }

    @Override
    protected String getUserName() {
        return TestUserDetailsService.USER_ADMIN;
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
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
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

        // Note: Only one menu is open - we're unable to see the other submenu items without clicking.
        assertTopLevelMenu(0, "Left", "Papers");
        assertNestedMenu(0, 0, "Left", "Papers");
        assertNestedMenu(0, 1, "Left", "Search");
        // TODO check how to get the test evaluate with user admin - showing all menu entries
        // assertTopLevelMenu(1, "Left", "Newsletters");
        // assertTopLevelMenu(2, "Left", "Synchronize");

        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/");
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc");
        assertPageLinkButton(2, "Right", "Logout");
    }

    private void assertPageLinkButton(int index, String position, String expectedLabelText) {
        String path = "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + index
                      + ":component";
        getTester().assertComponent(path, NavbarButton.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }

    private void assertTopLevelMenu(int index, String position, String expectedLabelText) {
        String path = "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + index
                      + ":component:btn";
        getTester().assertComponent(path, WebMarkupContainer.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }

    private void assertNestedMenu(int topLevelIndex, int menuIndex, String position, String expectedLabelText) {
        String path =
            "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + topLevelIndex
            + ":component:dropdown-menu:buttons:" + menuIndex + ":button";
        getTester().assertComponent(path, MenuBookmarkablePageLink.class);
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
        verify(paperServiceMock, times(5)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_opensModalWindow() {
        getTester().startPage(getPageClass());

        String b = "searchForm";
        getTester().executeAjaxEvent(b + ":showXmlPasteModalLink", "click");

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
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withNullContent_doesNotPersists() {
        makePage().onXmlPasteModalPanelClose(null, mock(AjaxRequestTarget.class));

        verify(pubmedImportService, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void onXmlPasteModalPanelClose_withBlankContent_doesNotPersists() {
        String content = "";
        makePage().onXmlPasteModalPanelClose(content, mock(AjaxRequestTarget.class));

        verify(pubmedImportService, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
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
        verify(paperServiceMock, times(5)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    private ServiceResult makeServiceResult() {
        ServiceResult serviceResult = new DefaultServiceResult();
        serviceResult.addInfoMessage("info");
        serviceResult.addWarnMessage("warn");
        serviceResult.addErrorMessage("error");
        return serviceResult;
    }

    @Test
    public void clickingOnResultTitle_forwardsToPaperEntryPage() {
        final List<PaperSlim> list = new ArrayList<>();
        long number = 10L;
        list.add(new PaperSlim(1L, number, "author", 2018, "title"));
        when(paperSlimServiceMock.countByFilter(isA(PaperFilter.class))).thenReturn(list.size());
        when(paperSlimServiceMock.findPageByFilter(isA(PaperFilter.class), isA(PaginationRequest.class))).thenReturn(
            list);

        getTester().startPage(getPageClass());

        getTester().clickLink("resultPanel:table:body:rows:1:cells:5:cell:link");
        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperSlimServiceMock).findPageByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
        verify(paperServiceMock).findByNumber(number, LC);
        verify(pubmedImportService, never()).persistPubmedArticlesFromXml(anyString());
        // from PaperEntryPage
        verify(codeClassServiceMock).find(LC);
        for (CodeClassId ccid : CodeClassId.values())
            verify(codeServiceMock).findCodesOfClass(ccid, LC);
    }
}
