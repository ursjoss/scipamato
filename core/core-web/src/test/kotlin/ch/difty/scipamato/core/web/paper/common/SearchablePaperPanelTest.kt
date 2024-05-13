package ch.difty.scipamato.core.web.paper.common

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.entity.search.SearchCondition
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class SearchablePaperPanelTest : PaperPanelTest<SearchCondition, SearchablePaperPanel>() {

    override fun makePanel(): SearchablePaperPanel = newPanel()

    private fun newPanel(attachments: Boolean? = null, attachmentName: String? = null): SearchablePaperPanel {
        val sc = SearchCondition().apply {
            id = "1"
            number = "100"
            authors = "a"
            firstAuthor = "fa"
            isFirstAuthorOverridden = false
            title = "t"
            location = "l"
            publicationYear = "2017"
            pmId = "pmid"
            doi = "doi"
            createdDisplayValue = "cdv"
            modifiedDisplayValue = "lmdv"
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
            intern = "i"
            resultMeasuredOutcome = "rmo"
            resultExposureRange = "rer"
            resultEffectEstimate = "ree"
            conclusion = "cc"
            comment = "c"
            addCode(newC(1, "F"))
            mainCodeOfCodeclass1 = "mcocc1"
            addCode(newC(2, "A"))
            addCode(newC(3, "A"))
            addCode(newC(4, "A"))
            addCode(newC(5, "A"))
            addCode(newC(6, "A"))
            addCode(newC(7, "A"))
            addCode(newC(8, "A"))
            codesExcluded = "1B 2C"
            originalAbstract = "oa"
            hasAttachments = attachments
            attachmentNameMask = attachmentName
        }
        return object : SearchablePaperPanel("panel", Model.of(sc)) {
            private val serialVersionUID: Long = 1L
            override fun onFormSubmit() {
                // no-op
            }

            override fun restartSearchInPaperSearchPage() {
                // no-op
            }

            override fun doOnSubmit() {
                // no-op
            }
        }
    }

    override fun assertSpecificComponents() {
        var b = "panel"
        tester.assertComponent(b, SearchablePaperPanel::class.java)
        assertCommonComponents(b)
        b += ":form"
        assertTextFieldWithLabel("$b:id", "1", "ID")
        assertTextFieldWithLabel("$b:number", "100", "SciPaMaTo-Core-No.")
        assertTextFieldWithLabel("$b:publicationYear", "2017", "Pub. Year")
        assertTextFieldWithLabel("$b:pmId", "pmid", "PMID")
        tester.assertLabel("$b:submit:label", "Search")
        assertTextFieldWithLabel("$b:createdDisplayValue", "cdv", "Created")
        assertTextFieldWithLabel("$b:modifiedDisplayValue", "lmdv", "Last Modified")
        tester.assertComponent("$b:submit", BootstrapButton::class.java)
        verifyCodeAndCodeClassCalls(1)

        tester.clickLinkSameSite("panel:form:tabs:tabs-container:tabs:5:link")
        val bb = "$b:tabs:panel"
        val bbb = "$bb:tab6Form"
        assertTextFieldWithLabel("$bbb:attachmentNameMask", null, "Attachment Name Mask")
        assertComponentWithLabel("$bbb:hasAttachments", CheckBoxX::class.java, null, "W/ or w/o Attachments")

        tester.assertComponent(bbb, Form::class.java)
    }

    @Test
    fun specificFields_areEnabled() {
        tester.startComponentInPage(makePanel())
        tester.isEnabled("panel:form:id")
        tester.isEnabled("panel:form:number")
        tester.isEnabled("panel:form:firstAuthorOverridden")
        tester.isEnabled("panel:form:createdDisplayValue")
        tester.isEnabled("panel:form:modifiedDisplayValue")
    }

    @Test
    fun summary_doesNotExist() {
        tester.startComponentInPage(makePanel())
        tester.assertContainsNot("panel:form:summary")
    }

    @Test
    fun summaryShort_doesNotExist() {
        tester.startComponentInPage(makePanel())
        tester.assertContainsNot("panel:form:summaryShort")
    }

    @Test
    fun navigationButtons_andPubmedRetrieval_andBackButton_areInvisible() {
        tester.startComponentInPage(makePanel())
        tester.assertInvisible("panel:form:previous")
        tester.assertInvisible("panel:form:next")
        tester.assertInvisible("panel:form:pubmedRetrieval")
        tester.assertInvisible("panel:form:back")
    }

    @Test
    fun assertSubmit() {
        tester.startComponentInPage(makePanel())
        applyTestHackWithNestedMultiPartForms()
        tester.submitForm("panel:form")
    }

    @Test
    fun gettingCallingPage_isNull() {
        val panel = tester.startComponentInPage(makePanel())
        panel.callingPage.shouldBeNull()
    }

    @Test
    fun isNotAssociatedWithNewsletter() {
        makePanel().isAssociatedWithNewsletter.shouldBeFalse()
    }

    @Test
    fun isNotAssociatedWithWipNewsletter() {
        makePanel().isAssociatedWithWipNewsletter.shouldBeFalse()
    }

    @Test
    fun isNotNewsletterInStatusWip() {
        makePanel().isaNewsletterInStatusWip().shouldBeFalse()
    }

    @Test
    fun modifyNewsletterAssociation_isNoOp() {
        val targetDummy = AjaxRequestTargetSpy()
        makePanel().modifyNewsletterAssociation(targetDummy)
        targetDummy.components.isEmpty()
        targetDummy.javaScripts.isEmpty()
    }

    @Test
    fun withExcludedCodeFilter() {
        tester.startComponentInPage(newPanel())
        val bbb = prepareCodePanel()
        assertTextFieldWithLabel("$bbb:codesExcluded", "1B 2C", "Excluded Codes")
        verifyCodeAndCodeClassCalls(1)
    }

    @Test
    fun withAttachmentFilter_havingAttachments() {
        tester.startComponentInPage(newPanel(attachments = true))
        val bbb = prepareAttachmentPanel()
        assertTextFieldWithLabel("$bbb:attachmentNameMask", null, "Attachment Name Mask")
        assertComponentWithLabel("$bbb:hasAttachments", CheckBoxX::class.java, true, "W/ or w/o Attachments")
    }

    @Test
    fun withAttachmentFilter_notHavingAttachments() {
        tester.startComponentInPage(newPanel(attachments = false))
        val bbb = prepareAttachmentPanel()
        assertTextFieldWithLabel("$bbb:attachmentNameMask", null, "Attachment Name Mask")
        assertComponentWithLabel("$bbb:hasAttachments", CheckBoxX::class.java, false, "W/ or w/o Attachments")
    }

    @Test
    fun withAttachmentFilter_havingAttachmentFilterWithAttachmentName() {
        tester.startComponentInPage(newPanel(attachmentName = "foo"))
        val bbb = prepareAttachmentPanel()
        assertTextFieldWithLabel("$bbb:attachmentNameMask", "foo", "Attachment Name Mask")
        assertComponentWithLabel("$bbb:hasAttachments", CheckBoxX::class.java, null, "W/ or w/o Attachments")
    }

    private fun prepareCodePanel() = prepareTabPanel(tabIndex = 2)
    private fun prepareAttachmentPanel() = prepareTabPanel(tabIndex = 5)

    private fun prepareTabPanel(tabIndex: Int): String {
        val b = "panel:form:tabs"
        tester.clickLinkSameSite("$b:tabs-container:tabs:$tabIndex:link")
        return "$b:panel:tab${tabIndex + 1}Form"
    }
}
