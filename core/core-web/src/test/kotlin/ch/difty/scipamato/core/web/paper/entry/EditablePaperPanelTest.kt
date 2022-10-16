package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.Paper.NewsletterLink
import ch.difty.scipamato.core.pubmed.AHEAD_OF_PRINT_TAG
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade
import ch.difty.scipamato.core.web.paper.common.PaperPanelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.PageReference
import org.apache.wicket.markup.html.link.ResourceLink
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Optional

@Suppress("SpellCheckingInspection")
internal abstract class EditablePaperPanelTest : PaperPanelTest<Paper, EditablePaperPanel>() {

    lateinit var pubmedArticleMock: PubmedArticleFacade

    val callingPageDummy = PageReference(1)

    override fun setUpLocalHook() {
        pubmedArticleMock = mockk()
        // when referring to PaperSearchPage
        every { searchOrderServiceMock.findById(SEARCH_ORDER_ID) } returns Optional.empty()
    }

    override fun tearDownLocalHook() {
        confirmVerified(pubmedArticleServiceMock, pubmedArticleMock, newsletterServiceMock)
    }

    override fun makePanel(): EditablePaperPanel = newPanel(paperFixture(), callingPageDummy, SEARCH_ORDER_ID, mode)

    protected fun makeAheadOfPrintPanel(): EditablePaperPanel =
        newPanel(aheadOfPrintFixture(), callingPageDummy, SEARCH_ORDER_ID, mode)

    private fun newPanel(p: Paper, pageRef: PageReference?, searchOrderId: Long?, mode: Mode): EditablePaperPanel =
        object : EditablePaperPanel(PANEL_ID, Model.of(p), pageRef, searchOrderId, SHOW_EXCLUDED, mode, Model.of(0)) {
            override fun onFormSubmit() {
                // no-op
            }

            override fun getResponsePage(p: Paper, searchOrderId: Long?, showingExclusions: Boolean): PaperEntryPage? =
                null
        }

    private fun paperFixture(): Paper = Paper().apply {
        id = 1L
        number = 100L
        authors = "a"
        firstAuthor = "fa"
        isFirstAuthorOverridden = false
        title = "t"
        location = "l"
        publicationYear = 2017
        pmId = PMID
        doi = "doi"
        created = LocalDateTime.parse("2017-02-01T13:34:45")
        createdByName = "u1"
        lastModified = LocalDateTime.parse("2017-03-01T13:34:45")
        lastModifiedByName = "u2"
        goals = "g"
        population = "p"
        methods = "m"
        populationPlace = "ppl"
        populationParticipants = "ppa"
        populationDuration = "pd"
        exposurePollutant = "ep"
        exposureAssessment = "ea"
        methodStudyDesign = "msd"
        methodOutcome = "mo"
        methodStatistics = "ms"
        methodConfounders = "mc"
        result = "r"
        comment = "c"
        intern = "i"
        resultMeasuredOutcome = "rmo"
        resultExposureRange = "rer"
        resultEffectEstimate = "ree"
        conclusion = "cc"
        addCode(newC(1, "F"))
        mainCodeOfCodeclass1 = "mcocc1"
        addCode(newC(2, "A"))
        addCode(newC(3, "A"))
        addCode(newC(4, "A"))
        addCode(newC(5, "A"))
        addCode(newC(6, "A"))
        addCode(newC(7, "A"))
        addCode(newC(8, "A"))
        originalAbstract = "oa"
    }

    private fun aheadOfPrintFixture(): Paper = paperFixture().apply {
        location = "J. foobar. $AHEAD_OF_PRINT_TAG"
    }

    abstract val mode: Mode

