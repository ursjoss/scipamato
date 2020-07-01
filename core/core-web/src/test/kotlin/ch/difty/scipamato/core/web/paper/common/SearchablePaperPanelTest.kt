package ch.difty.scipamato.core.web.paper.common

import ch.difty.scipamato.core.entity.search.SearchCondition
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.unmockkAll
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class SearchablePaperPanelTest : PaperPanelTest<SearchCondition?, SearchablePaperPanel>() {

    override fun makePanel(): SearchablePaperPanel {
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
            originalAbstract = "oa"
        }
        return object : SearchablePaperPanel("panel", Model.of(sc)) {
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
        val targetMock = mockk<AjaxRequestTarget>()
        makePanel().modifyNewsletterAssociation(targetMock)
        confirmVerified(targetMock)
        unmockkAll()
    }
}
