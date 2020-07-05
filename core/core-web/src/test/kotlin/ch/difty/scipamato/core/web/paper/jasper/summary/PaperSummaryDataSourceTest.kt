@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.paper.jasper.summary

import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.verify
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.design.JRDesignField
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

internal class PaperSummaryDataSourceTest : PaperDataSourceTest() {

    private lateinit var ds: PaperSummaryDataSource

    private val rhf = newReportHeaderFields()

    private fun newReportHeaderFields(): ReportHeaderFields =
        ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .populationLabel(POPULATION_LABEL)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .resultLabel(RESULT_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build()

    public override fun setUpHook() {
        every { paperMock.number } returns NUMBER
        every { paperMock.authors } returns AUTHORS
        every { paperMock.title } returns TITLE
        every { paperMock.location } returns LOCATION
        every { paperMock.goals } returns GOALS
        every { paperMock.population } returns POPULATION
        every { paperMock.methods } returns METHODS
        every { paperMock.result } returns RESULT
        every { paperMock.comment } returns COMMENT
        every { paperMock.createdByName } returns CREATED_BY
        every { shortFieldConcatenatorMock.methodsFrom(any(), rhf) } returns METHODS
        every { shortFieldConcatenatorMock.populationFrom(any(), rhf) } returns POPULATION
        every { shortFieldConcatenatorMock.resultFrom(any(), rhf) } returns RESULT
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithPaper_returnsPdfDataSourceWithOneRecord() {
        ds = PaperSummaryDataSource(paperMock, rhf, shortFieldConcatenatorMock, pdfExporterConfigMock)
        assertDataSource(FILE_NAME_SINGLE)
        verifyPaperMock(3)
        verifyShortFieldConcatenator()
    }

    private fun verifyPaperMock(i: Int) {
        verify(exactly = i) { paperMock.number }
        verify { paperMock.authors }
        verify { paperMock.title }
        verify { paperMock.location }
        verify { paperMock.goals }
        verify { paperMock.comment }
        verify { paperMock.createdByName }
    }

    private fun verifyShortFieldConcatenator() {
        verify { shortFieldConcatenatorMock.methodsFrom(paperMock, rhf) }
        verify { shortFieldConcatenatorMock.populationFrom(paperMock, rhf) }
        verify { shortFieldConcatenatorMock.resultFrom(paperMock, rhf) }
    }

    @Throws(JRException::class)
    private fun assertDataSource(fileName: String) {
        ds.connectionProvider.shouldBeNull()
        ds.contentDisposition.toString() shouldBeEqualTo "ATTACHMENT"
        ds.contentType shouldBeEqualTo "application/pdf"
        ds.extension shouldBeEqualTo "pdf"
        ds.jasperReport shouldBeInstanceOf JasperReport::class
        ds.reportParameters.shouldBeEmpty()
        ds.fileName shouldBeEqualTo fileName
        val jsds = ds.reportDataSource
        val f = JRDesignField()
        jsds.next().shouldBeTrue()
        assertFieldValue("number", NUMBER.toString(), f, jsds)
        assertFieldValue("authors", AUTHORS, f, jsds)
        assertFieldValue("title", TITLE, f, jsds)
        assertFieldValue("location", LOCATION, f, jsds)
        assertFieldValue("goals", GOALS, f, jsds)
        assertFieldValue("population", POPULATION, f, jsds)
        assertFieldValue("methods", METHODS, f, jsds)
        assertFieldValue("result", RESULT, f, jsds)
        assertFieldValue("comment", COMMENT, f, jsds)
        assertFieldValue("populationLabel", POPULATION_LABEL, f, jsds)
        assertFieldValue("methodsLabel", METHODS_LABEL, f, jsds)
        assertFieldValue("resultLabel", RESULT_LABEL, f, jsds)
        assertFieldValue("commentLabel", COMMENT_LABEL, f, jsds)
        assertFieldValue("header", HEADER, f, jsds)
        assertFieldValue("brand", BRAND, f, jsds)
        assertFieldValue("createdBy", CREATED_BY, f, jsds)
        jsds.next().shouldBeFalse()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithPaperSummary_returnsPdfDataSourceWithOneRecord() {
        val ps = PaperSummary(paperMock, shortFieldConcatenatorMock, rhf)
        ds = PaperSummaryDataSource(ps, pdfExporterConfigMock)
        assertDataSource(FILE_NAME_SINGLE)
        verifyPaperMock(1)
        verifyShortFieldConcatenator()
    }

    @Test
    fun instantiatingWithPaperSummaryWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        clearMocks(paperMock)
        every { paperMock.number } returns null
        val ps = PaperSummary(paperMock, shortFieldConcatenatorMock, rhf)
        ds = PaperSummaryDataSource(ps, pdfExporterConfigMock)
        ds.fileName shouldBeEqualTo FILE_NAME_SINGLE_FALLBACK
        verifyPaperMock(1)
        verifyShortFieldConcatenator()
    }

    @Test
    fun instantiatingWithPaperWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        clearMocks(paperMock)
        every { paperMock.number } returns null
        ds = PaperSummaryDataSource(paperMock, rhf, shortFieldConcatenatorMock, pdfExporterConfigMock)
        ds.fileName shouldBeEqualTo FILE_NAME_SINGLE_FALLBACK
        verifyPaperMock(2)
        verifyShortFieldConcatenator()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() {
        every { dataProviderMock.size() } returns 1L
        every { dataProviderMock.findAllPapersByFilter() } returns listOf(paperMock)
        ds = PaperSummaryDataSource(dataProviderMock, rhf, shortFieldConcatenatorMock, pdfExporterConfigMock)
        assertDataSource(FILE_NAME_MULTIPLE)
        verify { dataProviderMock.size() }
        verify { dataProviderMock.findAllPapersByFilter() }
        verifyPaperMock(1)
        verifyShortFieldConcatenator()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_withEmptyProvider_returnsNoRecord() {
        every { dataProviderMock.size() } returns 0L
        ds = PaperSummaryDataSource(dataProviderMock, rhf, shortFieldConcatenatorMock, pdfExporterConfigMock)
        ds.reportDataSource.next().shouldBeFalse()
        verify { dataProviderMock.size() }
    }

    companion object {
        private const val NUMBER = 100L
        private const val AUTHORS = "authors"
        private const val TITLE = "title"
        private const val LOCATION = "location"
        private const val GOALS = "goals"
        private const val POPULATION = "population"
        private const val METHODS = "methods"
        private const val RESULT = "result"
        private const val COMMENT = "comment"
        private const val CREATED_BY = "creatingUser"
        private const val POPULATION_LABEL = "Kollektiv"
        private const val GOALS_LABEL = "Ziele"
        private const val METHODS_LABEL = "Methoden"
        private const val RESULT_LABEL = "Resultat"
        private const val COMMENT_LABEL = "Bemerkungen"
        private const val HEADER_PART = "LUDOK-Zusammenfassung Nr."
        private const val HEADER = "$HEADER_PART $NUMBER"
        private const val BRAND = "LUDOK"
        private const val FILE_NAME_SINGLE = "paper_summary_no_$NUMBER.pdf"
        private const val FILE_NAME_SINGLE_FALLBACK = "paper_summary.pdf"
        private const val FILE_NAME_MULTIPLE = "paper_summaries.pdf"
    }
}
