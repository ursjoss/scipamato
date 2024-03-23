package ch.difty.scipamato.core.web.code

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeTranslation
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.newFormTesterSameSite
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.link.Link
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "VariableNaming")
internal class CodeListPageTest : BasePageTest<CodeListPage>() {

    private val cc1 = CodeClass(1, "cc1", "d1")
    private val cc2 = CodeClass(2, "cc2", "d2")

    private val ct1_de = CodeTranslation(1, "de", "Name1", "a comment", 1)
    private val ct1_en = CodeTranslation(2, "en", "name1", null, 1)
    private val ct1_fr = CodeTranslation(3, "fr", "nom1", null, 1)
    private val cd1 = CodeDefinition("1A", "de", cc1, 1, false, 1, ct1_de, ct1_en, ct1_fr)
    private val ct2_en = CodeTranslation(5, "en", "name2", null, 1)
    private val ct2_fr = CodeTranslation(6, "fr", "nom2", null, 1)
    private val ct2_de = CodeTranslation(4, "de", "Name2", null, 1)
    private val cd2 = CodeDefinition("2A", "de", cc2, 2, true, 1, ct2_de, ct2_en, ct2_fr)

    private val results: List<CodeDefinition> = listOf(cd1, cd2)

    override fun setUpHook() {
        every { codeServiceMock.countByFilter(any()) } returns results.size
        every { codeServiceMock.getCodeClass1("en_us") } returns cc1
        every { codeServiceMock.findPageOfEntityDefinitions(any(), any()) } returns results.iterator()
        every { codeClassServiceMock.find(any()) } returns listOf(cc1, cc2)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeServiceMock)
    }

    override fun makePage(): CodeListPage = CodeListPage(null)

    override val pageClass: Class<CodeListPage>
        get() = CodeListPage::class.java

    override fun assertSpecificComponents() {
        assertFilterForm("filterPanel:filterForm")
        val headers = arrayOf("Code", "Translations", "Sort", "Scope")
        val row1 = arrayOf("1A", "DE: 'Name1'; EN: 'name1'; FR: 'nom1'".replace("'", "&#039;"), "1", "Public")
        val row2 = arrayOf("2A", "DE: 'Name2'; EN: 'name2'; FR: 'nom2'".replace("'", "&#039;"), "2", "Internal")
        assertResultTable("resultPanel:results", headers, row1, row2)
        verify { codeServiceMock.getCodeClass1("en_us") }
        verify { codeServiceMock.countByFilter(any()) }
        verify { codeServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    @Suppress("SameParameterValue")
    private fun assertFilterForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        tester.assertLabel("$b:codeClassLabel", "Code Class")
        tester.assertComponent("$b:codeClass", BootstrapSelect::class.java)
        assertLabeledTextField(b, "name")
        assertLabeledTextField(b, "comment")
        tester.assertComponent("$b:newCode", BootstrapAjaxButton::class.java)
    }

    @Suppress("SameParameterValue")
    private fun assertResultTable(b: String, labels: Array<String>, vararg rows: Array<String>) {
        tester.assertComponent(b, BootstrapDefaultDataTable::class.java)
        assertHeaderColumns(b, labels)
        assertTableValuesOfRows(b, 1, COLUMN_ID_WITH_LINK, *rows)
    }

    private fun assertHeaderColumns(b: String, labels: Array<String>) {
        labels.withIndex().forEach { (idx, label) ->
            val p = "$b:topToolbars:toolbars:2:headers:${idx + 1}:header:orderByLink:header_body:label"
            tester.assertLabel(p, label)
        }
    }

    @Suppress("SameParameterValue")
    private fun assertTableValuesOfRows(b: String, rowStartIdx: Int, colIdxAsLink: Int?, vararg rows: Array<String>) {
        colIdxAsLink?.let { id ->
            tester.assertComponent("$b:body:rows:$rowStartIdx:cells:$id:cell:link", Link::class.java)
        }
        var rowIdx = rowStartIdx
        rows.forEach { row ->
            var colIdx = 1
            row.forEach { value ->
                tester.assertLabel(
                    "$b:body:rows:$rowIdx:cells:$colIdx:cell${if (colIdxAsLink != null && colIdx++ == colIdxAsLink) ":link:label" else ""}",
                    value
                )
            }
            rowIdx++
        }
    }

    @Test
    fun clickingOnCodeTitle_forwardsToCodeEditPage_withModelLoaded() {
        tester.startPage(pageClass)
        tester.clickLinkSameSite("resultPanel:results:body:rows:1:cells:$COLUMN_ID_WITH_LINK:cell:link")
        tester.assertRenderedPage(CodeEditPage::class.java)

        // verify the codes were loaded into the target page
        tester.assertModelValue("form:translationsPanel:translations:1:name", "Name1")
        tester.assertModelValue("form:translationsPanel:translations:2:name", "name1")
        tester.assertModelValue("form:translationsPanel:translations:3:name", "nom1")
        verify { codeServiceMock.getCodeClass1("en_us") }
        verify { codeServiceMock.countByFilter(any()) }
        verify { codeServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    @Suppress("LocalVariableName")
    @Test
    fun clickingNewCode_forwardsToCodeEditPage() {
        val ct_en = CodeTranslation(1, "en", "ct_en", null, 1)
        val kd = CodeDefinition("1A", "en", cc1, 1, false, 1, ct_en)
        every { codeServiceMock.newUnpersistedCodeDefinition() } returns kd
        tester.startPage(pageClass)
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTesterSameSite("filterPanel:filterForm")
        formTester.submit("newCode")
        tester.assertRenderedPage(CodeEditPage::class.java)
        verify { codeServiceMock.getCodeClass1("en_us") }
        verify { codeServiceMock.countByFilter(any()) }
        verify { codeServiceMock.findPageOfEntityDefinitions(any(), any()) }
        verify { codeServiceMock.newUnpersistedCodeDefinition() }
    }

    @Test
    fun changingCodeClass_refreshesResultPanel() {
        tester.startPage(pageClass)
        tester.executeAjaxEvent("filterPanel:filterForm:codeClass", "change")
        tester.assertComponentOnAjaxResponse("resultPanel")
        verify { codeServiceMock.getCodeClass1("en_us") }
        verify(exactly = 2) { codeServiceMock.countByFilter(any()) }
        verify(exactly = 2) { codeServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    companion object {
        private const val COLUMN_ID_WITH_LINK = 2
    }
}
