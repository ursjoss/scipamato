@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.newsletter.topic

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation
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

internal class NewsletterTopicListPageTest : BasePageTest<NewsletterTopicListPage>() {

    private val results: MutableList<NewsletterTopicDefinition> = ArrayList()

    @Suppress("LocalVariableName")
    override fun setUpHook() {
        val ntt1_de = NewsletterTopicTranslation(1, "de", "thema1", 1)
        val ntt1_en = NewsletterTopicTranslation(2, "en", "topic1", 1)
        val ntt1_fr = NewsletterTopicTranslation(3, "fr", "theme1", 1)
        val ntd1 = NewsletterTopicDefinition(1, "de", 1, ntt1_de, ntt1_en, ntt1_fr)
        val ntt2_de = NewsletterTopicTranslation(4, "de", "thema2", 1)
        val ntt2_en = NewsletterTopicTranslation(5, "en", "topic2", 1)
        val ntt2_fr = NewsletterTopicTranslation(6, "fr", "theme2", 1)
        val ntd2 = NewsletterTopicDefinition(2, "de", 1, ntt2_de, ntt2_en, ntt2_fr)
        results.addAll(listOf(ntd1, ntd2))
        every { newsletterTopicServiceMock.countByFilter(any()) } returns results.size
        every { newsletterTopicServiceMock.findPageOfEntityDefinitions(any(), any()) } returns results.iterator()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(newsletterTopicServiceMock)
    }

    override fun makePage(): NewsletterTopicListPage = NewsletterTopicListPage(null)

    override val pageClass: Class<NewsletterTopicListPage>
        get() = NewsletterTopicListPage::class.java

    override fun assertSpecificComponents() {
        assertFilterForm("filterPanel:filterForm")
        val headers = arrayOf("Translations")
        val values = arrayOf("DE: 'thema1'; EN: 'topic1'; FR: 'theme1'".replace("'", "&#039;"))
        assertResultTable("resultPanel:results", headers, values)
        verify { newsletterTopicServiceMock.countByFilter(any()) }
        verify { newsletterTopicServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    @Suppress("SameParameterValue")
    private fun assertFilterForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b, "title")
        tester.assertComponent("$b:newNewsletterTopic", BootstrapAjaxButton::class.java)
    }

    @Suppress("SameParameterValue")
    private fun assertResultTable(b: String, labels: Array<String>, values: Array<String>) {
        tester.assertComponent(b, BootstrapDefaultDataTable::class.java)
        assertHeaderColumns(b, labels)
        assertTableValuesOfRow(b, 1, 1, values)
    }

    private fun assertHeaderColumns(b: String, labels: Array<String>) =
        labels.withIndex().forEach { (idx, label) ->
            val p = "$b:topToolbars:toolbars:2:headers:${idx + 1}:header:orderByLink:header_body:label"
            tester.assertLabel(p, label)
        }

    @Suppress("SameParameterValue")
    private fun assertTableValuesOfRow(b: String, rowIdx: Int, colIdxAsLink: Int?, values: Array<String>) {
        colIdxAsLink?.let { idx ->
            tester.assertComponent("$b:body:rows:$rowIdx:cells:$idx:cell:link", Link::class.java)
        }
        var colIdx = 1
        values.forEach { value ->
            val p = "$b:body:rows:$rowIdx:cells:$colIdx:cell${if (colIdxAsLink != null && colIdx++ == colIdxAsLink) ":link:label" else ""}"
            tester.assertLabel(p, value)
        }
    }

    @Test
    fun clickingOnNewsletterTopicTitle_forwardsToNewsletterTopicEditPage_withModelLoaded() {
        tester.startPage(pageClass)
        tester.clickLink("resultPanel:results:body:rows:1:cells:1:cell:link")
        tester.assertRenderedPage(NewsletterTopicEditPage::class.java)

        // verify the newsletter was loaded into the target page
        tester.assertModelValue("form:translationsPanel:translations:1:title", "thema1")
        tester.assertModelValue("form:translationsPanel:translations:2:title", "topic1")
        tester.assertModelValue("form:translationsPanel:translations:3:title", "theme1")

        verify { newsletterTopicServiceMock.countByFilter(any()) }
        verify { newsletterTopicServiceMock.findPageOfEntityDefinitions(any(), any()) }
    }

    @Suppress("LocalVariableName")
    @Test
    fun clickingNewNewsletterTopic_forwardsToNewsletterTopicEditPage() {
        val ntt_en = NewsletterTopicTranslation(1, "en", "ntt_en", 1)
        val ntd = NewsletterTopicDefinition(1, "en", 1, ntt_en)
        every { newsletterTopicServiceMock.newUnpersistedNewsletterTopicDefinition() } returns ntd

        tester.startPage(pageClass)
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("filterPanel:filterForm")
        formTester.submit("newNewsletterTopic")
        tester.assertRenderedPage(NewsletterTopicEditPage::class.java)

        verify { newsletterTopicServiceMock.countByFilter(any()) }
        verify { newsletterTopicServiceMock.findPageOfEntityDefinitions(any(), any()) }
        verify { newsletterTopicServiceMock.newUnpersistedNewsletterTopicDefinition() }
    }
}