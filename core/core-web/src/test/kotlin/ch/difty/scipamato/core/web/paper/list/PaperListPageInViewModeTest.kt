package ch.difty.scipamato.core.web.paper.list

import ch.difty.scipamato.core.web.security.TestUserDetailsService
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import io.mockk.verify
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer

internal class PaperListPageInViewModeTest : PaperListPageTest() {

    override val userName: String = TestUserDetailsService.USER_VIEWER

    override fun assertSpecificComponents() {
        assertSearchForm("searchForm")
        assertResultPanel("resultPanel")
        assertMenuEntries()
        verify(exactly = 2) { paperSlimServiceMock.countByFilter(any()) }
        verify(exactly = 4) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    private fun assertMenuEntries() {
        tester.assertComponent("_header_", HtmlHeaderContainer::class.java)
        tester.assertComponent("navbar", Navbar::class.java)

        // Note: Only one menu is open - we're unable to see the other submenu items without clicking.
        assertTopLevelMenu(0, "Left", "Papers")
        assertNestedMenu(0, 0, "Left", "Papers")
        assertExternalLink(
            "navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/"
        )
        assertExternalLink(
            "navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc"
        )
        assertPageLinkButton(2, "Right", "Logout")
    }

    override fun assertSpecificSearchFormComponents(b: String?) {
        tester.assertInvisible("$b:newPaper")
        tester.assertInvisible("$b:showXmlPasteModalLink")
    }
}
