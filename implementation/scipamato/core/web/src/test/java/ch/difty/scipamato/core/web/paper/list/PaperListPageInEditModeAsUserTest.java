package ch.difty.scipamato.core.web.paper.list;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.web.security.TestUserDetailsService;

@SuppressWarnings("SameParameterValue")
class PaperListPageInEditModeAsUserTest extends PaperListPageTest {

    @Override
    protected String getUserName() {
        return TestUserDetailsService.USER_USER;
    }

    @Override
    protected void setUpHook() {
        super.setUpHook();
        when(getWebSessionFacade().hasAtLeastOneRoleOutOf(Roles.USER, Roles.ADMIN)).thenReturn(true);
        when(getWebSessionFacade().hasAtLeastOneRoleOutOf(Roles.ADMIN)).thenReturn(false);
        when(getWebSessionFacade().hasAtLeastOneRoleOutOf(Roles.USER)).thenReturn(true);
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
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Profile");
        assertNestedMenu(menuIndex, subMenuIndex, position, "Change Password");

        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/");
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc");
        assertPageLinkButton(2, "Right", "Logout");
    }

}