    @Test
    open fun specificFields_areDisabled() {
        tester.startComponentInPage(makePanel())
        tester.isDisabled("panel:form:id")
        tester.isDisabled("panel:form:firstAuthorOverridden")
        tester.isDisabled("panel:form:createdDisplayValue")
        tester.isDisabled("panel:form:modifiedDisplayValue")
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    fun makePanelWithEmptyPaper(pmId: Int?): EditablePaperPanel {
        val p = Paper().apply { this.pmId = pmId }
        return newPanel(p, null, null, mode)
    }

    fun makePanelWith(
        pmId: Int?,
        callingPage: PageReference?,
        searchOrderId: Long?,
        showExcluded: Boolean,
    ): EditablePaperPanel {
        val p = Paper().apply {
            id = 1L
            this.pmId = pmId
        }
        return newThrowingPanel(p, callingPage, searchOrderId, showExcluded)
    }

    private fun newThrowingPanel(
        p: Paper,
        callingPage: PageReference?,
        searchOrderId: Long?,
        showExcluded: Boolean,
    ): EditablePaperPanel =
        object : EditablePaperPanel(PANEL_ID, Model.of(p), callingPage, searchOrderId, showExcluded, mode, Model.of(0)) {
            override fun onFormSubmit() {
                // no-op
            }

            @Suppress("TooGenericExceptionThrown")
            override fun getResponsePage(p: Paper, searchOrderId: Long?, showingExclusions: Boolean): PaperEntryPage =
                throw RuntimeException("forward to calling page triggered")
        }

    @Test
    fun withNoCallingPage_hasInvisibleBackButton() {
        tester.startComponentInPage(makePanelWith(PMID, null, SEARCH_ORDER_ID, SHOW_EXCLUDED))
        tester.assertInvisible("$PANEL_ID:form:back")
        if (mode != Mode.VIEW)
            verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun isAssociatedWithNewsletter_withNoNewsletterLink() {
        val p = makePanel()
        p.isAssociatedWithNewsletter.shouldBeFalse()
    }

    @Test
    fun isAssociatedWithWipNewsletter_withNoNewsletterLink_isFalse() {
        val p = makePanel()
        p.isAssociatedWithWipNewsletter.shouldBeFalse()
    }

    @Test
    fun isAssociatedWithWipNewsletter_withNewsletterLinkInNonWipStatus_isFalse() {
        val p = makePanel()
        p.modelObject.newsletterLink = NewsletterLink(1, "i1", PublicationStatus.PUBLISHED.id, 1, "t1", "hl")
        p.isAssociatedWithWipNewsletter.shouldBeFalse()
    }

    @Test
    fun isAssociatedWithWipNewsletter_withNewsletterLinkInWipStatus_isTrue() {
        val p = makePanel()
        p.modelObject.newsletterLink =
            NewsletterLink(1, "i1", PublicationStatus.WIP.id, 1, "t1", "hl")
        p.isAssociatedWithWipNewsletter.shouldBeTrue()
    }

    @Test
    fun assertSummaryLink() {
        tester.startComponentInPage(makePanel())
        val path = "panel:form:summary"
        tester.assertComponent(path, ResourceLink::class.java)
        tester.assertVisible(path)
        tester.assertEnabled(path)
        if (mode != Mode.VIEW)
            verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun assertSummaryShortLink() {
        tester.startComponentInPage(makePanel())
        val path = "panel:form:summaryShort"
        tester.assertComponent(path, ResourceLink::class.java)
        tester.assertVisible(path)
        tester.assertEnabled(path)
        if (mode != Mode.VIEW)
            verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun cannotInstantiateInSearchMode() {
        invoking { newPanel(paperFixture(), callingPageDummy, SEARCH_ORDER_ID, Mode.SEARCH) } shouldThrow
            IllegalArgumentException::class withMessage
            "Mode SEARCH is not enabled in class ch.difty.scipamato.core.web.paper.entry." +
            "EditablePaperPanelTest\$newPanel$1"
    }

    companion object {
        const val PMID = 1234
        const val SEARCH_ORDER_ID = 5678L
        const val SHOW_EXCLUDED = false
    }
}
