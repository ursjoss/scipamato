package ch.difty.scipamato.core.web.paper.list

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage
import ch.difty.scipamato.core.web.paper.result.EDIT_LINK
import ch.difty.scipamato.core.web.paper.result.ResultPanel
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.Form
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal abstract class PaperListPageTest : BasePageTest<PaperListPage>() {

    @MockkBean(relaxed = true)
    protected lateinit var applicationPropertiesMock: ApplicationCoreProperties

    override fun makePage(): PaperListPage = PaperListPage(null)

    override val pageClass: Class<PaperListPage>
        get() = PaperListPage::class.java

    override fun setUpHook() {
        every { applicationPropertiesMock.brand } returns "SciPaMaTo-Core"
        every { applicationPropertiesMock.authorParserStrategy } returns AuthorParserStrategy.PUBMED
        every { newsletterTopicServiceMock.findAll("en") } returns emptyList()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(
            paperSlimServiceMock, paperServiceMock, codeServiceMock,
            codeClassServiceMock, paperServiceMock
        )
    }

    fun assertSearchForm(b: String?) {
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b!!, "number")
        assertLabeledTextField(b, "authorsSearch")
        assertLabeledTextField(b, "methodsSearch")
        assertLabeledTextField(b, "populationSearch")
        assertLabeledTextField(b, "fieldSearch")
        assertLabeledTextField(b, "pubYearFrom")
        assertLabeledTextField(b, "pubYearUntil")
        assertSpecificSearchFormComponents(b)
    }

    protected abstract fun assertSpecificSearchFormComponents(b: String?)

    fun assertResultPanel(b: String) {
        tester.assertComponent(b, ResultPanel::class.java)
        tester.assertComponent("$b:table", BootstrapDefaultDataTable::class.java)
    }

    fun assertPageLinkButton(index: Int, position: String, expectedLabelText: String?) {
        val path = "navbar:container:collapse:nav${position}ListEnclosure:nav${position}List:$index:component"
        tester.assertComponent(path, NavbarButton::class.java)
        tester.assertLabel("$path:label", expectedLabelText)
    }

    fun assertTopLevelMenu(index: Int, position: String, expectedLabelText: String?) {
        val path = "navbar:container:collapse:nav${position}ListEnclosure:nav${position}List:$index:component:btn"
        tester.assertComponent(path, WebMarkupContainer::class.java)
        tester.assertLabel("$path:label", expectedLabelText)
    }

    fun assertNestedMenu(topLevelIndex: Int, menuIndex: Int, position: String, expectedLabelText: String?) {
        val path = "navbar:container:collapse:nav${position}ListEnclosure:nav$position" +
            "List:$topLevelIndex:component:dropdown-menu:buttons:$menuIndex:button"
        tester.assertComponent(path, MenuBookmarkablePageLink::class.java)
        tester.assertLabel("$path:label", expectedLabelText)
    }

    fun assertExternalLink(path: String?, link: String?) {
        tester.assertComponent(path, NavbarExternalLink::class.java)
        tester.assertModelValue(path, link)
    }

    @Test
    fun clickingOnResultTitle_forwardsToPaperEntryPage() {
        val list: MutableList<PaperSlim> = ArrayList()
        val number = 10L
        list.add(PaperSlim(1L, number, "author", 2018, "title"))
        every { paperSlimServiceMock.countByFilter(any()) } returns list.size
        every { paperSlimServiceMock.findPageByFilter(any(), any()) } returns list
        every { paperServiceMock.findByNumber(number, LC) } returns java.util.Optional.empty()
        tester.startPage(pageClass)
        tester.clickLinkSameSite("resultPanel:$EDIT_LINK")
        tester.assertRenderedPage(PaperEntryPage::class.java)
        verify(exactly = 2) { paperSlimServiceMock.countByFilter(any()) }
        verify { paperSlimServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 4) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
        verify { paperServiceMock.findByNumber(number, LC) }
    }

    companion object {
        private const val LC = "en_us"
    }
}
