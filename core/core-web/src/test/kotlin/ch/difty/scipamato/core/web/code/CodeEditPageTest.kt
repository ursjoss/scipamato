package ch.difty.scipamato.core.web.code

import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.authentication.LogoutPage
import ch.difty.scipamato.core.web.code.CodeEditHeaderPanel.CodeMustMatchCodeClassValidator
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.feedback.FeedbackMessage
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.repeater.RefreshingView
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException

@Suppress("SpellCheckingInspection", "PrivatePropertyName")
internal class CodeEditPageTest : BasePageTest<CodeEditPage>() {

    private lateinit var codeDefinitionDummy: CodeDefinition
    private lateinit var formDummy: Form<*>

    private lateinit var codeField: TextField<String>
    private lateinit var codeClasses: BootstrapSelect<CodeClass>

    private val cc1 = CodeClass(1, "CC1", "c1")
    private val cc2 = CodeClass(2, "Region", "d2")

    private val kt_de = CodeTranslation(1, "de", "Name1", "some comment", 1)
    private val kt_de2 = CodeTranslation(1, "de", "Name1a", null, 1)
    private val kt_en = CodeTranslation(2, "en", "name1", null, 1)
    private val kt_fr = CodeTranslation(3, "fr", "nom1", null, 1)
    private val cd = CodeDefinition("2A", "de", cc2, 1, false, 1, kt_de, kt_en, kt_fr, kt_de2)

