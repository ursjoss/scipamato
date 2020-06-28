package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.common.SelfUpdatingPageTest
import ch.difty.scipamato.core.web.paper.common.PaperPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldNotBeNull
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.util.tester.FormTester
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class PaperEntryPageTest : SelfUpdatingPageTest<PaperEntryPage>() {

    @MockK(relaxed = true)
    private lateinit var persistedPaperMock: Paper

    override fun makePage(): PaperEntryPage = PaperEntryPage(Model.of(Paper()), null)

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
        val id = 0L
        every { persistedPaperMock.id } returns id
        every { paperServiceMock.saveOrUpdate(any()) } returns persistedPaperMock

        tester.startPage(makePage())
        val formTester = makeSavablePaperTester()
        formTester.submit()

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
        verify(exactly = 2) { itemNavigatorMock.setIdToHeadIfNotPresent(id) }
    }

    @Test
    fun submitting_withPersistedPaper_doesNotAddsIdToPaperManagerAfterSaving() {
        val id = 1L
        every { persistedPaperMock.id } returns id
        every { paperServiceMock.saveOrUpdate(any()) } returns persistedPaperMock

        tester.startPage(makePage())
        val formTester = makeSavablePaperTester()
        formTester.submit()

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()

        verify(exactly = 2) { paperServiceMock.saveOrUpdate(any()) }
        verify(exactly = 1) { itemNavigatorMock.setIdToHeadIfNotPresent(id) }
    }

    @Test
    fun paperFailingValidation_showsAllValidationMessages() {
        tester.startPage(makePage())
        applyTestHackWithNestedMultiPartForms()

        tester.submitForm("contentPanel:form")
        tester.assertErrorMessages(
            "'Authors' is required.", "'Title' is required.", "'Location' is required.",
            "'Pub. Year' is required.", "'SciPaMaTo-No.' is required.", "'Goals' is required."
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
        tester.newFormTester("contentPanel:form").apply {
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
        every { persistedPaperMock.id } returns 10L
        every { paperServiceMock.saveOrUpdate(any()) } returns persistedPaperMock
        every { paperServiceMock.findLowestFreeNumberStartingFrom(7L) } returns 19L

        tester.startPage(PaperEntryPage(PageParameters(), null))
        val formTester = tester.newFormTester("contentPanel:form")

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
        verify { paperServiceMock.findLowestFreeNumberStartingFrom(7L) }
    }

    @Test
    fun eventTest() {
        every { persistedPaperMock.id } returns 3L

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
}
