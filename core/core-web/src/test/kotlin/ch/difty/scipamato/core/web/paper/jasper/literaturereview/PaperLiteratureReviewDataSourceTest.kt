@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.paper.jasper.literaturereview

import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import io.mockk.every
import io.mockk.verify
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.design.JRDesignField
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test

internal class PaperLiteratureReviewDataSourceTest : PaperDataSourceTest() {

    private lateinit var ds: PaperLiteratureReviewDataSource

    private val rhf = newReportHeaderFields()

    private fun newReportHeaderFields(): ReportHeaderFields =
        ReportHeaderFields
            .builder("", BRAND)
            .numberLabel(NUMBER_LABEL)
            .captionLabel(CAPTION)
            .pubmedBaseUrl(PUBMED_BASE_URL)
            .build()

    public override fun setUpHook() {
        every { paperMock.number } returns NUMBER
        every { paperMock.authors } returns "a"
        every { paperMock.publicationYear } returns 2017
        every { paperMock.title } returns "t"
        every { paperMock.goals } returns "g"
        every { paperMock.doi } returns "d"
        every { paperMock.location } returns "l"
        every { paperMock.pmId } returns 1234
    }

    @Suppress("SameParameterValue")
    @Throws(JRException::class)
    private fun assertDataSource(fileName: String) {
        ds.connectionProvider.shouldBeNull()
        ds.contentDisposition.toString() shouldBeEqualTo "ATTACHMENT"
        ds.contentType shouldBeEqualTo "application/pdf"
        ds.extension shouldBeEqualTo "pdf"
        ds.jasperReport shouldBeInstanceOf JasperReport::class
        ds.reportParameters.shouldNotBeEmpty()
        ds.reportParameters["show_goal"] shouldBeEqualTo false
        ds.fileName shouldBeEqualTo fileName
        val jsds = ds.reportDataSource
        val f = JRDesignField()
        jsds.next().shouldBeTrue()
        assertFieldValue("number", NUMBER.toString(), f, jsds)
        assertFieldValue("authors", "a", f, jsds)
        assertFieldValue("publicationYear", "2017", f, jsds)
        assertFieldValue("title", "t", f, jsds)
        assertFieldValue("goals", "g", f, jsds)
        assertFieldValue("location", "l", f, jsds)
        assertFieldValue("doi", "d", f, jsds)
        assertFieldValue("pubmedLink", "https://www.ncbi.nlm.nih.gov/pubmed/1234", f, jsds)
        assertFieldValue("caption", CAPTION, f, jsds)
        assertFieldValue("brand", BRAND, f, jsds)
        assertFieldValue("numberLabel", NUMBER_LABEL, f, jsds)
        jsds.next().shouldBeFalse()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() {
        every { dataProviderMock.size() } returns 1L
        every { dataProviderMock.findAllPapersByFilter() } returns listOf(paperMock)
        ds = PaperLiteratureReviewDataSource(dataProviderMock, rhf, pdfExporterConfigMock)
        assertDataSource(FILE_NAME)
        verify { dataProviderMock.size() }
        verify { dataProviderMock.findAllPapersByFilter() }
        verify { paperMock.number }
        verify { paperMock.authors }
        verify(exactly = 2) { paperMock.publicationYear }
        verify { paperMock.title }
        verify { paperMock.goals }
        verify { paperMock.location }
        verify { paperMock.doi }
        verify { paperMock.pmId }
    }

    companion object {
        private const val FILE_NAME = "paper_literature_review.pdf"
        private const val NUMBER = 5L
        private const val BRAND = "brand"
        private const val CAPTION = "caption"
        private const val NUMBER_LABEL = "numberLabel"
        private const val PUBMED_BASE_URL = "https://www.ncbi.nlm.nih.gov/pubmed/"
    }
}
