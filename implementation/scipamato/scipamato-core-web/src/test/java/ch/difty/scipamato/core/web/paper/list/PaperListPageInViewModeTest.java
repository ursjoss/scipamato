package ch.difty.scipamato.core.web.paper.list;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.web.security.TestUserDetailsService;

@SuppressWarnings("SameParameterValue")
class PaperListPageInViewModeTest extends PaperListPageTest {

    @Override
    protected String getUserName() {
        return TestUserDetailsService.USER_VIEWER;
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchForm("searchForm");
        assertResultPanel("resultPanel");

        assertMenuEntries();

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
    }

    void assertMenuEntries() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);
        getTester().assertComponent("navbar", Navbar.class);

        // Note: Only one menu is open - we're unable to see the other submenu items without clicking.
        assertTopLevelMenu(0, "Left", "Papers");
        assertNestedMenu(0, 0, "Left", "Papers");

        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/");
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc");
        assertPageLinkButton(2, "Right", "Logout");
    }

    @Override
    protected void assertSpecificSearchFormComponents(String b) {
        getTester().assertInvisible(b + ":newPaper");
        getTester().assertInvisible(b + ":showXmlPasteModalLink");
    }

}
