package ch.difty.scipamato.core.web.keyword

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.authentication.LogoutPage
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.repeater.RefreshingView
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException

@Suppress("SpellCheckingInspection")
internal class KeywordEditPageTest : BasePageTest<KeywordEditPage>() {

    @MockK
    private lateinit var keywordDefinitionDummy: KeywordDefinition

    private lateinit var kd: KeywordDefinition

    @Suppress("LocalVariableName")
    public override fun setUpHook() {
        val kt_de = KeywordTranslation(1, "de", "Name1", 1)
        val kt_de2 = KeywordTranslation(10, "de", "Name1a", 1)
        val kt_en = KeywordTranslation(2, "en", "name1", 1)
        val kt_fr = KeywordTranslation(3, "fr", "nom1", 1)
        kd = KeywordDefinition(1, "de", "thename", 1, kt_de, kt_en, kt_fr, kt_de2)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(keywordServiceMock)
    }

    override fun makePage(): KeywordEditPage = KeywordEditPage(Model.of(kd), null)

    override val pageClass: Class<KeywordEditPage>
        get() = KeywordEditPage::class.java

    public override fun assertSpecificComponents() {
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        var bb = b + "header"
        tester.assertLabel(bb + "Label", "Keyword")
        bb += "Panel:"
        tester.assertComponent(bb + "back", BootstrapButton::class.java)
        tester.assertComponent(bb + "submit", BootstrapButton::class.java)
        tester.assertComponent(bb + "delete", BootstrapButton::class.java)
        bb += "searchOverride"
        tester.assertLabel(bb + "Label", "Search Override")
        tester.assertComponent(bb, TextField::class.java)
        tester.assertModelValue(bb, "thename")
        bb = "form:translations"
        tester.assertLabel(bb + "Label", "Keyword Translations")
        bb += "Panel:translations"
        tester.assertComponent(bb, RefreshingView::class.java)
        bb += ":"
        assertTranslation(bb, 1, "de", "Name1")
        assertTranslation(bb, 2, "de", "Name1a")
        assertTranslation(bb, 3, "en", "name1")
        assertTranslation(bb, 4, "fr", "nom1")
    }

    private fun assertTranslation(bb: String, idx: Int, langCode: String, name: String) = with(tester) {
        assertLabel("$bb$idx:langCode", langCode)
        assertComponent("$bb$idx:name", TextField::class.java)
        assertModelValue("$bb$idx:name", name)
        assertComponent("$bb$idx:addTranslation", AjaxLink::class.java)
        assertComponent("$bb$idx:removeTranslation", AjaxLink::class.java)
    }

    @Test
    fun submitting_withSuccessfulServiceCall_addsInfoMsg() {
        every { keywordServiceMock.saveOrUpdate(any()) } returns kd
        runSubmitTest()
        tester.assertInfoMessages("Successfully saved keyword [id 1]: DE: 'foo','Name1a'; EN: 'name1'; FR: 'nom1'.")
        tester.assertNoErrorMessage()
    }

    private fun runSubmitTest() {
        tester.startPage(KeywordEditPage(Model.of(kd), null))

        val formTester = tester.newFormTester("form")
        formTester.setValue("translationsPanel:translations:1:name", "foo")
        assertTranslation("form:translationsPanel:translations:", 1, "de", "Name1")
        formTester.submit("headerPanel:submit")
        assertTranslation("form:translationsPanel:translations:", 5, "de", "foo")

        verify { keywordServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        every { keywordServiceMock.saveOrUpdate(any()) } returns null

        runSubmitTest()

        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Could not save keyword [id 1].")
    }

    @Test
    fun submitting_withOptimisticLockingException_addsErrorMsg() {
        every { keywordServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE)

        runSubmitTest()

        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The tblName with id 1 has been modified concurrently " +
                "by another user. Please reload it and apply your changes once more."
        )
    }

    @Test
    fun submitting_withDuplicateKeyException_addsErrorMsg() {
        every { keywordServiceMock.saveOrUpdate(any()) } throws DuplicateKeyException("boom")
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("boom")
    }

