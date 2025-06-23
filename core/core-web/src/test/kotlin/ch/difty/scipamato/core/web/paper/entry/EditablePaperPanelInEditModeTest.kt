package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.pubmed.PubmedArticleResult
import ch.difty.scipamato.core.web.paper.search.PaperSearchPage
import ch.difty.scipamato.newFormTesterSameSite
import ch.difty.scipamato.submitFormSameSite
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.PageReference
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter
import org.apache.wicket.feedback.FeedbackMessage
import org.apache.wicket.util.tester.TagTester
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.*

@Suppress("SpellCheckingInspection", "LargeClass")
internal class EditablePaperPanelInEditModeTest : EditablePaperPanelTest() {

    override val mode: Mode
        get() = Mode.EDIT

    override fun setUpLocalHook() {
        super.setUpLocalHook()
        every { newsletterServiceMock.canCreateNewsletterInProgress() } returns false
    }

    override fun assertSpecificComponents() {
        var b = PANEL_ID
        tester.assertComponent(b, EditablePaperPanel::class.java)
        assertCommonComponents(b)
        b += ":form"
        assertTextFieldWithLabel("$b:id", 1L, "ID")
        assertTextFieldWithLabel("$b:number", 100L, "SciPaMaTo-Core-No.")
        assertTextFieldWithLabel("$b:publicationYear", 2017, "Pub. Year")
        assertTextFieldWithLabel("$b:pmId", 1234, "PMID")
        tester.assertComponent("$b:submit", BootstrapButton::class.java)
        tester.assertLabel("$b:submit:label", "Save")
        assertTextFieldWithLabel("$b:createdDisplayValue", "u1 (2017-02-01 13:34:45)", "Created")
        assertTextFieldWithLabel("$b:modifiedDisplayValue", "u2 (2017-03-01 13:34:45)", "Last Modified")
        tester.assertComponent("$b:back", BootstrapButton::class.java)
        tester.assertComponent("$b:previous", BootstrapButton::class.java)
        tester.assertComponent("$b:next", BootstrapButton::class.java)
        // disabled as paperManagerMock is not mocked here
        tester.isDisabled("$b:previous")
        tester.isDisabled("$b:next")
        tester.assertComponent("$b:exclude", BootstrapButton::class.java)
        tester.assertComponent("$b:pubmedRetrieval", BootstrapAjaxLink::class.java)
        tester.assertVisible("$b:pubmedRetrieval")
        tester.assertComponent("$b:modAssociation", BootstrapAjaxLink::class.java)
        verifyCodeAndCodeClassCalls(1)
        verify(exactly = 7) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun withNoSearchOrderId_hasInvisibleExcludeButton() {
        tester.startComponentInPage(makePanelWith(PMID, callingPageDummy, null, SHOW_EXCLUDED))
        tester.assertInvisible("$PANEL_ID:form:exclude")
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun doesNotShowAttachmentSearchFields() {
        tester.startComponentInPage(makePanel())
        var b = "panel"
        tester.assertComponent(b, EditablePaperPanel::class.java)
        assertCommonComponents(b)
        b += ":form"

        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:5:link")
        val bb = "$b:tabs:panel"
        val bbb = "$bb:tab6Form"
        tester.assertInvisible("$bbb:attachmentNameMask")
        tester.assertInvisible("$bbb:hasAttachments")

        verify(exactly = 1) { codeClassServiceMock.find(any()) }
        verify(exactly = 8) { codeServiceMock.findCodesOfClass(any(), any()) }
        verify(exactly = 8) { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun assertSubmit() {
        tester.startComponentInPage(makePanel())
        applyTestHackWithNestedMultiPartForms()
        tester.submitFormSameSite("panel:form")
        verify(exactly = 2) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun assertRequiredFields() {
        val b = "panel:form:"
        tester.startComponentInPage(makePanel())
        tester.assertRequired(b + "number")
        tester.assertRequired(b + "authors")
        tester.assertRequired(b + "firstAuthor")
        tester.assertRequired(b + "title")
        tester.assertRequired(b + "location")
        tester.assertRequired(b + "publicationYear")
        tester.assertRequired(b + "tabs:panel:tab1Form:goals")
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    override fun specificFields_areDisabled() {
        tester.startComponentInPage(makePanel())
        tester.isDisabled("panel:form:id")
        tester.isDisabled("panel:form:firstAuthorOverridden")
        tester.isDisabled("panel:form:createdDisplayValue")
        tester.isDisabled("panel:form:modifiedDisplayValue")
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun firstAuthorChangeBehavior_withoutTriggering_hasFirstAuthorOverriddenFalseAndFirstAuthorDisabled() {
        tester.startComponentInPage(makePanel())
        val formId = "panel:form:"
        tester.assertModelValue(formId + "firstAuthorOverridden", false)
        tester.assertDisabled(formId + "firstAuthor")
        val formTester = tester.newFormTesterSameSite(formId)
        formTester.getTextComponentValue("authors") shouldBeEqualTo "a"
        formTester.getTextComponentValue("firstAuthor") shouldBeEqualTo "fa"
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun firstAuthorChangeBehavior_withoutUpdatedAuthor_hasFirstAuthorOverriddenFalseAndFirstAuthorDisabled() {
        tester.startComponentInPage(makePanel())
        val formId = "panel:form:"
        val formTester = tester.newFormTester(formId)
        formTester.getTextComponentValue("authors") shouldBeEqualTo "a"
        formTester.getTextComponentValue("firstAuthor") shouldBeEqualTo "fa"
        formTester.setValue("authors", "Darwin C.")
        tester.executeAjaxEvent(formId + "authors", "change")
        formTester.getTextComponentValue("authors") shouldBeEqualTo "Darwin C."
        formTester.getTextComponentValue("firstAuthor") shouldBeEqualTo "Darwin"
        tester.assertComponentOnAjaxResponse(formId + "firstAuthor")
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun mainCodeOfCodeClass1ChangeBehavior_whenChangingCodesClass1_reflectsInMainCodeOfCodeClass() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:2:link")
        val formId = "panel:form:tabs:panel:tab3Form:"
        tester.assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1")
        tester.assertModelValue(formId + "codesClass1", listOf(newC(1, "F")))
        val formTester = tester.newFormTesterSameSite(formId)
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "mcocc1"
        tester.executeAjaxEvent(formId + "codesClass1", "change")
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "1F"
        verifyCodeAndCodeClassCalls(3, 4)
        verify(exactly = 2) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun mainCodeOfCodeClass1ChangeBehavior_whenRemovingCodeOfClass1_clearsMainCodeOfCodeClass() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:2:link")
        val formId = "panel:form:tabs:panel:tab3Form:"
        tester.assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1")
        tester.assertModelValue(formId + "codesClass1", listOf(newC(1, "F")))
        val formTester = tester.newFormTesterSameSite(formId)
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "mcocc1"
        val indices = IntArray(2)
        indices[0] = 2
        formTester.selectMultiple("codesClass1", indices, true)
        tester.executeAjaxEvent(formId + "codesClass1", "change")
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo ""
        verifyCodeAndCodeClassCalls(3, 4)
        verify(exactly = 2) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun mainCodeOfCodeClass1ChangeBehavior_walkThroughStateChanges() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:2:link")
        val formId = "panel:form:tabs:panel:tab3Form:"
        tester.assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1")
        tester.assertModelValue(formId + "codesClass1", listOf(newC(1, "F")))
        val formTester = tester.newFormTesterSameSite(formId)
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "mcocc1"

        // first choice selected -> keep mainCode as is
        var indices = IntArray(2)
        formTester.selectMultiple("codesClass1", indices, true)
        tester.executeAjaxEvent(formId + "codesClass1", "change")
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "1F"

        // add additional code to code class1 -> leave mainCodeOfCodeClass as is
        indices[1] = 1
        formTester.selectMultiple("codesClass1", indices, true)
        tester.executeAjaxEvent(formId + "codesClass1", "change")
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "1F"

        // only select the second option in codesOfCodeClass1 -> adjusts
        // mainCodeOfCodeClass1 accordingly
        indices = IntArray(1)
        indices[0] = 1
        formTester.selectMultiple("codesClass1", indices, true)
        tester.executeAjaxEvent(formId + "codesClass1", "change")
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "1G"

        // clear all codes of codeClass1 -> clear mainCodeOfCodeClass1
        indices = IntArray(0)
        formTester.selectMultiple("codesClass1", indices, true)
        tester.executeAjaxEvent(formId + "codesClass1", "change")
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo ""

        // getTester().assertComponentOnAjaxResponse(formId + "mainCodeOfCodeclass1"); // TOOD should actually be true
        verifyCodeAndCodeClassCalls(6, 9)
        verify(exactly = 2) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun canModifyMultipleCodes() {
        tester.startComponentInPage(makePanel())
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:2:link")
        val formId = "panel:form:tabs:panel:tab3Form:"
        tester.assertModelValue(formId + "mainCodeOfCodeclass1", "mcocc1")
        tester.assertModelValue(formId + "codesClass1", listOf(newC(1, "F")))
        val formTester = tester.newFormTesterSameSite(formId)
        formTester.getTextComponentValue("mainCodeOfCodeclass1") shouldBeEqualTo "mcocc1"

        // first choice selected -> keep mainCode as is
        val indices = IntArray(2)
        indices[0] = 1
        indices[1] = 1
        formTester.selectMultiple("codesClass1", indices, true)
        tester.executeAjaxEvent(formId + "codesClass1", "change")

        applyTestHackWithNestedMultiPartForms()
        tester.submitFormSameSite("panel:form")

        verifyCodeAndCodeClassCalls(4, 5)
        verify(exactly = 3) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withNoPmIdInScipamatoPaper_warns() {
        tester.startComponentInPage(makePanel())
        every { pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID) } returns
            PubmedArticleResult(null, null, "some message")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertErrorMessages(
            "Could not retrieve an article with PMID $PMID from PubMed: some message"
        )
        verify { pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withBadGateway_warns() {
        tester.startComponentInPage(makePanel())
        every { pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID) } returns
            PubmedArticleResult(null, HttpStatus.BAD_GATEWAY, "boom")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertErrorMessages(
            "Could not retrieve an article with PMID $PMID from PubMed: Status 502 BAD_GATEWAY: boom"
        )
        verify { pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetAndEqual_informsAboutPerfectMatch() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages("All compared fields are matching those in PubMed.")
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Suppress("LongParameterList")
    private fun fixPubmedRetrievalButtonClicked(
        a: String,
        fa: String,
        t: String,
        l: String,
        py: String,
        doi: String,
        oa: String
    ) {
        every { pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID) } returns
            PubmedArticleResult(pubmedArticleMock, HttpStatus.OK, null)
        every { pubmedArticleMock.pmId } returns java.lang.String.valueOf(PMID)
        every { pubmedArticleMock.authors } returns a
        every { pubmedArticleMock.firstAuthor } returns fa
        every { pubmedArticleMock.title } returns t
        every { pubmedArticleMock.location } returns l
        every { pubmedArticleMock.publicationYear } returns py
        every { pubmedArticleMock.doi } returns doi
        every { pubmedArticleMock.originalAbstract } returns oa
    }

    private fun verifyPubmedRetrievalButtonClicked(cnip: Int) {
        verify { pubmedArticleServiceMock.getPubmedArticleWithPmid(PMID) }
        verify { pubmedArticleMock.pmId }
        verify { pubmedArticleMock.authors }
        verify { pubmedArticleMock.firstAuthor }
        verify { pubmedArticleMock.title }
        verify { pubmedArticleMock.location }
        verify { pubmedArticleMock.publicationYear }
        verify { pubmedArticleMock.doi }
        verify { pubmedArticleMock.originalAbstract }
        verify(exactly = cnip) { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptArticle_warnsAboutDifferingArticle() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("_a", "fa", "t", "l", "2017", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed Authors: _a"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptFirstAuthor_warnsAboutDifferingFirstAuthor() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("a", "_fa", "t", "l", "2017", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed First Author: _fa"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptTitle_warnsAboutDifferingTitle() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("a", "fa", "_t", "l", "2017", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed Title: _t"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptLocation_warnsAboutDifferingLocation() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "_l", "2017", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed Location: _l"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptYear_warnsAboutDifferingYear() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2016", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed Pub. Year: 2016"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesEqualExceptDoi_warnsAboutDifferingDoi() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "_doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING), "PubMed DOI: _doi"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetAndAllDifferent_warnsAboutAllComparedFields() {
        tester.startComponentInPage(makePanel())
        fixPubmedRetrievalButtonClicked("_a", "_fa", "_t", "_l", "2016", "_doi", "_oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Authors: _a", "PubMed First Author: _fa", "PubMed Title: _t", "PubMed Pub. Year: 2016",
            "PubMed Location: _l", "PubMed DOI: _doi", "PubMed Original Abstract: _oa"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_withAheadOfPrintArticle_withFirstAuthorAndLocationStartAndOtherTitle_warns() {
        tester.startComponentInPage(makeAheadOfPrintPanel())
        fixPubmedRetrievalButtonClicked("_a", "fa", "_t", "J. FinalLocation.", "2016", "_doi", "_oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Authors: _a", "PubMed Title: _t", "PubMed Pub. Year: 2016",
            "PubMed Location: J. FinalLocation.", "PubMed DOI: _doi", "PubMed Original Abstract: _oa"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_withAheadOfPrintArticle_withTitleAndStartOfLocationAndOtherFirstAuthor_warns() {
        tester.startComponentInPage(makeAheadOfPrintPanel())
        fixPubmedRetrievalButtonClicked("_a", "_fa", "t", "J. FinalLocation.", "2016", "_doi", "_oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Authors: _a", "PubMed First Author: _fa", "PubMed Pub. Year: 2016",
            "PubMed Location: J. FinalLocation.", "PubMed DOI: _doi", "PubMed Original Abstract: _oa"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_withAheadOfPrintArticle_withTitleAndFirstAuthorAndOtherStartOfLocation_warns() {
        tester.startComponentInPage(makeAheadOfPrintPanel())
        fixPubmedRetrievalButtonClicked("_a", "fa", "t", "Y. FinalLocation.", "2016", "_doi", "_oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertFeedbackMessages(
            ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING),
            "PubMed Authors: _a", "PubMed Pub. Year: 2016",
            "PubMed Location: Y. FinalLocation.", "PubMed DOI: _doi", "PubMed Original Abstract: _oa"
        )
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_withAheadOfPrintArticle_withFirstAuthorAndTitleAndStartOfLocation_overwrites() {
        tester.startComponentInPage(makeAheadOfPrintPanel())
        fixPubmedRetrievalButtonClicked("_a", "fa", "t", "J. FinalLocation.", "2016", "_doi", "_oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages("Ahead-of-print article was updated from PubMed. Click save if you want to keep the changes.")
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_aheadOfPrint_withAllMatchingButInDifferentCapitalization_overwrites() {
        tester.startComponentInPage(makeAheadOfPrintPanel())
        fixPubmedRetrievalButtonClicked("_a", "FA", "T", "j. FinalLocation.", "2016", "_doi", "_oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages("Ahead-of-print article was updated from PubMed. Click save if you want to keep the changes.")
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun withNoPmId_PubmedRetrievalLinkIsNotEnabled() {
        tester.startComponentInPage(makePanelWithEmptyPaper(null))
        tester.assertDisabled("$PANEL_ID:form:pubmedRetrieval")
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithNoOtherValuesSet_setsThemFromPubmedIncludingOriginalAbstract() {
        tester.startComponentInPage(makePanelWithEmptyPaper(PMID))
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa")
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:4:link")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages(
            "Some fields have changed (Authors, First Author, Title, Pub. Year, Location, " +
                "DOI, Original Abstract). Click save if you want to keep the changes."
        )
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(2)
    }

    private fun testAndVerifySingleFieldSet(panel: EditablePaperPanel, field: String) {
        tester.startComponentInPage(panel)
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages("Some fields have changed ($field). Click save if you want to keep the changes.")
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(1)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptAuthor_setsAuthor() {
        val panel = makePanel()
        panel.modelObject.authors = null
        testAndVerifySingleFieldSet(panel, "Authors")
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptFirstAuthor_setsFirstAuthor() {
        val panel = makePanel()
        panel.modelObject.firstAuthor = null
        testAndVerifySingleFieldSet(panel, "First Author")
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptTitle_setsTitle() {
        val panel = makePanel()
        panel.modelObject.title = null
        testAndVerifySingleFieldSet(panel, "Title")
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptLocation_setsLocation() {
        val panel = makePanel()
        panel.modelObject.location = null
        testAndVerifySingleFieldSet(panel, "Location")
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptYear_setsYear() {
        val panel = makePanel()
        panel.modelObject.publicationYear = null
        testAndVerifySingleFieldSet(panel, "Pub. Year")
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptDoi_setsDoi() {
        val panel = makePanel()
        panel.modelObject.doi = null
        testAndVerifySingleFieldSet(panel, "DOI")
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithAllValuesSetExceptOriginalAbstract_setsOriginalAbstract() {
        val panel = makePanel()
        panel.modelObject.originalAbstract = null
        tester.startComponentInPage(panel)
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "2017", "doi", "oa")
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:4:link")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages(
            "Some fields have changed (Original Abstract). Click save if you want to keep the changes."
        )
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertNoErrorMessage()
        verifyPubmedRetrievalButtonClicked(2)
    }

    @Test
    fun clickingOnPubmedRetrievalButton_withMatchingPmId_andWithNoOtherValuesSet_butWithInvalidYear_warnsAboutYearButSetsOtherFields() {
        tester.startComponentInPage(makePanelWithEmptyPaper(PMID))
        fixPubmedRetrievalButtonClicked("a", "fa", "t", "l", "invalid", "doi", "oa")
        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:4:link")
        tester.executeAjaxEvent("$PANEL_ID:form:pubmedRetrieval", "click")
        tester.assertInfoMessages(
            "Some fields have changed (Authors, First Author, Title, Location, " +
                "DOI, Original Abstract). Click save if you want to keep the changes."
        )
        tester.assertFeedbackMessages(ExactLevelFeedbackMessageFilter(FeedbackMessage.WARNING))
        tester.assertErrorMessages("Unable to parse the year 'invalid'")
        verifyPubmedRetrievalButtonClicked(2)
    }

    @Test
    fun clickingExclude_withBothSearchOrderIdAndPaperId_excludesPaperFromSearchOrder_andForwardsToPaperSearchPage() {
        every { itemNavigatorMock.itemWithFocus } returns null
        tester.startComponentInPage(makePanelWith(PMID, callingPageDummy, SEARCH_ORDER_ID, false))
        val formTester = tester.newFormTesterSameSite("$PANEL_ID:form")
        formTester.submit("exclude")
        tester.assertRenderedPage(PaperSearchPage::class.java)
        verify { paperServiceMock.excludeFromSearchOrder(SEARCH_ORDER_ID, 1L) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun clickingExclude_showingExcluded_reIncludesPaperIntoSearchOrder_andSkipsToNextPaper() {
        val idOfNextPaper = 10L
        every { itemNavigatorMock.itemWithFocus } returns idOfNextPaper
        every { paperServiceMock.findById(idOfNextPaper) } returns Optional.of(mockk())
        tester.startComponentInPage(makePanelWith(PMID, callingPageDummy, SEARCH_ORDER_ID, true))
        val formTester = tester.newFormTesterSameSite("$PANEL_ID:form")
        invoking {
            formTester.submit("exclude")
        } shouldThrow Exception::class withMessage "forward to calling page triggered"
        verify { paperServiceMock.reincludeIntoSearchOrder(SEARCH_ORDER_ID, 1L) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
        verify { itemNavigatorMock.itemWithFocus }
        verify { paperServiceMock.findById(idOfNextPaper) }
    }

    @Test
    fun clickingExclude_showingExcluded_reIncludesPaperIntoSearchOrder_andForwardsToPaperSearchPage() {
        every { itemNavigatorMock.itemWithFocus } returns null
        tester.startComponentInPage(makePanelWith(PMID, callingPageDummy, SEARCH_ORDER_ID, true))
        val formTester = tester.newFormTesterSameSite("$PANEL_ID:form")
        formTester.submit("exclude")
        tester.assertRenderedPage(PaperSearchPage::class.java)
        verify { paperServiceMock.reincludeIntoSearchOrder(SEARCH_ORDER_ID, 1L) }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun startingPage_notShowingExclusions_adjustsIconAndTitleOfToggleInclusionsButton() {
        assertExcluded(
            false,
            "Exclude paper from current search",
            "fa-ban"
        )
    }

    private fun assertExcluded(showingExclusion: Boolean, titleValue: String, iconValue: String) {
        tester.startComponentInPage(makePanelWith(PMID, callingPageDummy, SEARCH_ORDER_ID, showingExclusion))
        val responseTxt = tester.lastResponse.document
        val tagTester = TagTester.createTagByAttribute(responseTxt, "title", titleValue)
        tagTester.shouldNotBeNull()
        tagTester.name shouldBeEqualTo "button"
        tagTester.value shouldContain "<i class=\"fa-solid $iconValue\"></i>"
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }

    @Test
    fun startingPageShowingExclusions_adjustsIconAndTitleOfToggleInclusionsButton() {
        assertExcluded(
            true,
            "Re-include paper into current search", "fa-circle-check"
        )
    }

    @Test
    fun clickingBack_withoutHavingModifiedExclusionList_forwardsToPaperSearchPageViaCallingPage() {
        val callingPageRefMock = mockk<PageReference> {
            every { page } throws RuntimeException("forward to calling page triggered")
        }
        tester.startComponentInPage(makePanelWith(PMID, callingPageRefMock, SEARCH_ORDER_ID, true))
        val formTester = tester.newFormTesterSameSite("$PANEL_ID:form")
        invoking {
            formTester.submit("back")
        } shouldThrow Exception::class withMessage "forward to calling page triggered"
        verify { callingPageRefMock.page }
        verify { newsletterServiceMock.canCreateNewsletterInProgress() }
    }
}
