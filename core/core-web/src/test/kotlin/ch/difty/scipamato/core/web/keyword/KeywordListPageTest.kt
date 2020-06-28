package ch.difty.scipamato.core.web.keyword

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.link.Link
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.ArrayList

@Suppress("SameParameterValue")
internal class KeywordListPageTest : BasePageTest<KeywordListPage>() {

    private val results: MutableList<KeywordDefinition> = ArrayList()

    override fun setUpHook() {
        val kt1_de = KeywordTranslation(1, "de", "Name1", 1)
        val kt1_en = KeywordTranslation(2, "en", "name1", 1)
        val kt1_fr = KeywordTranslation(3, "fr", "nom1", 1)
        val kd1 = KeywordDefinition(1, "de", "nameOverride", 1, kt1_de, kt1_en, kt1_fr)
        val kt2_en = KeywordTranslation(5, "en", "name2", 1)
        val kt2_fr = KeywordTranslation(6, "fr", "nom2", 1)
        val kt2_de = KeywordTranslation(4, "de", "Name2", 1)
        val kd2 = KeywordDefinition(2, "de", 1, kt2_de, kt2_en, kt2_fr)
        results.addAll(listOf(kd1, kd2))
        every { keywordServiceMock.countByFilter(any()) } returns results.size
        every { keywordServiceMock.findPageOfEntityDefinitions(any(), any()) } returns results.iterator()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(keywordServiceMock)
    }

    override fun makePage(): KeywordListPage = KeywordListPage(null)

    override val pageClass: Class<KeywordListPage>
        get() = KeywordListPage::class.java

    override fun assertSpecificComponents() {
        assertFilterForm("filterPanel:filterForm")
        val headers = arrayOf("Translations", "Search Override")
        val values = arrayOf("DE: 'Name1'; EN: 'name1'; FR: 'nom1'".replace("'", "&#039;"), "nameOverride")
        assertResultTable("resultPanel:results", headers, values)
        verify { keywordServiceMock.countByFilter(any()) }
        verify { keywordServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    private fun assertFilterForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b, "name")
        tester.assertComponent("$b:newKeyword", BootstrapAjaxButton::class.java)
    }

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

    private fun assertTableValuesOfRow(b: String, rowIdx: Int, colIdxAsLink: Int?, values: Array<String>) {
        if (colIdxAsLink != null)
            tester.assertComponent("$b:body:rows:$rowIdx:cells:$colIdxAsLink:cell:link", Link::class.java)
        var colIdx = 1
        for (value in values)
            tester.assertLabel(
                "$b:body:rows:$rowIdx:cells:$colIdx:cell${if (colIdxAsLink != null && colIdx++ == colIdxAsLink) ":link:label" else ""}",
                value
            )
    }

    @Test
    fun clickingOnKeywordTitle_forwardsToKeywordEditPage_withModelLoaded() {
        tester.startPage(pageClass)
        tester.clickLink("resultPanel:results:body:rows:1:cells:$COLUMN_ID_WITH_LINK:cell:link")
        tester.assertRenderedPage(KeywordEditPage::class.java)

        // verify the keywords were loaded into the target page
        tester.assertModelValue("form:translationsPanel:translations:1:name", "Name1")
        tester.assertModelValue("form:translationsPanel:translations:2:name", "name1")
        tester.assertModelValue("form:translationsPanel:translations:3:name", "nom1")
        verify { keywordServiceMock.countByFilter(any()) }
        verify { keywordServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    @Test
    fun clickingNewKeyword_forwardsToKeywordEditPage() {
        val kt_en = KeywordTranslation(1, "en", "kt_en", 1)
        val kd = KeywordDefinition(1, "en", 1, kt_en)
        every { keywordServiceMock.newUnpersistedKeywordDefinition() } returns kd
        tester.startPage(pageClass)
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("filterPanel:filterForm")
        formTester.submit("newKeyword")
        tester.assertRenderedPage(KeywordEditPage::class.java)
        verify { keywordServiceMock.countByFilter(any()) }
        verify { keywordServiceMock.findPageOfEntityDefinitions(any(), any()) }
        verify { keywordServiceMock.newUnpersistedKeywordDefinition() }
    }

    companion object {
        private const val COLUMN_ID_WITH_LINK = 1
    }
}