    @Test
    fun submitting_withOtherException_addsErrorMsg() {
        every { keywordServiceMock.saveOrUpdate(any()) } throws RuntimeException("fooMsg")
        runSubmitTest()
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save the keyword [id 1]: fooMsg")
    }

    @Test
    fun submittingDelete_delegatesDeleteToService() {
        every { keywordServiceMock.delete(any(), any()) } returns keywordDefinitionDummy
        every { keywordServiceMock.countByFilter(any()) } returns 0

        tester.startPage(KeywordEditPage(Model.of(kd), null))

        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")

        verify { keywordServiceMock.delete(1, 1) }
        verify(exactly = 0) { keywordServiceMock.saveOrUpdate(any()) }
        verify { keywordServiceMock.countByFilter(any()) }

        tester.assertNoInfoMessage()
        tester.assertNoErrorMessage()
    }

    @Test
    fun submittingDelete_withForeignKeyConstraintViolationException_addsErrorMsg() {
        val msg =
            """... is still referenced from table "paper_code".; nested exception is org.postgresql.util.PSQLException..."""
        every { keywordServiceMock.delete(any(), any()) } throws DataIntegrityViolationException(msg)
        tester.startPage(KeywordEditPage(Model.of(kd), null))

        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")

        verify { keywordServiceMock.delete(1, 1) }

        tester.assertNoInfoMessage()
        tester.assertErrorMessages("Unable to delete this record as it is still used in other places.")
    }

    @Test
    fun submittingDelete_withOptimisticLockingException_addsErrorMsg() {
        every { keywordServiceMock.delete(any(), any()) } throws
            OptimisticLockingException("keyword", OptimisticLockingException.Type.DELETE)
        tester.startPage(KeywordEditPage(Model.of(kd), null))

        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")

        verify { keywordServiceMock.delete(1, 1) }

        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The keyword with id 1 has been modified concurrently by another user. " +
                "Please reload it and apply your changes once more."
        )
    }

    @Test
    fun submittingDelete_withException_addsErrorMsg() {
        every { keywordServiceMock.delete(any(), any()) } throws RuntimeException("boom")

        tester.startPage(KeywordEditPage(Model.of(kd), null))

        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:delete")

        verify { keywordServiceMock.delete(1, 1) }

        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to delete keyword [id 1]: boom")
    }

    @Test
    fun clickingBackButton_withPageWithoutCallingPageRef_forwardsToKeywordListPage() {
        every { keywordServiceMock.countByFilter(any()) } returns 0

        tester.startPage(KeywordEditPage(Model.of(kd), null))

        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(KeywordListPage::class.java)

        // from CodeListPage
        verify { keywordServiceMock.countByFilter(any()) }
    }

    @Test
    fun clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        tester.startPage(KeywordEditPage(Model.of(kd), LogoutPage(PageParameters()).pageReference))

        val formTester = tester.newFormTester("form")
        formTester.submit("headerPanel:back")
        tester.assertRenderedPage(LogoutPage::class.java)
    }

    @Test
    fun clickingAddNewKeyword_addsTranslationInRequestedLanguage_andRefreshesForm() {
        tester.startPage(KeywordEditPage(Model.of(kd), LogoutPage(PageParameters()).pageReference))

        val next = assertTranslationsInLanguages(1, "de", "de", "en", "fr")
        tester.clickLink("form:translationsPanel:translations:3:addTranslation")
        assertTranslationsInLanguages(next, "de", "de", "en", "en", "fr")
        tester.assertComponentOnAjaxResponse("form")
    }

    private fun assertTranslationsInLanguages(startIndex: Int, vararg languages: String): Int {
        var i = startIndex
        languages.forEach { lang ->
            tester.assertModelValue("form:translationsPanel:translations:" + i++ + ":langCode", lang)
        }
        return i
    }

    @Test
    fun clickingRemoveKeyword_removesTranslation_andRefreshesForm() {
        tester.startPage(KeywordEditPage(Model.of(kd), LogoutPage(PageParameters()).pageReference))

        val next = assertTranslationsInLanguages(1, "de", "de", "en", "fr")
        tester.clickLink("form:translationsPanel:translations:2:removeTranslation")
        assertTranslationsInLanguages(next, "de", "en", "fr")
        tester.assertComponentOnAjaxResponse("form")
    }
}
