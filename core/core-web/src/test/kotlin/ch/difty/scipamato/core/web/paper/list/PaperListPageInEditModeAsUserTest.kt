package ch.difty.scipamato.core.web.paper.list

import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.web.security.TestUserDetailsService
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer

@Suppress("SpellCheckingInspection")
internal class PaperListPageInEditModeAsUserTest : PaperListPageTest() {

    override val userName = TestUserDetailsService.USER_USER

    override fun setUpHook() {
        super.setUpHook()
        every { sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.USER, Roles.ADMIN) } returns true
        every { sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN) } returns false
        every { sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.USER) } returns true
    }

    override fun assertSpecificComponents() {
        assertSearchForm("searchForm")
        assertPasteModal("xmlPasteModal")
        assertResultPanel("resultPanel")
        assertMenuEntries()
        verify(exactly = 2) { paperSlimServiceMock.countByFilter(any()) }
        verify(exactly = 4) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    override fun assertSpecificSearchFormComponents(b: String?) {
        tester.assertComponent("$b:newPaper", BootstrapAjaxButton::class.java)
        tester.assertComponent("$b:showXmlPasteModalLink", BootstrapAjaxLink::class.java)
    }

    @Suppress("SameParameterValue")
    private fun assertPasteModal(id: String) {
        tester.assertComponent(id, ModalWindow::class.java)
        tester.assertInvisible("$id:content")
    }

    private fun assertMenuEntries() {
        tester.assertComponent("_header_", HtmlHeaderContainer::class.java)
        tester.assertComponent("navbar", Navbar::class.java)

        // Note: Only one menu is open - we're unable to see the other submenu items without clicking.
        val position = "Left"
        var menuIndex = 0
        var subMenuIndex = 0
        assertTopLevelMenu(menuIndex, position, "Papers")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Papers")
        assertNestedMenu(menuIndex, subMenuIndex, position, "Search")
        subMenuIndex = 0
        assertTopLevelMenu(++menuIndex, position, "Newsletters")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Newsletter")
        assertNestedMenu(menuIndex, subMenuIndex, position, "Newsletter Topics")
        subMenuIndex = 0
        assertTopLevelMenu(++menuIndex, position, "Reference Data")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Keywords")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Codes")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Code Classes")
        assertNestedMenu(menuIndex, subMenuIndex, position, "Sync: Core -&gt; Public")
        subMenuIndex = 0
        assertTopLevelMenu(++menuIndex, position, "Preferences")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Profile")
        assertNestedMenu(menuIndex, subMenuIndex, position, "Change Password")
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/")
        assertExternalLink("navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/master/CHANGELOG.asciidoc")
        assertPageLinkButton(2, "Right", "Logout")
    }
}
