package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.common.SelfUpdatingPageTest
import ch.difty.scipamato.core.web.paper.common.PaperPanel
import ch.difty.scipamato.newFormTesterSameSite
import ch.difty.scipamato.submitFormSameSite
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldNotBeNull
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.util.tester.FormTester
import org.junit.jupiter.api.Test

private const val ID = 1L

@Suppress("SpellCheckingInspection")
internal class PaperEntryPageTest : SelfUpdatingPageTest<PaperEntryPage>() {

    private val persistedPaper = Paper().apply { id = ID }

    override fun makePage(): PaperEntryPage = PaperEntryPage(Model.of(Paper().apply { id = 0L }), null)

    override val pageClass: Class<PaperEntryPage>
        get() = PaperEntryPage::class.java

    override fun assertSpecificComponents() {
        var b = "contentPanel"
        tester.assertComponent(b, PaperPanel::class.java)
        b += ":form"
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextArea(b, "authors")
        assertLabeledTextField(b, "firstAuthor")
        assertLabeledCheckBoxX(b, "firstAuthorOverridden")
        assertLabeledTextArea(b, "title")
        assertLabeledTextField(b, "location")
        assertLabeledTextField(b, "id")
        assertLabeledTextField(b, "publicationYear")
        assertLabeledTextField(b, "pmId")
        assertLabeledTextField(b, "doi")
        b += ":tabs"
        tester.assertComponent(b, BootstrapTabbedPanel::class.java)
        b += ":panel"
        assertTabPanelFields(
            1, b, "goals", "population", "methods",
            "populationPlace", "populationParticipants", "populationDuration",
            "exposurePollutant", "exposureAssessment", "methodStudyDesign",
            "methodOutcome", "methodStatistics", "methodConfounders"
        )
    }

    @Suppress("SameParameterValue")
    private fun assertTabPanelFields(tabId: Int, b: String, vararg fields: String) {
        val bb = b + ":tab" + tabId + "Form"
        tester.assertComponent(bb, Form::class.java)
        fields.forEach { f -> assertLabeledTextArea(bb, f) }
    }

    @Test
    fun submitting_withNewPaper_addsIdToPaperManager() {
        every { paperServiceMock.saveOrUpdate(any()) } returns persistedPaper

        tester.startPage(makePage())
        val formTester = makeSavablePaperTester()
        formTester.submit()

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
        verify(exactly = 1) { itemNavigatorMock.setIdToHeadIfNotPresent(ID) }
    }

    @Test
    fun submitting_withPersistedPaper_doesNotAddsIdToPaperManagerAfterSaving() {
        every { paperServiceMock.saveOrUpdate(any()) } returns persistedPaper

        tester.startPage(makePage())
        val formTester = makeSavablePaperTester()
        formTester.submit()

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
        verify(exactly = 1) { itemNavigatorMock.setIdToHeadIfNotPresent(ID) }
    }

    @Test
    fun paperFailingValidation_showsAllValidationMessages() {
        tester.startPage(makePage())
        applyTestHackWithNestedMultiPartForms()

        tester.submitFormSameSite("contentPanel:form")
        tester.assertErrorMessages(
            "'Authors' is required.", "'Title' is required.", "'Location' is required.",
            "'Pub. Year' is required.", "'SciPaMaTo-Core-No.' is required.", "'Goals' is required."
        )
    }

    // See https://issues.apache.org/jira/browse/WICKET-2790
    private fun applyTestHackWithNestedMultiPartForms() = tester.request.setUseMultiPartContentType(true)

    @Test
    fun serviceThrowingError() {
        every { paperServiceMock.saveOrUpdate(any()) } throws RuntimeException("foo")

        tester.startPage(makePage())
        val formTester = makeSavablePaperTester()
        formTester.submit()

        tester.assertErrorMessages(
            "An unexpected error occurred when trying to save Paper [id 0]: foo",
            "An unexpected error occurred when trying to save Paper [id 0]: foo"
        )

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
    }

