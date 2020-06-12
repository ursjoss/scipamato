package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.web.common.PanelTest
import ch.difty.scipamato.core.web.paper.PaperSlimBySearchOrderProvider
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.link.ResourceLink
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.util.tester.TagTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.Optional

internal abstract class ResultPanelTest : PanelTest<ResultPanel>() {

    @MockK(relaxed = true)
    lateinit var searchOrderMock: SearchOrder

    @MockK(relaxed = true)
    lateinit var paperMock: Paper

    val paperSlim = PaperSlim(1L, NUMBER, "firstAuthor", 2016, "title")

    override fun setUpHook() {
        every { paperMock.getCodesOf(any()) } returns emptyList()
        every { searchOrderMock.id } returns 10L
        every { paperSlimServiceMock.countBySearchOrder(searchOrderMock) } returns 1
        every { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) } returns listOf(paperSlim)
        every { paperServiceMock.findPageBySearchOrder(searchOrderMock, any(), LC) } returns listOf(paperMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(paperSlimServiceMock, paperServiceMock, codeClassServiceMock, codeServiceMock, searchOrderMock)
    }

    override fun makePanel(): ResultPanel =
        object : ResultPanel(PANEL_ID, PaperSlimBySearchOrderProvider(searchOrderMock, ROWS_PER_PAGE), mode) {
            override fun isOfferingSearchComposition(): Boolean = true
        }

    abstract val mode: Mode

    fun assertEditableTableRow(bb: String) {
        tester.assertLabel("$bb:1:cell", "1")
        tester.assertLabel("$bb:2:cell", NUMBER.toString())
        tester.assertLabel("$bb:3:cell", "firstAuthor")
        tester.assertLabel("$bb:4:cell", "2016")
        tester.assertLabel("$bb:5:cell:link:label", "title")
        tester.assertComponent("$bb:6:cell:link", AjaxLink::class.java)
        tester.assertLabel("$bb:6:cell:link:image", "")
        tester.assertLabel("$bb:7:cell:link:image", "")
    }

    override fun assertSpecificComponents() {
        val b = PANEL_ID
        tester.assertComponent(b, Panel::class.java)
        var bb = "$b:table"
        tester.assertComponent(bb, BootstrapDefaultDataTable::class.java)
        assertTableRow("$bb:body:rows:1:cells")
        bb = "$b:summaryLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:summaryShortLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:reviewLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:literatureReviewLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:literatureReviewPlusLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:summaryTableLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:exportRisLink"
        tester.assertComponent(bb, AjaxLink::class.java)

        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        if (mode != Mode.VIEW)
            verify(exactly = 2) { searchOrderMock.isShowExcluded }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    abstract fun assertTableRow(bb: String)

    fun assertClickingDeleteIconLink() {
        tester.startComponentInPage(makePanel())

        tester.clickLink("$PANEL_ID:table:body:rows:1:cells:6:cell:link")
        tester.assertComponentOnAjaxResponse("$PANEL_ID:table")

        verify(exactly = 2) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify(exactly = 2) { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify(exactly = 4) { searchOrderMock.isShowExcluded }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    fun assertExcludeIcon(iconClass: String?, titleValue: String?) {
        tester.startComponentInPage(makePanel())

        val responseTxt = tester.lastResponse.document
        val iconTagTester = TagTester.createTagByAttribute(responseTxt, "class", iconClass)
        iconTagTester.shouldNotBeNull()
        iconTagTester.name shouldBeEqualTo "i"
        val titleTagTester = TagTester.createTagByAttribute(responseTxt, "title", titleValue)
        titleTagTester.shouldNotBeNull()
        titleTagTester.name shouldBeEqualTo "i"

        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify(exactly = 2) { searchOrderMock.isShowExcluded }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    fun assertNewsletterIcon(iconClass: String?, titleValue: String?) {
        tester.startComponentInPage(newNonSearchRelevantResultPanel())

        val responseTxt = tester.lastResponse.document
        val iconTagTester = TagTester.createTagByAttribute(responseTxt, "class", iconClass)
        iconTagTester.shouldNotBeNull()
        iconTagTester.name shouldBeEqualTo "i"
        val titleTagTester = TagTester.createTagByAttribute(responseTxt, "title", titleValue)
        titleTagTester.shouldNotBeNull()
        titleTagTester.name shouldBeEqualTo "i"

        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingLink_opensPaperEntryPage() {
        val paper = Paper()
        paper.number = NUMBER
        every { paperServiceMock.findByNumber(NUMBER, LC) } returns Optional.of(paper)

        tester.startComponentInPage(makePanel())

        tester.clickLink("$PANEL_ID:table:body:rows:1:cells:5:cell:link")
        tester.assertRenderedPage(PaperEntryPage::class.java)

        verify { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify { paperServiceMock.findByNumber(NUMBER, LC) }
        verify { searchOrderMock.id }
        verify(exactly = if (mode == Mode.VIEW) 1 else 3) { searchOrderMock.isShowExcluded }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    /**
     * Note, we're partially also testing the PaperSummaryDataSource and even the
     * Provider here in order to make sure the functionality is triggered. Not sure
     * how to verify the action otherwise.
     *
     *
     * Also, this is not really asserting anything, just verifying the methods have
     * been called. Bit of a workaround
     */
    private fun verifyPdfExport() {
        verify(exactly = 2) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify { paperServiceMock.findPageBySearchOrder(searchOrderMock, any(), LC) }
        if (mode != Mode.VIEW)
            verify(exactly = 2) { searchOrderMock.isShowExcluded }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingSummaryLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:summaryLink")
        verifyPdfExport()
    }

    @Test
    fun clickingSummaryShortLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:summaryShortLink")
        verifyPdfExport()
    }

    @Test
    fun clickingReviewLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:reviewLink")
        verifyPdfExport()
    }

    @Test
    fun clickingLiteratureReviewLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:literatureReviewLink")
        verifyPdfExport()
    }

    @Test
    fun clickingLiteratureReviewPlusLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:literatureReviewPlusLink")
        verifyPdfExport()
    }

    @Test
    fun clickingSummaryTableLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:summaryTableLink")
        verifyPdfExport()
    }

    @Test
    fun clickingExportRisLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLink("$PANEL_ID:exportRisLink")
        verifyRisExport()
    }

    private fun verifyRisExport() {
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        if (mode != Mode.VIEW)
            verify(exactly = 2) { searchOrderMock.isShowExcluded }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun startingPage_inNonSearchContext_doesNotRenderExcludeFromSearchIcon() {
        val panel = newNonSearchRelevantResultPanel()
        tester.startComponentInPage(panel)
        val responseTxt = tester.lastResponse.document
        val iconExcludeTagTester = TagTester.createTagByAttribute(responseTxt, "class", "fa fa-fw fa-ban")
        iconExcludeTagTester.shouldBeNull()
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrderMock) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrderMock, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    // with isOfferingSearchComposition = false
    fun newNonSearchRelevantResultPanel(): ResultPanel =
        object : ResultPanel(PANEL_ID, PaperSlimBySearchOrderProvider(searchOrderMock, ROWS_PER_PAGE), mode) {
            override fun isOfferingSearchComposition(): Boolean = false
        }

    companion object {
        const val NUMBER = 2L
        private const val ROWS_PER_PAGE = 12
        private const val LC = "en_us"
    }
}
