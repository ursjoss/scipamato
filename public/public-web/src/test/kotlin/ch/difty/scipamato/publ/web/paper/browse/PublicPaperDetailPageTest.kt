package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

private const val NUMBER = 17L

@Suppress("SameParameterValue")
class PublicPaperDetailPageTest : BasePageTest<PublicPaperDetailPage>() {

    override fun setUpHook() {
        super.setUpHook()
        val paper = PublicPaper(
            id = 1L,
            number = NUMBER,
            pmId = 10000,
            authors = "authors",
            authorsAbbreviated = "auths",
            title = "title",
            location = "location",
            journal = "journal",
            publicationYear = 2017,
            goals = "goals",
            methods = "methods",
            population = "population",
            result = "result",
            comment = "comment"
        )
        every { paperService.findByNumber(NUMBER) } returns paper
    }

    override fun doVerify() {
        verify { paperService.findByNumber(NUMBER) }
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(paperService)
    }

    override fun makePage(): PublicPaperDetailPage {
        val pp = PageParameters()
        pp[PublicPageParameters.NUMBER.parameterName] = NUMBER
        return PublicPaperDetailPage(pp)
    }

    override val pageClass: Class<PublicPaperDetailPage>
        get() = PublicPaperDetailPage::class.java

    override fun assertSpecificComponents() {
        val b = "form"
        tester.assertComponent(b, Form::class.java)
        assertHeader(b, true)
        assertReferenceTopic(b)
        assertVisible(b, "goals", "Goals:")
        assertVisible(b, "population", "Population:")
        assertVisible(b, "methods", "Methods:")
        assertVisible(b, "result", "Results:")
        assertVisible(b, "comment", "Comment:")
    }

    private fun assertHeader(form: String, pubmedVisible: Boolean) {
        tester.assertLabel("$form:captionLabel", "Summary of Paper (No 17)")
        tester.assertLabel("$form:title", "title")
        tester.assertComponent("$form:back", BootstrapButton::class.java)
        tester.assertComponent("$form:previous", BootstrapButton::class.java)
        tester.assertComponent("$form:next", BootstrapButton::class.java)
        if (pubmedVisible) tester.assertComponent("$form:pubmed", BootstrapExternalLink::class.java) else tester.assertInvisible("$form:pubmed")
    }

    private fun assertReferenceTopic(form: String) {
        tester.assertLabel("$form:referenceLabel", "Reference:")
        tester.assertLabel("$form:authors", "authors")
        tester.assertLabel("$form:title2", "title")
        tester.assertLabel("$form:location", "location")
    }

    private fun assertVisible(parent: String, topic: String, labelText: String) {
        assertTopic(parent, topic, labelText, true)
    }

    private fun assertInvisible(parent: String, topic: String) {
        assertTopic(parent, topic, null, false)
    }

    private fun assertTopic(parent: String, topic: String, labelText: String?, visible: Boolean) {
        val fullTopic = "$parent:$topic"
        if (visible) {
            tester.assertLabel(fullTopic + "Label", labelText)
            tester.assertLabel(fullTopic, topic)
        } else {
            tester.assertInvisible(fullTopic + "Label")
            tester.assertInvisible(fullTopic)
        }
    }