    private fun makeSavablePaperTester(): FormTester =
        tester.newFormTesterSameSite("contentPanel:form").apply {
            setValue("number", "100")
            setValue("authors", "Poe EA.")
            setValue("title", "Title")
            setValue("location", "loc")
            setValue("publicationYear", "2017")
            setValue("tabs:panel:tab1Form:goals", "goals")
        }

    @Test
    fun serviceThrowingOptimisticLockingException() {
        every { paperServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("paper", "rcd", OptimisticLockingException.Type.UPDATE)

        tester.startPage(makePage())
        makeSavablePaperTester().submit()

        val msg = "The paper with id 0 has been modified concurrently by another user. " +
            "Please reload it and apply your changes once more."
        tester.assertErrorMessages(msg, msg)
        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun serviceReturningNullPaperAfterSave_hasErrorMessage() {
        every { paperServiceMock.saveOrUpdate(any()) } returns null

        tester.startPage(makePage())
        makeSavablePaperTester().submit()

        tester.assertNoInfoMessage()
        val msg = "An unexpected error occurred when trying to save Paper [id 0]: "
        tester.assertErrorMessages(msg, msg)

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun defaultModel_containsNaValuesAndCanSubmitWithoutErrors() {
        every { paperServiceMock.saveOrUpdate(any()) } returns persistedPaper
        every { paperServiceMock.findLowestFreeNumberStartingFrom(7L) } returns 19L

        tester.startPage(PaperEntryPage(PageParameters(), null))
        val formTester = tester.newFormTesterSameSite("contentPanel:form")

        formTester.getTextComponentValue("number").shouldNotBeNull()
        formTester.getTextComponentValue("authors").shouldNotBeNull()
        formTester.getTextComponentValue("firstAuthor").shouldNotBeNull()
        formTester.getTextComponentValue("title").shouldNotBeNull()
        formTester.getTextComponentValue("location").shouldNotBeNull()
        formTester.getTextComponentValue("publicationYear").shouldNotBeNull()
        formTester.getTextComponentValue("tabs:panel:tab1Form:goals").shouldNotBeNull()

        formTester.submit()

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
        verify { paperServiceMock.findLowestFreeNumberStartingFrom(any()) }
    }

    @Test
    fun eventTest() {
        every { paperServiceMock.mergePaperIntoWipNewsletter(any(), any(),any()) } returns java.util.Optional.empty()

        tester.startPage(makePage())

        tester.executeAjaxEvent("contentPanel:form:modAssociation", "click")

        tester.assertComponentOnAjaxResponse("contentPanel")
        tester.assertComponentOnAjaxResponse("feedback")
    }

    @Test
    fun canStartPageWithDefaultConstructor() {
        tester.startPage(PaperEntryPage(PageParameters()))
        tester.assertRenderedPage(PaperEntryPage::class.java)
    }

    @Test
    fun canModifyMultipleCodeClasses() {
        every { codeClassServiceMock.find("en_us") } returns listOf(CodeClass(1, "CC1", ""))
        every { codeServiceMock.findCodesOfClass(any(), "en_us") } returns listOf(
            Code("C1", "C1", "", false, 1, "CC1", "", 1),
            Code("C2", "C2", "", false, 1, "CC1", "", 2)
        )

        tester.startPage(makePage())
        tester.clickLinkSameSite("contentPanel:form:tabs:tabs-container:tabs:2:link")

        val formTester = tester.newFormTesterSameSite("contentPanel:form").apply {
            setValue("authors", "Poe EA.")
            setValue("title", "Title")
            setValue("location", "loc")
            setValue("publicationYear", "2017")

            val indices = IntArray(2)
            indices[0] = 1
            indices[1] = 1
            selectMultiple("tabs:panel:tab3Form:codesClass1", indices, true)
        }
        tester.executeAjaxEvent("contentPanel:form:tabs:panel:tab3Form:codesClass1", "change")
        formTester.submit()

        verify(exactly = 1) { paperServiceMock.saveOrUpdate(any()) }
    }
}
