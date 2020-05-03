package ch.difty.scipamato.core.web.codeclass

import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.authentication.LogoutPage
import ch.difty.scipamato.core.web.code.CodeListPage
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.repeater.RefreshingView
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException

internal class CodeClassEditPageTest : BasePageTest<CodeClassEditPage>() {

    private lateinit var ccd: CodeClassDefinition

    @Suppress("LocalVariableName")
    public override fun setUpHook() {
        val cct_de = CodeClassTranslation(1, "de", "Name1", "some description", 1)
        val cct_de2 = CodeClassTranslation(1, "de", "Name1a", null, 1)
        val cct_en = CodeClassTranslation(2, "en", "name1", null, 1)
        val cct_fr = CodeClassTranslation(3, "fr", "nom1", null, 1)
        ccd = CodeClassDefinition(1, "de", 1, cct_de, cct_en, cct_fr, cct_de2)
        every { codeClassServiceMock.find(any()) } returns listOf(
            CodeClass(1, "cc1", "d1"),
            CodeClass(2, "cc2", "d2"),
            CodeClass(3, "cc3", "d3")
        )
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeClassServiceMock)
    }

    override fun makePage(): CodeClassEditPage = CodeClassEditPage(Model.of(ccd), null)

    override val pageClass: Class<CodeClassEditPage>
        get() = CodeClassEditPage::class.java

    public override fun assertSpecificComponents() {
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":headerPanel:"
        var bb = b + "id"
        with(tester) {
            assertLabel(bb + "Label", "ID")
            assertComponent(bb, TextField::class.java)
            assertModelValue(bb, 1)
            assertDisabled(bb)
            assertComponent(b + "back", BootstrapButton::class.java)
            assertComponent(b + "submit", BootstrapButton::class.java)
            bb = "form:translations"
            assertLabel(bb + "Label", "Code Class Translations and Descriptions")
            bb += "Panel:translations"
            assertComponent(bb, RefreshingView::class.java)
        }
        bb += ":"
        assertTranslation(bb, 1, "de", "Name1", "some description")
        assertTranslation(bb, 2, "de", "Name1a", null)
        assertTranslation(bb, 3, "en", "name1", null)
        assertTranslation(bb, 4, "fr", "nom1", null)
    }

    private fun assertTranslation(bb: String, idx: Int, langCode: String, name: String, description: String?) =
        with(tester) {
            assertLabel("$bb$idx:langCode", langCode)
            assertComponent("$bb$idx:name", TextField::class.java)
            assertModelValue("$bb$idx:name", name)
            assertComponent("$bb$idx:description", TextField::class.java)
            assertModelValue("$bb$idx:description", description)
        }

    @Test
    fun submitting_withSuccessfulServiceCall_addsInfoMsg() {
        every { codeClassServiceMock.saveOrUpdate(any()) } returns ccd
        runSubmitTest()
        tester.assertInfoMessages("Successfully saved code class 1: DE: 'foo','Name1a'; EN: 'name1'; FR: 'nom1'.")
        tester.assertNoErrorMessage()
    }

    private fun runSubmitTest() {
        tester.startPage(CodeClassEditPage(Model.of(ccd), null))
        val formTester = tester.newFormTester("form")
        formTester.setValue("translationsPanel:translations:1:name", "foo")
        assertTranslation("form:translationsPanel:translations:", 1, "de", "Name1", "some description")
        formTester.submit("headerPanel:submit")
        assertTranslation("form:translationsPanel:translations:", 5, "de", "foo", "some description")
        verify { codeClassServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withOptimisticLockingException_addsErrorMsg() {
        every { codeClassServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("The tblName with id 1 has been modified concurrently "
            + "by another user. Please reload it and apply your changes once more.")
    }

    @Test
    fun submitting_withOtherException_addsErrorMsg() {
        every { codeClassServiceMock.saveOrUpdate(any()) } throws RuntimeException("fooMsg")
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save code class 1: fooMsg")
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun submitting_withDuplicateKeyConstraintViolationException_addsErrorMsg() {
        val msg = ("...Detail: Key (code_class_id, lang_code)=(1, en) already exists.; " +
            "nested exception is org.postgresql.util.PSQLException: " +
            "ERROR: duplicate key value violates unique constraint...")
        every { codeClassServiceMock.saveOrUpdate(any()) } throws DuplicateKeyException(msg)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Code class id 1 is already used.")
    }

    @Test
    fun submitting_withDuplicateKeyConstraintViolationException_withUnexpectedErrorMessage_addsThat() {
        val msg = "odd"
        every { codeClassServiceMock.saveOrUpdate(any()) } throws DuplicateKeyException(msg)
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("odd")
    }

    @Test
    fun clickingBackButton_withPageWithoutCallingPageRef_forwardsToCodeListPage() {
        every { codeServiceMock.countByFilter(any()) } returns 1
        every { codeServiceMock.findPageOfEntityDefinitions(any(), any()) } returns listOf(
            CodeDefinition("c1", "en", CodeClass(1, "cc1", "d1"), 1, false, 1)
        ).iterator()
        tester.startPage(CodeClassEditPage(Model.of(ccd), null))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(CodeListPage::class.java)

        // from CodeClassListPage
        verify { codeClassServiceMock.find("en_us") }
    }

    @Test
    fun clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        tester.startPage(CodeClassEditPage(Model.of(ccd), LogoutPage(PageParameters()).pageReference))
        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(LogoutPage::class.java)
    }
}
