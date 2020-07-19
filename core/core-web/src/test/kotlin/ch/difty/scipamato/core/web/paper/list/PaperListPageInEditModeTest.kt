package ch.difty.scipamato.core.web.paper.list

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.persistence.DefaultServiceResult
import ch.difty.scipamato.core.persistence.ServiceResult
import ch.difty.scipamato.core.web.common.pastemodal.XmlPasteModalPanel
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage
import ch.difty.scipamato.core.web.security.TestUserDetailsService
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class PaperListPageInEditModeTest : PaperListPageTest() {

    override val userName: String = TestUserDetailsService.USER_ADMIN

    override fun setUpHook() {
        super.setUpHook()
        every { sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.USER, Roles.ADMIN) } returns true
        every { sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN) } returns true
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
        assertNestedMenu(menuIndex, subMenuIndex++, position, "User Management")
        assertNestedMenu(menuIndex, subMenuIndex++, position, "Profile")
        assertNestedMenu(menuIndex, subMenuIndex, position, "Change Password")
        assertExternalLink(
            "navbar:container:collapse:navRightListEnclosure:navRightList:0:component",
            "https://github.com/ursjoss/scipamato/wiki/"
        )
        assertExternalLink(
            "navbar:container:collapse:navRightListEnclosure:navRightList:1:component",
            "https://github.com/ursjoss/scipamato/blob/main/CHANGELOG.adoc"
        )
        assertPageLinkButton(2, "Right", "Logout")
    }

    @Test
    fun clickingNewPaper_forwardsToPaperEntryPage() {
        clearMocks(applicationPropertiesMock)
        val minimumNumber: Long = 7
        val freeNumber: Long = 21
        every { applicationPropertiesMock.minimumPaperNumberToBeRecycled } returns minimumNumber
        every { paperServiceMock.findLowestFreeNumberStartingFrom(minimumNumber) } returns freeNumber
        tester.startPage(pageClass)
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("searchForm")
        formTester.submit("newPaper")
        tester.assertRenderedPage(PaperEntryPage::class.java)
        verify(exactly = 2) { paperSlimServiceMock.countByFilter(any()) }
        verify { applicationPropertiesMock.minimumPaperNumberToBeRecycled }
        verify { paperServiceMock.findLowestFreeNumberStartingFrom(minimumNumber) }
        // from PaperEntryPage
        verify(exactly = 5) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun clickingOnShowXmlPastePanelButton_opensModalWindow() {
        tester.startPage(pageClass)
        var b = "searchForm"
        tester.executeAjaxEvent("$b:showXmlPasteModalLink", "click")
        b = "xmlPasteModal"
        tester.assertComponent(b, ModalWindow::class.java)
        b += ":content"
        tester.isVisible(b)
        tester.assertComponent(b, XmlPasteModalPanel::class.java)
        b += ":form"
        tester.assertComponent(b, Form::class.java)
        tester.assertComponent("$b:content", TextArea::class.java)
        tester.assertComponent("$b:submit", BootstrapAjaxButton::class.java)
        verify(exactly = 2) { paperSlimServiceMock.countByFilter(any()) }
        verify(exactly = 4) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun onXmlPasteModalPanelClose_withNullContent_doesNotPersists() {
        makePage().onXmlPasteModalPanelClose(null, mockk())
        verify(exactly = 0) { pubmedImporterMock.persistPubmedArticlesFromXml(any()) }
        verify { paperSlimServiceMock.countByFilter(any()) }
        verify(exactly = 4) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun onXmlPasteModalPanelClose_withBlankContent_doesNotPersists() {
        val content = ""
        makePage().onXmlPasteModalPanelClose(content, mockk())
        verify(exactly = 0) { pubmedImporterMock.persistPubmedArticlesFromXml(any()) }
        verify { paperSlimServiceMock.countByFilter(any()) }
        verify(exactly = 4) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun onXmlPasteModalPanelClose_withContent_persistsArticlesAndUpdatesNavigateable() {
        val content = "content"
        every { pubmedImporterMock.persistPubmedArticlesFromXml(content) } returns makeServiceResult()
        val targetDummy = AjaxRequestTargetSpy()
        makePage().apply {
            onXmlPasteModalPanelClose("content", targetDummy)
        }
        verify { pubmedImporterMock.persistPubmedArticlesFromXml(content) }
        verify { paperSlimServiceMock.countByFilter(any()) }
        // The third call to findPageOfIds... is to update the Navigateable, the fourth
        // one because of the page redirect
        verify(exactly = 5) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    private fun makeServiceResult(): ServiceResult =
        DefaultServiceResult().apply {
            addInfoMessage("info")
            addWarnMessage("warn")
            addErrorMessage("error")
        }
}