    @Test
    fun withGoalsMissing_hideGoalsTopic() {
        val p = PublicPaper(
            1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            null, "methods", "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val b = "form"
        assertHeader(b, true)
        assertReferenceTopic(b)
        assertInvisible(b, "goals")
        assertVisible(b, "population", "Population:")
        assertVisible(b, "methods", "Methods:")
        assertVisible(b, "result", "Results:")
        assertVisible(b, "comment", "Comment:")
    }

    @Test
    fun withPopulationMissing_hidePopulationTopic() {
        val p = PublicPaper(
            1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", null, "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val b = "form"
        assertHeader(b, true)
        assertReferenceTopic(b)
        assertVisible(b, "goals", "Goals:")
        assertInvisible(b, "population")
        assertVisible(b, "methods", "Methods:")
        assertVisible(b, "result", "Results:")
        assertVisible(b, "comment", "Comment:")
    }

    @Test
    fun withMethodsMissing_hideMethodsTopic() {
        val p = PublicPaper(
            1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", null, "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val b = "form"
        assertHeader(b, true)
        assertReferenceTopic(b)
        assertVisible(b, "goals", "Goals:")
        assertVisible(b, "population", "Population:")
        assertInvisible(b, "methods")
        assertVisible(b, "result", "Results:")
        assertVisible(b, "comment", "Comment:")
    }

    @Test
    fun withResultMissing_hideResultTopic() {
        val p = PublicPaper(
            1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", null, "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val b = "form"
        assertHeader(b, true)
        assertReferenceTopic(b)
        assertVisible(b, "goals", "Goals:")
        assertVisible(b, "population", "Population:")
        assertVisible(b, "methods", "Methods:")
        assertInvisible(b, "result")
        assertVisible(b, "comment", "Comment:")
    }

    @Test
    fun withCommentMissing_hideCommentTopic() {
        val p = PublicPaper(
            1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", null
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val b = "form"
        assertHeader(b, true)
        assertReferenceTopic(b)
        assertVisible(b, "goals", "Goals:")
        assertVisible(b, "population", "Population:")
        assertVisible(b, "methods", "Methods:")
        assertVisible(b, "result", "Results:")
        assertInvisible(b, "comment")
    }

    @Test
    fun withNullPmId_pubMedLinkIsInvisible() {
        val p = PublicPaper(
            1L, NUMBER, null, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val b = "form"
        assertHeader(b, false)
        assertReferenceTopic(b)
        assertVisible(b, "goals", "Goals:")
        assertVisible(b, "population", "Population:")
        assertVisible(b, "methods", "Methods:")
        assertVisible(b, "result", "Results:")
        assertVisible(b, "comment", "Comment:")
    }

    @Test
    fun clickingPrevious_withPreviousItemAvailable_skipsBack() {
        val previousId: Long = 1
        every { itemNavigator.hasPrevious() } returns true
        every { itemNavigator.itemWithFocus } returns previousId

        val p = PublicPaper(
            2L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("previous")
        tester.assertRenderedPage(PublicPaperDetailPage::class.java)

        verify(exactly = 2) { itemNavigator.hasPrevious() }
        verify { itemNavigator.previous() }
        verify { itemNavigator.itemWithFocus }
        verify { paperService.findByNumber(previousId) }
    }

    @Test
    fun clickingNext_withNextItemAvailable_skipsForward() {
        val nextId: Long = 2
        every { itemNavigator.hasNext() } returns true
        every { itemNavigator.itemWithFocus } returns nextId

        val p = PublicPaper(
            1L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("next")
        tester.assertRenderedPage(PublicPaperDetailPage::class.java)

        verify(exactly = 2) { itemNavigator.hasNext() }
        verify { itemNavigator.next() }
        verify { itemNavigator.itemWithFocus }
        verify { paperService.findByNumber(nextId) }
    }

    @Test
    fun clickingNext_withNextItemAvailable_butWithNoIdReturnedFromItemManager_triesToSkipForwardButRemainsOnPage() {
        every { itemNavigator.hasNext() } returns true
        every { itemNavigator.itemWithFocus } returns null

        val p = PublicPaper(
            1L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("next")
        tester.assertRenderedPage(PublicPaperDetailPage::class.java)

        verify(exactly = 2) { itemNavigator.hasNext() }
        verify { itemNavigator.next() }
        verify { itemNavigator.itemWithFocus }
        verify(exactly = 0) { paperService.findByNumber(any()) }
    }

    @Test
    fun clickingBack_withoutCallingRef_jumpsToPublicPage() {
        val p = PublicPaper(
            2L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment"
        )
        tester.startPage(PublicPaperDetailPage(Model.of(p), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("back")
        tester.assertRenderedPage(PublicPage::class.java)

        verify { paperService.findPageOfNumbersByFilter(any(), any()) }
    }

    @Test
    fun constructingPage_withPageParameterProvidingNumber_loadsPaperWithNumber() {
        val pp = PageParameters()
        pp[PublicPageParameters.NUMBER.parameterName] = NUMBER
        PublicPaperDetailPage(pp)
        verify { paperService.findByNumber(NUMBER) }
    }

    @Test
    fun constructingPage_withoutPageParameterProvidingNumber_loadsNothing() {
        PublicPaperDetailPage(PageParameters())
        verify(exactly = 0) { paperService.findByNumber(any()) }
    }
}
