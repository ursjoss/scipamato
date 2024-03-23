package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.clickLinkSameSite
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

const val EDIT_LINK = "table:body:rows:1:cells:4:cell:link"

internal abstract class ResultPanelTest : PanelTest<ResultPanel>() {

    val searchOrder = SearchOrder().apply { id = 10L }

    val paper: Paper = Paper().apply { id = ID }

    val paperSlim = PaperSlim(ID, NUMBER, "firstAuthor", 2016, "title")

    override fun setUpHook() {
        every { paperSlimServiceMock.countBySearchOrder(searchOrder) } returns 1
        every { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) } returns listOf(paperSlim)
        every { paperServiceMock.findPageBySearchOrder(searchOrder, any(), LC) } returns listOf(paper)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeClassServiceMock, codeServiceMock)
    }

    override fun makePanel(): ResultPanel =
        object : ResultPanel(PANEL_ID, PaperSlimBySearchOrderProvider(searchOrder, ROWS_PER_PAGE), mode) {
            override val isOfferingSearchComposition: Boolean
                get() = true
        }

    abstract val mode: Mode

    fun assertEditableTableRow(bb: String) {
        tester.assertLabel("$bb:1:cell", NUMBER.toString())
        tester.assertLabel("$bb:2:cell", "firstAuthor")
        tester.assertLabel("$bb:3:cell", "2016")
        tester.assertLabel("$bb:4:cell:link:label", "title")
        tester.assertComponent("$bb:5:cell:link", AjaxLink::class.java)
        tester.assertLabel("$bb:5:cell:link:image", "")
        tester.assertLabel("$bb:6:cell:link:image", "")
        tester.assertLabel("$bb:7:cell", "1")
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
        bb = "$b:reviewCsvLink"
        tester.assertComponent(bb, AjaxLink::class.java)
        bb = "$b:literatureReviewLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:literatureReviewPlusLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:summaryTableLink"
        tester.assertComponent(bb, ResourceLink::class.java)
        bb = "$b:exportRisLink"
        tester.assertComponent(bb, AjaxLink::class.java)

        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    abstract fun assertTableRow(bb: String)

    fun assertClickingDeleteIconLink() {
        tester.startComponentInPage(makePanel())

        tester.clickLinkSameSite("$PANEL_ID:table:body:rows:1:cells:5:cell:link")
        tester.assertComponentOnAjaxResponse("$PANEL_ID:table")

        verify(exactly = 2) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify(exactly = 2) { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
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

        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
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

        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingLink_opensPaperEntryPage() {
        val paper = Paper()
        paper.number = NUMBER
        every { paperServiceMock.findByNumber(NUMBER, LC) } returns Optional.of(paper)

        tester.startComponentInPage(makePanel())

        tester.clickLinkSameSite("$PANEL_ID:$EDIT_LINK")
        tester.assertRenderedPage(PaperEntryPage::class.java)

        verify { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify { paperServiceMock.findByNumber(NUMBER, LC) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    /**
     * Note, we're partially also testing the PaperSummaryDataSource and even the
     * Provider here in order to make sure the functionality is triggered. Not sure
     * how to verify the action otherwise.
     *
     * Also, this is not really asserting anything, just verifying the methods have
     * been called. Bit of a workaround
     */
    private fun verifyPdfExport() {
        verify(exactly = 2) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify(exactly = 1) { paperSlimServiceMock.countByFilter(any()) }
        verify { paperServiceMock.findPageBySearchOrder(searchOrder, any(), LC) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingSummaryLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:summaryLink")
        verifyPdfExport()
    }

    @Test
    fun clickingSummaryShortLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:summaryShortLink")
        verifyPdfExport()
    }

    @Test
    fun clickingReviewLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:reviewLink")
        verifyPdfExport()
    }

    @Test
    fun clickingReviewCsvLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:reviewCsvLink")
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingLiteratureReviewLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:literatureReviewLink")
        verifyPdfExport()
    }

    @Test
    fun clickingLiteratureReviewPlusLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:literatureReviewPlusLink")
        verifyPdfExport()
    }

    @Test
    fun clickingSummaryTableLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:summaryTableLink")
        verifyPdfExport()
    }

    @Test
    fun clickingReferenceAbstractLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:referenceAbstractLink")
        verifyPdfExport()
    }

    @Test
    fun clickingExportRisLink_succeeds() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("$PANEL_ID:exportRisLink")
        verifyRisExport()
    }

    private fun verifyRisExport() {
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun startingPage_inNonSearchContext_doesNotRenderExcludeFromSearchIcon() {
        val panel = newNonSearchRelevantResultPanel()
        tester.startComponentInPage(panel)
        val responseTxt = tester.lastResponse.document
        val iconExcludeTagTester = TagTester.createTagByAttribute(responseTxt, "class", "fa fa-fw fa-ban")
        iconExcludeTagTester.shouldBeNull()
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder) }
        verify { paperSlimServiceMock.findPageBySearchOrder(searchOrder, any()) }
        verify { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    // with isOfferingSearchComposition = false
    fun newNonSearchRelevantResultPanel(): ResultPanel =
        object : ResultPanel(PANEL_ID, PaperSlimBySearchOrderProvider(searchOrder, ROWS_PER_PAGE), mode) {
            override val isOfferingSearchComposition: Boolean
                get() = false
        }

    companion object {
        const val ID = 1L
        const val NUMBER = 2L
        private const val ROWS_PER_PAGE = 12
        private const val LC = "en_us"
    }
}
