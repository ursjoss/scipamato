package ch.difty.scipamato.core.web.newsletter.list

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.web.component.table.column.LinkIconPanel
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.core.web.newsletter.edit.NewsletterEditPage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldBeBlank
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.link.Link
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

private const val ID_PUB = 1
private const val ID_WIP = 2

@Suppress("SameParameterValue")
internal class NewsletterListPageTest : BasePageTest<NewsletterListPage>() {

    private val newsletterInProgress = Newsletter
        .builder()
        .issue("1801")
        .issueDate(LocalDate.parse("2018-01-01"))
        .publicationStatus(PublicationStatus.WIP)
        .build().apply { id = ID_WIP }
    private val newsletterPublished = Newsletter
        .builder()
        .issue("1801")
        .issueDate(LocalDate.parse("2018-01-01"))
        .publicationStatus(PublicationStatus.PUBLISHED)
        .build().apply { id = ID_PUB }
    private val results = listOf(newsletterInProgress, newsletterPublished)

    override fun setUpHook() {
        every { newsletterServiceMock.countByFilter(any()) } returns results.size
        every { newsletterServiceMock.findPageByFilter(any(), any()) } returns results
        every { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(any()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(newsletterServiceMock)
    }

    override fun makePage(): NewsletterListPage = NewsletterListPage(null)

    override val pageClass: Class<NewsletterListPage>
        get() = NewsletterListPage::class.java

    override fun assertSpecificComponents() {
        assertFilterForm("filterForm")
        val headers = arrayOf("Issue", "Issue Date", "Publication Status")
        val values = arrayOf("1801", "2018-01-01", "In Progress")
        assertResultTable("results", headers, values)
        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    private fun assertFilterForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b, "issue")
        assertLabeledBootstrapSelect(b, "publicationStatus")
        assertLabeledBootstrapSelect(b, "topics")
        tester.assertComponent("$b:newNewsletter", BootstrapAjaxButton::class.java)
    }

    private fun assertResultTable(b: String, labels: Array<String>, values: Array<String>) {
        tester.assertComponent(b, BootstrapDefaultDataTable::class.java)
        assertHeaderColumns(b, labels)
        assertTableValuesOfRow(b, 1, 1, values)
    }

    private fun assertHeaderColumns(b: String, labels: Array<String>) =
        labels.withIndex().forEach { (idx, label) ->
            val p = b + ":topToolbars:toolbars:2:headers:" + (idx + 1) + ":header:orderByLink:header_body:label"
            tester.assertLabel(p, label)
        }

    @Suppress("SameParameterValue")
    private fun assertTableValuesOfRow(b: String, rowIdx: Int, colIdxAsLink: Int?, values: Array<String>) {
        colIdxAsLink?.let { idx ->
            tester.assertComponent("$b:body:rows:$rowIdx:cells:$idx:cell:link", Link::class.java)
        }
        var colIdx = 1
        values.forEach { value ->
            val p = "$b:body:rows:$rowIdx:cells:$colIdx:cell" +
                if (colIdxAsLink != null && colIdx++ == colIdxAsLink) ":link:label" else ""
            tester.assertLabel(p, value)
        }
    }

    @Test
    fun clickingOnNewsletterIssue_forwardsToNewsletterEntryPage_withModelLoaded() {
        tester.startPage(pageClass)
        tester.clickLinkSameSite("results:body:rows:1:cells:1:cell:link")
        tester.assertRenderedPage(NewsletterEditPage::class.java)

        // verify the newsletter was loaded in the target page
        val formTester = tester.newFormTester("form")
        formTester.getTextComponentValue("issue") shouldBeEqualTo "1801"

        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun clickingNewNewsletter_forwardsToNewsletterEditPage() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns true
        tester.startPage(pageClass)
        tester.assertRenderedPage(pageClass)
        tester.assertEnabled("filterForm:newNewsletter")
        val formTester = tester.newFormTester("filterForm")
        formTester.submit("newNewsletter")
        tester.assertRenderedPage(NewsletterEditPage::class.java)

        // verify we have a blank newsletter in the target page
        val targetFormTester = tester.newFormTester("form")
        targetFormTester.getTextComponentValue("issue").shouldBeBlank()

        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun givenNoNewNewsletterShallBeCreated_newNewsletterButtonIsDisabled() {
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns false

        tester.startPage(pageClass)

        tester.assertRenderedPage(pageClass)
        tester.assertDisabled("filterForm:newNewsletter")

        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun clickingSortTopicsIcon_forwardsToSortTopicsPage() {
        tester.startPage(pageClass)

        tester.assertRenderedPage(pageClass)

        tester.clickLinkSameSite("results:body:rows:1:cells:4:cell:link")
        tester.assertRenderedPage(NewsletterTopicSortPage::class.java)

        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify { newsletterTopicServiceMock.removeObsoleteNewsletterTopicsFromSort(ID_WIP) }
    }

    @Test
    fun clickingRemoveIcon_forNewsletterInProgress_delegatesRemovalToServiceAndRefreshesResultPanel() {
        with(tester) {
            startPage(pageClass)

            assertRenderedPage(pageClass)

            clickLinkSameSite("results:body:rows:1:cells:5:cell:link")
            assertComponentOnAjaxResponse("filterForm:newNewsletter")
            assertComponentOnAjaxResponse("results")
            assertComponentOnAjaxResponse("feedback")
            assertInfoMessages("Newsletter 1801 was deleted successfully.")
        }

        verify(exactly = 2) { newsletterServiceMock.countByFilter(any()) }
        verify(exactly = 2) { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 1) { newsletterServiceMock.remove(newsletterInProgress) }
        verify(exactly = 2) { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun clickingRemoveIcon_forNewsletterPublished_doesNotDelegate() {
        tester.startPage(pageClass)

        tester.assertRenderedPage(pageClass)

        tester.clickLinkSameSite("results:body:rows:2:cells:5:cell:link")

        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 0) { newsletterServiceMock.remove(newsletterInProgress) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun assertVisibilityOfRemoveLink_dependingOnPublicationState() {
        tester.startPage(pageClass)

        tester.assertRenderedPage(pageClass)
        validateLinkIconColumn(1, "In Progress", "fa-solid fa-trash-can fa-fw")
        validateLinkIconColumn(2, "Published", "")

        verify { newsletterServiceMock.countByFilter(any()) }
        verify { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    private fun validateLinkIconColumn(row: Int, status: String, value: String) {
        val bodyRow = "results:body:rows:$row:cells:"
        tester.assertLabel(bodyRow + "3:cell", status)
        tester.assertComponent(bodyRow + "4:cell", LinkIconPanel::class.java)
        tester.assertModelValue(bodyRow + "4:cell", "fa-solid fa-shuffle fa-fw")
        tester.assertComponent(bodyRow + "5:cell", LinkIconPanel::class.java)
        tester.assertModelValue(bodyRow + "5:cell", value)
    }

    @Test
    fun changingPublicationStatus_updatesResultTable() {
        tester.startPage(pageClass)

        tester.executeAjaxEvent("filterForm:publicationStatus", "change")
        tester.assertComponentOnAjaxResponse("results")

        verify(exactly = 2) { newsletterServiceMock.countByFilter(any()) }
        verify(exactly = 2) { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun changingNewsletterTopic_updatesResultTable() {
        tester.startPage(pageClass)

        tester.executeAjaxEvent("filterForm:topics", "change")
        tester.assertComponentOnAjaxResponse("results")

        verify(exactly = 2) { newsletterServiceMock.countByFilter(any()) }
        verify(exactly = 2) { newsletterServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 1) { newsletterServiceMock.canCreateNewsletterInProgress() }
    }
}
