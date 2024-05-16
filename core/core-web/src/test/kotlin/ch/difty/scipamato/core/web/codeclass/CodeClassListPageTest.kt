package ch.difty.scipamato.core.web.codeclass

import ch.difty.scipamato.clickLinkSameSite
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.link.Link
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "VariableNaming")
internal class CodeClassListPageTest : BasePageTest<CodeClassListPage>() {

    private val cct1_de = CodeClassTranslation(1, "de", "Name1", "a description", 1)
    private val cct1_en = CodeClassTranslation(2, "en", "name1", null, 1)
    private val cct1_fr = CodeClassTranslation(3, "fr", "nom1", null, 1)
    private val ccd1 = CodeClassDefinition(1, "de", 1, cct1_de, cct1_en, cct1_fr)
    private val cct2_en = CodeClassTranslation(5, "en", "name2", null, 1)
    private val cct2_fr = CodeClassTranslation(6, "fr", "nom2", null, 1)
    private val cct2_de = CodeClassTranslation(4, "de", "Name2", null, 1)
    private val ccd2 = CodeClassDefinition(2, "de", 1, cct2_de, cct2_en, cct2_fr)

    private val results = listOf(ccd1, ccd2)

    override fun setUpHook() {
        every { codeClassServiceMock.countByFilter(any()) } returns results.size
        every { codeClassServiceMock.findPageOfEntityDefinitions(any(), any()) } returns results.iterator()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(codeClassServiceMock)
    }

    override fun makePage(): CodeClassListPage = CodeClassListPage(null)

    override val pageClass: Class<CodeClassListPage>
        get() = CodeClassListPage::class.java

    override fun assertSpecificComponents() {
        assertFilterForm("filterPanel:filterForm")
        val headers = arrayOf("Id", "Translations")
        val values = arrayOf("1", "DE: 'Name1'; EN: 'name1'; FR: 'nom1'".replace("'", "&#039;"))
        assertResultTable("resultPanel:results", headers, values)
        verify { codeClassServiceMock.countByFilter(any()) }
        verify { codeClassServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    @Suppress("SameParameterValue")
    private fun assertFilterForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b, "name")
        assertLabeledTextField(b, "description")
    }

    @Suppress("SameParameterValue")
    private fun assertResultTable(b: String, labels: Array<String>, values: Array<String>) {
        tester.assertComponent(b, BootstrapDefaultDataTable::class.java)
        assertHeaderColumns(b, labels)
        assertTableValuesOfRow(b, 1, COLUMN_ID_WITH_LINK, values)
    }

    private fun assertHeaderColumns(b: String, labels: Array<String>) {
        var idx = 0
        labels.forEach { label ->
            tester.assertLabel(
                "$b:topToolbars:toolbars:2:headers:${++idx}:header:orderByLink:header_body:label", label
            )
        }
    }

    @Suppress("SameParameterValue")
    private fun assertTableValuesOfRow(b: String, rowIdx: Int, colIdxAsLink: Int?, values: Array<String>) {
        if (colIdxAsLink != null)
            tester.assertComponent("$b:body:rows:$rowIdx:cells:$colIdxAsLink:cell:link", Link::class.java)
        var colIdx = 1
        for (value in values) {
            val p = "$b:body:rows:$rowIdx:cells:$colIdx:cell" +
                if (colIdxAsLink != null && colIdx++ == colIdxAsLink) ":link:label" else ""
            tester.assertLabel(p, value)
        }
    }

    @Test
    fun clickingOnCodeTitle_forwardsToCodeEditPage_withModelLoaded() {
        tester.startPage(pageClass)
        tester.clickLinkSameSite("resultPanel:results:body:rows:1:cells:$COLUMN_ID_WITH_LINK:cell:link")
        tester.assertRenderedPage(CodeClassEditPage::class.java)

        // verify the codes were loaded into the target page
        tester.assertModelValue("form:translationsPanel:translations:1:name", "Name1")
        tester.assertModelValue("form:translationsPanel:translations:2:name", "name1")
        tester.assertModelValue("form:translationsPanel:translations:3:name", "nom1")
        verify { codeClassServiceMock.countByFilter(any()) }
        verify { codeClassServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    companion object {
        private const val COLUMN_ID_WITH_LINK = 2
    }
}
