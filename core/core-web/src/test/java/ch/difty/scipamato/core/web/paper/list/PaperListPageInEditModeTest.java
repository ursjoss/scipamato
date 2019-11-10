package ch.difty.scipamato.core.web.paper.list;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.persistence.DefaultServiceResult;
import ch.difty.scipamato.core.persistence.ServiceResult;
import ch.difty.scipamato.core.web.common.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage;
import ch.difty.scipamato.core.web.security.TestUserDetailsService;

@SuppressWarnings("SameParameterValue")
class PaperListPageInEditModeTest extends PaperListPageTest {

    @Override
    protected String getUserName() {
        return TestUserDetailsService.USER_ADMIN;
    }

    @Override
    protected void setUpHook() {
        super.setUpHook();
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.USER, Roles.ADMIN)).thenReturn(true);
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN)).thenReturn(true);
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchForm("searchForm");
        assertPasteModal("xmlPasteModal");
        assertResultPanel("resultPanel");

        assertMenuEntries();

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Override
    protected void assertSpecificSearchFormComponents(String b) {
        getTester().assertComponent(b + ":newPaper", BootstrapAjaxButton.class);
        getTester().assertComponent(b + ":showXmlPasteModalLink", BootstrapAjaxLink.class);
    }

    private void assertPasteModal(String id) {
        getTester().assertComponent(id, ModalWindow.class);
        getTester().assertInvisible(id + ":content");
    }

    void assertMenuEntries() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);
        getTester().assertComponent("navbar", Navbar.class);

        // Note: Only one menu is open - we're unable to see the other submenu items without clicking.
        String position = "Left";
        int menuIndex = 0;
        int subMenuIndex = 0;

        assertTopLevelMenu(menuIndex, position, "Papers");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Papers");
        assertNestedMenu(menuIndex, subMenuIndex, position, "Search");

        subMenuIndex = 0;
        assertTopLevelMenu(++menuIndex, position, "Newsletters");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Newsletter");
        assertNestedMenu(menuIndex, subMenuIndex, position, "Newsletter Topics");

        subMenuIndex = 0;
        assertTopLevelMenu(++menuIndex, position, "Reference Data");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Keywords");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Codes");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Code Classes");
        assertNestedMenu(menuIndex, subMenuIndex, position, "Sync: Core -&gt; Public");

        subMenuIndex = 0;
        assertTopLevelMenu(++menuIndex, position, "Preferences");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "User Management");
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Profile");
        assertNestedMenu(menuIndex, subMenuIndex, position, "Change Password");

        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/");
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc");
        assertPageLinkButton(2, "Right", "Logout");
    }

    @Test
    void clickingNewPaper_forwardsToPaperEntryPage() {
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
        verify(paperServiceMock, times(5)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    void clickingOnShowXmlPastePanelButton_opensModalWindow() {
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
    void onXmlPasteModalPanelClose_withNullContent_doesNotPersists() {
        makePage().onXmlPasteModalPanelClose(null, mock(AjaxRequestTarget.class));

        verify(pubmedImporterMock, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    void onXmlPasteModalPanelClose_withBlankContent_doesNotPersists() {
        String content = "";
        makePage().onXmlPasteModalPanelClose(content, mock(AjaxRequestTarget.class));

        verify(pubmedImporterMock, never()).persistPubmedArticlesFromXml(anyString());
        verify(paperSlimServiceMock).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    void onXmlPasteModalPanelClose_withContent_persistsArticlesAndUpdatesNavigateable() {
        String content = "content";
        when(pubmedImporterMock.persistPubmedArticlesFromXml(content)).thenReturn(makeServiceResult());

        makePage().onXmlPasteModalPanelClose("content", mock(AjaxRequestTarget.class));

        verify(pubmedImporterMock).persistPubmedArticlesFromXml(content);
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
}
