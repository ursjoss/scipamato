package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.publ.entity.CodeClass
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.persistence.api.CodeClassService
import ch.difty.scipamato.publ.persistence.api.CodeService
import ch.difty.scipamato.publ.web.common.BasePageTest
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal class PublicPageTest : BasePageTest<PublicPage>() {

    @MockkBean(relaxed = true)
    private lateinit var codeServiceMock: CodeService

    @MockkBean(relaxed = true)
    private lateinit var codeClassServiceMock: CodeClassService

    private val papers: MutableList<PublicPaper> = ArrayList()

    override fun setUpHook() {
        super.setUpHook()
        papers.add(
            PublicPaper(
                1L, 10L, 1000, "authors1", "auths1", "title1", "location1", "journal1", 2016, "goals1",
                "methods1", "population1", "result1", "comment1"
            )
        )
        papers.add(
            PublicPaper(
                2L, 20L, 1002, "authors2", "auths2", "title2", "location2", "journal2", 2017, "goals2",
                "methods2", "population2", "result2", "comment2"
            )
        )

        every { paperService.countByFilter(any()) } returns papers.size
        every { paperService.findPageByFilter(any(), any()) } returns papers
    }

    override fun doVerify() {
        // override if necessary
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(paperService, codeClassServiceMock)
    }

    override fun makePage(): PublicPage = PublicPage(PageParameters())

    override val pageClass: Class<PublicPage>
        get() = PublicPage::class.java

    override fun assertSpecificComponents() {
        val b = "searchForm"
        tester.assertComponent(b, Form::class.java)
        assertTabPanelWithFirstTabVisible("$b:tabs")
        tester.assertComponent("$b:query", BootstrapButton::class.java)
        tester.assertComponent("$b:clear", BootstrapButton::class.java)
        tester.assertComponent("$b:help", BootstrapExternalLink::class.java)
        tester.assertModelValue("$b:help", "https://github.com/ursjoss/scipamato/wiki/Filtering-Papers-Public")

        // query was not yet executed and results panel is still invisible
        tester.assertInvisible("results")

        verify { paperService.findPageOfNumbersByFilter(any(), any()) }
    }

    private fun assertTabPanelWithFirstTabVisible(b: String) {
        var bb = b
        tester.assertComponent(bb, BootstrapTabbedPanel::class.java)

        // both tab titles are visible
        assertTabTitle(bb, 0, "Simple Search")
        assertTabTitle(bb, 1, "Extended Search")

        // first tab is visible
        bb += ":panel:tab1Form"
        tester.assertComponent(bb, Form::class.java)
        tester.assertComponent("$bb:simpleFilterPanel", SimpleFilterPanel::class.java)
    }

    private fun assertTabTitle(b: String, index: Int, title: String) {
        val bb = "$b:tabs-container:tabs:$index:link"
        tester.assertComponent(bb, Link::class.java)
        tester.assertLabel("$bb:title", title)
    }

    @Test
    fun clickingQuery_showsResultPanel() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)

        // trigger the round-trip to get the data by clicking 'query'
        // this should make the result panel visible
        tester
            .newFormTester("searchForm")
            .submit("query")
        val b = "searchForm"
        tester.assertComponent(b, Form::class.java)
        assertTabPanelWithFirstTabVisible("$b:tabs")
        tester.assertComponent("$b:query", BootstrapButton::class.java)
        assertResultsTable()

        verify { paperService.countByFilter(any()) }
        verify { paperService.findPageByFilter(any(), any()) }
        // used in navigateable
        verify(exactly = 3) { paperService.findPageOfNumbersByFilter(any(), any()) }
    }

    private fun assertResultsTable() {
        tester.assertComponent("results", BootstrapDefaultDataTable::class.java)
        assertTableRow("results:body:rows:1:cells", "auths1", "title1", "journal1", "2016")
        assertTableRow("results:body:rows:2:cells", "auths2", "title2", "journal2", "2017")
    }

    private fun assertTableRow(bb: String, vararg values: String) {
        var i = 1
        for (v in values) {
            if (i != 2) tester.assertLabel(bb + ":" + i++ + ":cell", v) else tester.assertLabel(bb + ":" + i++ + ":cell:link:label", v)
        }
    }

    @Test
    fun clickingTab2Title_showsTab2() {
        val cc1 = CodeClass
            .builder()
            .codeClassId(1)
            .name("cc1")
            .build()
        val cc2 = CodeClass
            .builder()
            .codeClassId(2)
            .name("cc2")
            .build()
        every { codeClassServiceMock.find("en_us") } returns listOf(cc1, cc2)
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        val b = "searchForm"
        val bb = "$b:tabs"
        // Switch to the second tab
        tester.clickLink("$bb:tabs-container:tabs:1:link")
        assertTabPanelWithSecondTabVisible(bb)

        verify { codeClassServiceMock.find("en_us") }
        verify(exactly = 2) { paperService.findPageOfNumbersByFilter(any(), any()) }
    }

    private fun assertTabPanelWithSecondTabVisible(b: String) {
        tester.assertComponent(b, BootstrapTabbedPanel::class.java)

        // both tab titles are visible
        assertTabTitle(b, 0, "Simple Search")
        assertTabTitle(b, 1, "Extended Search")

        // second tab is visible
        val bb = "$b:panel:tab2Form"
        tester.assertComponent(bb, Form::class.java)
        tester.assertComponent("$bb:simpleFilterPanel", SimpleFilterPanel::class.java)
        var i = 1
        assertCodeClass(bb, i, "cc" + i++)
        assertCodeClass(bb, i, "cc" + i++)
        assertCodeClass(bb, i++)
        assertCodeClass(bb, i++)
        assertCodeClass(bb, i++)
        assertCodeClass(bb, i++)
        assertCodeClass(bb, i++)
        assertCodeClass(bb, i)
    }

    private fun assertCodeClass(esc: String, ccId: Int, ccLabel: String = "CC$ccId") {
        val compId = "$esc:codesOfClass$ccId"
        tester.assertLabel(compId + "Label", ccLabel)
        tester.assertComponent(compId, BootstrapMultiSelect::class.java)
    }

    @Test
    fun clickingTitle_forwardsToDetailsPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester
            .newFormTester("searchForm")
            .submit("query")
        tester.clickLink("results:body:rows:1:cells:2:cell:link")
        tester.assertRenderedPage(PublicPaperDetailPage::class.java)

        verify { paperService.countByFilter(any()) }
        verify { paperService.findPageByFilter(any(), any()) }
        // used in navigateable
        verify(exactly = 3) { paperService.findPageOfNumbersByFilter(any(), any()) }
    }

    @Test
    fun clickingClearSearch() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("searchForm")
        formTester.setValue("tabs:panel:tab1Form:simpleFilterPanel:methodsSearch", "foo")
        formTester.submit("clear")
        tester.assertRenderedPage(PublicPage::class.java)

        // used in navigateable
        verify(exactly = 3) { paperService.findPageOfNumbersByFilter(any(), any()) }
    }
}
