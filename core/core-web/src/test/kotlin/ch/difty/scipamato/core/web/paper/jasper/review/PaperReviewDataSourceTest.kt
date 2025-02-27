@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.paper.jasper.review

import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
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

internal class PaperReviewDataSourceTest : PaperDataSourceTest() {

    private lateinit var ds: PaperReviewDataSource

    private val rhf = newReportHeaderFields()

    private fun newReportHeaderFields() = ReportHeaderFields(
        headerPart = "",
        brand = "b",
        numberLabel = "nl",
        authorYearLabel = "ayl",
        populationPlaceLabel = "ppl",
        methodOutcomeLabel = "mol",
        exposurePollutantLabel = "epl",
        methodStudyDesignLabel = "msdl",
        populationDurationLabel = "pdl",
        populationParticipantsLabel = "ppal",
        exposureAssessmentLabel = "eal",
        resultExposureRangeLabel = "rerl",
        methodStatisticsLabel = "msl",
        methodConfoundersLabel = "mcl",
        resultEffectEstimateLabel = "reel",
        conclusionLabel = "ccl",
        commentLabel = "col",
        internLabel = "il",
        goalsLabel = "gl",
        populationLabel = "pl",
        methodsLabel = "ml",
        resultLabel = "rl",
    )

    override fun setUpHook() {
        every { paperMock.number } returns NUMBER
        every { paperMock.firstAuthor } returns "fa"
        every { paperMock.publicationYear } returns 2017
        every { paperMock.populationPlace } returns "pp"
        every { paperMock.methodOutcome } returns "mo"
        every { paperMock.exposurePollutant } returns "ep"
        every { paperMock.methodStudyDesign } returns "msd"
        every { paperMock.populationDuration } returns "pd"
        every { paperMock.populationParticipants } returns "pp"
        every { paperMock.exposureAssessment } returns "ea"
        every { paperMock.resultExposureRange } returns "rer"
        every { paperMock.methodStatistics } returns "ms"
        every { paperMock.methodConfounders } returns "mc"
        every { paperMock.resultEffectEstimate } returns "ree"
        every { paperMock.conclusion } returns "cc"
        every { paperMock.comment } returns "co"
        every { paperMock.intern } returns "i"
        every { paperMock.goals } returns "g"
        every { paperMock.methods } returns "m"
        every { paperMock.population } returns "p"
        every { paperMock.result } returns "r"
        every { paperMock.createdByName } returns "cb"
    }

    @Suppress("SameParameterValue")
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
        assertFieldValue("authorYear", "fa 2017", f, jsds)
        assertFieldValue("populationPlace", "pp", f, jsds)
        assertFieldValue("methodOutcome", "mo", f, jsds)
        assertFieldValue("exposurePollutant", "ep", f, jsds)
        assertFieldValue("methodStudyDesign", "msd", f, jsds)
        assertFieldValue("populationDuration", "pd", f, jsds)
        assertFieldValue("populationParticipants", "pp", f, jsds)
        assertFieldValue("exposureAssessment", "ea", f, jsds)
        assertFieldValue("resultExposureRange", "rer", f, jsds)
        assertFieldValue("methodStatistics", "ms", f, jsds)
        assertFieldValue("methodConfounders", "mc", f, jsds)
        assertFieldValue("resultEffectEstimate", "ree", f, jsds)
        assertFieldValue("conclusion", "cc", f, jsds)
        assertFieldValue("comment", "co", f, jsds)
        assertFieldValue("numberLabel", "nl", f, jsds)
        assertFieldValue("authorYearLabel", "ayl", f, jsds)
        assertFieldValue("populationPlaceLabel", "ppl", f, jsds)
        assertFieldValue("methodOutcomeLabel", "mol", f, jsds)
        assertFieldValue("exposurePollutantLabel", "epl", f, jsds)
        assertFieldValue("methodStudyDesignLabel", "msdl", f, jsds)
        assertFieldValue("populationDurationLabel", "pdl", f, jsds)
        assertFieldValue("populationParticipantsLabel", "ppal", f, jsds)
        assertFieldValue("exposureAssessmentLabel", "eal", f, jsds)
        assertFieldValue("resultExposureRangeLabel", "rerl", f, jsds)
        assertFieldValue("methodStatisticsLabel", "msl", f, jsds)
        assertFieldValue("methodConfoundersLabel", "mcl", f, jsds)
        assertFieldValue("resultEffectEstimateLabel", "reel", f, jsds)
        assertFieldValue("conclusionLabel", "ccl", f, jsds)
        assertFieldValue("commentLabel", "col", f, jsds)
        assertFieldValue("internLabel", "il", f, jsds)
        assertFieldValue("goalsLabel", "gl", f, jsds)
        assertFieldValue("methodsLabel", "ml", f, jsds)
        assertFieldValue("populationLabel", "pl", f, jsds)
        assertFieldValue("resultLabel", "rl", f, jsds)
        assertFieldValue("brand", "b", f, jsds)
        assertFieldValue("createdBy", "cb", f, jsds)
        jsds.next().shouldBeFalse()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() {
        every { dataProviderMock.size() } returns 1L
        every { dataProviderMock.findAllPapersByFilter() } returns listOf(paperMock)
        ds = PaperReviewDataSource(dataProviderMock, rhf, pdfExporterConfigMock)
        assertDataSource(FILE_NAME)
        verify { dataProviderMock.size() }
        verify { dataProviderMock.findAllPapersByFilter() }
        verify(exactly = 2) { paperMock.number }
        verify { paperMock.firstAuthor }
        verify { paperMock.publicationYear }
        verify { paperMock.populationPlace }
        verify { paperMock.methodOutcome }
        verify { paperMock.exposurePollutant }
        verify { paperMock.methodStudyDesign }
        verify { paperMock.populationDuration }
        verify { paperMock.populationParticipants }
        verify { paperMock.exposureAssessment }
        verify { paperMock.resultExposureRange }
        verify { paperMock.methodStatistics }
        verify { paperMock.methodConfounders }
        verify { paperMock.resultEffectEstimate }
        verify { paperMock.conclusion }
        verify { paperMock.comment }
        verify { paperMock.intern }
        verify { paperMock.goals }
        verify { paperMock.methods }
        verify { paperMock.population }
        verify { paperMock.result }
        verify { paperMock.createdByName }
    }

    companion object {
        private const val FILE_NAME = "paper_review.pdf"
        private const val NUMBER = 15L
    }
}