    @Suppress("LocalVariableName")
    public override fun setUpHook() {
        codeDefinitionDummy = mockk()
        formDummy = mockk()

        codeField = TextField("code")
        codeClasses = BootstrapSelect("codeClasses")

        every { codeClassServiceMock.find(any()) } returns listOf(cc2)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeServiceMock)
        tester.destroy()
        unmockkAll()
    }

    override fun makePage(): CodeEditPage = CodeEditPage(Model.of(cd), null)

    override val pageClass: Class<CodeEditPage>
        get() = CodeEditPage::class.java

    public override fun assertSpecificComponents() {
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":headerPanel:"
        var bb = b + "code"
        tester.assertLabel(bb + "Label", "Code")
        tester.assertModelValue(bb, "2A")
        bb = b + "codeClass"
        tester.assertLabel(bb + "Label", "Code Class")
        tester.assertComponent(bb, BootstrapSelect::class.java)
        tester.assertModelValue(bb, cc2)
        tester.assertContains("<option selected=\"selected\" value=\"2\">2 - Region</option>")
        bb = b + "sort"
        tester.assertLabel(bb + "Label", "Sort")
        tester.assertComponent(bb, TextField::class.java)
        bb = b + "internal"
        tester.assertLabel(bb + "Label", "Internal")
        tester.assertComponent(bb, CheckBoxX::class.java)
        tester.assertComponent(b + "back", BootstrapButton::class.java)
        tester.assertComponent(b + "submit", BootstrapButton::class.java)
        tester.assertComponent(b + "delete", BootstrapButton::class.java)
        bb = "form:translations"
        tester.assertLabel(bb + "Label", "Code Translations and Comments")
        bb += "Panel:translations"
        tester.assertComponent(bb, RefreshingView::class.java)
        bb += ":"
        assertTranslation(bb, 1, "de", "Name1", "some comment")
        assertTranslation(bb, 2, "de", "Name1a", null)
        assertTranslation(bb, 3, "en", "name1", null)
        assertTranslation(bb, 4, "fr", "nom1", null)
    }

    private fun assertTranslation(bb: String, idx: Int, langCode: String, name: String, comment: String?) {
        tester.assertLabel("$bb$idx:langCode", langCode)
        tester.assertComponent("$bb$idx:name", TextField::class.java)
        tester.assertModelValue("$bb$idx:name", name)
        tester.assertComponent("$bb$idx:comment", TextField::class.java)
        tester.assertModelValue("$bb$idx:comment", comment)
    }

    @Test
    fun submitting_withSuccessfulServiceCall_addsInfoMsg() {
        every { codeServiceMock.saveOrUpdate(any()) } returns cd
        runSubmitTest()
        tester.assertInfoMessages("Successfully saved code 2A: DE: 'foo','Name1a'; EN: 'name1'; FR: 'nom1'.")
        tester.assertNoErrorMessage()
    }

    private fun runSubmitTest() {
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.setValue("translationsPanel:translations:1:name", "foo")
        assertTranslation("form:translationsPanel:translations:", 1, "de", "Name1", "some comment")
        formTester.submit("headerPanel:submit")
        assertTranslation("form:translationsPanel:translations:", 5, "de", "foo", "some comment")
        verify { codeServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        every { codeServiceMock.saveOrUpdate(any()) } returns null
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Could not save code 2A.")
    }

    @Test
    fun submitting_withOptimisticLockingException_addsErrorMsg() {
        every { codeServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The tblName with id 2A has been modified concurrently " +
                "by another user. Please reload it and apply your changes once more."
        )
    }

    @Test
    fun submitting_withOtherException_addsErrorMsg() {
        every { codeServiceMock.saveOrUpdate(any()) } throws RuntimeException("fooMsg")
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save the code 2A: fooMsg")
    }

    @Test
    fun submitting_withDuplicateKeyConstraintViolationException_addsErrorMsg() {
        val msg = (
            "...Detail: Key (code_class_id, sort)=(2, 1) already exists.; " +
                "nested exception is org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint..."
            )
        every { codeServiceMock.saveOrUpdate(any()) } throws DuplicateKeyException(msg)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("The sort index 1 is already in use for codes of code class 2.")
    }

    @Test
    fun submitting_withDuplicateKeyConstraintViolationException_withUnexpectedMsg_addsThatErrorMsg() {
        val msg = "something unexpected happened"
        every { codeServiceMock.saveOrUpdate(any()) } throws DuplicateKeyException(msg)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("something unexpected happened")
    }

    @Test
    fun submitting_withNullCode_preventsSave() {
        assertCodeCodeClassMismatch(null)
    }

    @Test
    fun submitting_withBlankCode_preventsSave() {
        assertCodeCodeClassMismatch("")
    }

    @Test
    fun submitting_withCodeCodeClassMismatch_preventsSave() {
        assertCodeCodeClassMismatch("3A")
    }

    private fun assertCodeCodeClassMismatch(code: String?) {
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.setValue("headerPanel:code", code)
        formTester.submit("headerPanel:submit")
        tester.assertErrorMessages("The first digit of the Code must match the Code Class Number.")
        verify(exactly = 0) { codeServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submittingDelete_delegatesDeleteToService() {
        every { codeServiceMock.delete(any(), any()) } returns codeDefinitionDummy
        every { codeServiceMock.getCodeClass1("en_us") } returns cc1
        every { codeServiceMock.countByFilter(any()) } returns 0
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")
        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
        verify { codeServiceMock.delete("2A", 1) }
        verify(exactly = 0) { codeServiceMock.saveOrUpdate(any()) }
        verify { codeServiceMock.getCodeClass1("en_us") }
        verify { codeServiceMock.countByFilter(any()) }
    }

    @Test
    fun submittingDelete_withServiceReturningNull_informsAboutRepoError() {
        every { codeServiceMock.delete(any(), any()) } returns null
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Could not delete code 2A.")
        verify { codeServiceMock.delete("2A", 1) }
        verify(exactly = 0) { codeServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submittingDelete_withForeignKeyConstraintViolationException_addsErrorMsg() {
        val msg = "... is still referenced from table \"paper_code\".; nested exception is org.postgresql.util.PSQLException..."
        every { codeServiceMock.delete(any(), any()) } throws DataIntegrityViolationException(msg)
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("You cannot delete code '2A' as it is still assigned to at least one paper.")
        verify { codeServiceMock.delete(any(), any()) }
    }

    @Test
    fun submittingDelete_withOptimisticLockingException_addsErrorMsg() {
        every { codeServiceMock.delete(any(), any()) } throws
            OptimisticLockingException("code_class", OptimisticLockingException.Type.DELETE)
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The code_class with id 2A has been modified concurrently by another user. Please reload it and apply your changes once more."
        )
        verify { codeServiceMock.delete(any(), any()) }
    }

    @Test
    fun submittingDelete_withException_addsErrorMsg() {
        every { codeServiceMock.delete(any(), any()) } throws RuntimeException("boom")
        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to delete code 2A: boom")
        verify { codeServiceMock.delete(any(), any()) }
    }

    @Test
    fun clickingBackButton_withPageWithoutCallingPageRef_forwardsToCodeListPage() {
        every { codeServiceMock.getCodeClass1("en_us") } returns cc1
        every { codeServiceMock.countByFilter(any()) } returns 0

        tester.startPage(CodeEditPage(Model.of(cd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(CodeListPage::class.java)

        // from CodeListPage
        verify { codeServiceMock.getCodeClass1("en_us") }
        verify { codeServiceMock.countByFilter(any()) }
    }

    @Test
    fun clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        tester.startPage(CodeEditPage(Model.of(cd), LogoutPage(PageParameters()).pageReference))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(LogoutPage::class.java)
    }

    @Test
    fun withNullCodeClasses_failsValidation() {
        codeClasses.convertedInput.shouldBeNull()
        assertValidationDidNotPass()
    }

    @Test
    fun withNonNullCodeClass_withNullCode_failsValidation() {
        codeClasses.convertedInput = CodeClass(1, "CC1", "")
        codeField.convertedInput.shouldBeNull()
        assertValidationDidNotPass()
    }

    @Test
    fun withNonNullCodeClass_withBlankCode_failsValidation() {
        codeClasses.convertedInput = CodeClass(1, "CC1", "")
        codeField.convertedInput = ""
        assertValidationDidNotPass()
    }

    private fun assertValidationDidNotPass() {
        val validator = CodeMustMatchCodeClassValidator(codeField, codeClasses)
        validator.validate(formDummy)
        codeField.feedbackMessages.hasMessage(FeedbackMessage.ERROR).shouldBeTrue()
    }
}
