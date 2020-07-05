@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.paper.jasper.summaryshort

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

internal class PaperSummaryShortDataSourceTest : PaperDataSourceTest() {

    private lateinit var ds: PaperSummaryShortDataSource

    private val rhf = newReportHeaderFields()

    private fun newReportHeaderFields(): ReportHeaderFields =
        ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .methodOutcomeLabel(METHOD_OUTCOME_LABEL)
            .resultMeasuredOutcomeLabel(RESULT_MEASURED_OUTCOME_LABEL)
            .methodStudyDesignLabel(METHOD_STUDY_DESIGN_LABEL)
            .populationPlaceLabel(POPULATION_PLACE_LABEL)
            .populationParticipantsLabel(POPULATION_PARTICIPANTS_LABEL)
            .populationDurationLabel(POPULATION_DURATION_LABEL)
            .exposurePollutantLabel(EXPOSURE_POLLUTANT_LABEL)
            .exposureAssessmentLabel(EXPOSURE_ASSESSMENT_LABEL)
            .resultExposureRangeLabel(RESULT_EXPOSURE_RANGE_LABEL)
            .methodStatisticsLabel(METHOD_STATISTICS_LABEL)
            .methodConfoundersLabel(METHOD_CONFOUNDERS_LABEL)
            .resultEffectEstimateLabel(RESULT_EFFECT_ESTIMATE_LABEL)
            .conclusionLabel(CONCLUSION_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build()

    public override fun setUpHook() {
        every { paperMock.number } returns NUMBER
        every { paperMock.authors } returns AUTHORS
        every { paperMock.title } returns TITLE
        every { paperMock.location } returns LOCATION
        every { paperMock.goals } returns GOALS
        every { paperMock.methods } returns METHODS
        every { paperMock.methodOutcome } returns METHOD_OUTCOME
        every { paperMock.resultMeasuredOutcome } returns RESULT_MEASURED_OUTCOME
        every { paperMock.methodStudyDesign } returns METHOD_STUDY_DESIGN
        every { paperMock.populationPlace } returns POPULATION_PLACE
        every { paperMock.populationParticipants } returns POPULATION_PARTICIPANTS
        every { paperMock.populationDuration } returns POPULATION_DURATION
        every { paperMock.exposurePollutant } returns EXPOSURE_POLLUTANT
        every { paperMock.exposureAssessment } returns EXPOSURE_ASSESSMENT
        every { paperMock.resultExposureRange } returns RESULT_EXPOSURE_RANGE
        every { paperMock.methodStatistics } returns METHOD_STATISTICS
        every { paperMock.methodConfounders } returns METHOD_CONFOUNDERS
        every { paperMock.resultEffectEstimate } returns RESULT_EFFECT_ESTIMATE
        every { paperMock.conclusion } returns CONCLUSION
        every { paperMock.comment } returns COMMENT
        every { paperMock.createdByName } returns CREATED_BY
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithPaper_returnsPdfDataSourceWithOneRecord() {
        ds = PaperSummaryShortDataSource(paperMock, rhf, pdfExporterConfigMock)
        assertDataSource(FILE_NAME_SINGLE)
        verifyPaperMock(3)
    }

    private fun verifyPaperMock(i: Int) {
        verify(exactly = i) { paperMock.number }
        verify { paperMock.authors }
        verify { paperMock.title }
        verify { paperMock.location }
        verify { paperMock.goals }
        verify { paperMock.methods }
        verify { paperMock.methodOutcome }
        verify { paperMock.resultMeasuredOutcome }
        verify { paperMock.methodStudyDesign }
        verify { paperMock.populationPlace }
        verify { paperMock.populationParticipants }
        verify { paperMock.populationDuration }
        verify { paperMock.exposurePollutant }
        verify { paperMock.exposureAssessment }
        verify { paperMock.resultExposureRange }
        verify { paperMock.methodStatistics }
        verify { paperMock.methodConfounders }
        verify { paperMock.resultEffectEstimate }
        verify { paperMock.conclusion }
        verify { paperMock.comment }
        verify { paperMock.createdByName }
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
        assertFieldValue("methods", METHODS, f, jsds)
        assertFieldValue("methodOutcome", METHOD_OUTCOME, f, jsds)
        assertFieldValue("methodStudyDesign", METHOD_STUDY_DESIGN, f, jsds)
        assertFieldValue("populationPlace", POPULATION_PLACE, f, jsds)
        assertFieldValue("populationParticipants", POPULATION_PARTICIPANTS, f, jsds)
        assertFieldValue("populationDuration", POPULATION_DURATION, f, jsds)
        assertFieldValue("exposurePollutant", EXPOSURE_POLLUTANT, f, jsds)
        assertFieldValue("exposureAssessment", EXPOSURE_ASSESSMENT, f, jsds)
        assertFieldValue("resultExposureRange", RESULT_EXPOSURE_RANGE, f, jsds)
        assertFieldValue("methodStatistics", METHOD_STATISTICS, f, jsds)
        assertFieldValue("methodConfounders", METHOD_CONFOUNDERS, f, jsds)
        assertFieldValue("resultEffectEstimate", RESULT_EFFECT_ESTIMATE, f, jsds)
        assertFieldValue("conclusion", CONCLUSION, f, jsds)
        assertFieldValue("comment", COMMENT, f, jsds)
        assertFieldValue("goalsLabel", GOALS_LABEL, f, jsds)
        assertFieldValue("methodsLabel", METHODS_LABEL, f, jsds)
        assertFieldValue("methodOutcomeLabel", METHOD_OUTCOME_LABEL, f, jsds)
        assertFieldValue("methodStudyDesignLabel", METHOD_STUDY_DESIGN_LABEL, f, jsds)
        assertFieldValue("populationPlaceLabel", POPULATION_PLACE_LABEL, f, jsds)
        assertFieldValue("populationParticipantsLabel", POPULATION_PARTICIPANTS_LABEL, f, jsds)
        assertFieldValue("populationDurationLabel", POPULATION_DURATION_LABEL, f, jsds)
        assertFieldValue("exposurePollutantLabel", EXPOSURE_POLLUTANT_LABEL, f, jsds)
        assertFieldValue("exposureAssessmentLabel", EXPOSURE_ASSESSMENT_LABEL, f, jsds)
        assertFieldValue("resultExposureRangeLabel", RESULT_EXPOSURE_RANGE_LABEL, f, jsds)
        assertFieldValue("methodStatisticsLabel", METHOD_STATISTICS_LABEL, f, jsds)
        assertFieldValue("methodConfoundersLabel", METHOD_CONFOUNDERS_LABEL, f, jsds)
        assertFieldValue("resultEffectEstimateLabel", RESULT_EFFECT_ESTIMATE_LABEL, f, jsds)
        assertFieldValue("conclusionLabel", CONCLUSION_LABEL, f, jsds)
        assertFieldValue("commentLabel", COMMENT_LABEL, f, jsds)
        assertFieldValue("header", HEADER, f, jsds)
        assertFieldValue("brand", BRAND, f, jsds)
        assertFieldValue("createdBy", CREATED_BY, f, jsds)
        jsds.next().shouldBeFalse()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithPaperSummaryShort_returnsPdfDataSourceWithOneRecord() {
        val ps = PaperSummaryShort(paperMock, rhf)
        ds = PaperSummaryShortDataSource(ps, pdfExporterConfigMock)
        assertDataSource(FILE_NAME_SINGLE)
        verifyPaperMock(1)
    }

    @Test
    fun instantiatingWithPaperSummaryWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        clearMocks(paperMock)
        every { paperMock.number } returns null
        val ps = PaperSummaryShort(paperMock, rhf)
        ds = PaperSummaryShortDataSource(ps, pdfExporterConfigMock)
        ds.fileName shouldBeEqualTo FILE_NAME_SINGLE_FALLBACK
        verifyPaperMock(1)
    }

    @Test
    fun instantiatingWithPaperWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        clearMocks(paperMock)
        every { paperMock.number } returns null
        ds = PaperSummaryShortDataSource(paperMock, rhf, pdfExporterConfigMock)
        ds.fileName shouldBeEqualTo FILE_NAME_SINGLE_FALLBACK
        verifyPaperMock(2)
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() {
        every { dataProviderMock.size() } returns 1L
        every { dataProviderMock.findAllPapersByFilter() } returns listOf(paperMock)
        ds = PaperSummaryShortDataSource(dataProviderMock, rhf, pdfExporterConfigMock)
        assertDataSource(FILE_NAME_MULTIPLE)
        verify { dataProviderMock.size() }
        verify { dataProviderMock.findAllPapersByFilter() }
        verifyPaperMock(1)
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_withEmptyProvider_returnsNoRecord() {
        every { dataProviderMock.size() } returns 0L
        ds = PaperSummaryShortDataSource(dataProviderMock, rhf, pdfExporterConfigMock)
        ds.reportDataSource.next().shouldBeFalse()
        verify { dataProviderMock.size() }
    }

    companion object {
        private const val NUMBER = 100L
        private const val AUTHORS = "authors"
        private const val TITLE = "title"
        private const val LOCATION = "location"
        private const val GOALS = "goals"
        private const val METHODS = "methods"
        private const val METHOD_OUTCOME = "methodOutcome"
        private const val RESULT_MEASURED_OUTCOME = "resultMeasuredOutcome"
        private const val METHOD_STUDY_DESIGN = "methodStudyDesign"
        private const val POPULATION_PLACE = "populationPlace"
        private const val POPULATION_PARTICIPANTS = "populationParticipants"
        private const val POPULATION_DURATION = "populationDuration"
        private const val EXPOSURE_POLLUTANT = "exposurePollutant"
        private const val EXPOSURE_ASSESSMENT = "exposureAssessment"
        private const val RESULT_EXPOSURE_RANGE = "resultExposureRange"
        private const val METHOD_STATISTICS = "methodStatistics"
        private const val METHOD_CONFOUNDERS = "methodConfounders"
        private const val RESULT_EFFECT_ESTIMATE = "resultEffectEstimate"
        private const val CONCLUSION = "conclusion"
        private const val COMMENT = "comment"
        private const val CREATED_BY = "creatingUser"
        private const val GOALS_LABEL = "Ziele"
        private const val METHODS_LABEL = "Methoden"
        private const val METHOD_OUTCOME_LABEL = "Gesundheitliche Zielgrössen"
        private const val RESULT_MEASURED_OUTCOME_LABEL = "Gemessene Zielgrösse"
        private const val METHOD_STUDY_DESIGN_LABEL = "Studiendesign"
        private const val POPULATION_PLACE_LABEL = "Ort/Land (Studie)"
        private const val POPULATION_PARTICIPANTS_LABEL = "Studienteilnehmer"
        private const val POPULATION_DURATION_LABEL = "Studiendauer"
        private const val EXPOSURE_POLLUTANT_LABEL = "Schadstoff"
        private const val EXPOSURE_ASSESSMENT_LABEL = "Belastungsabschätzung"
        private const val RESULT_EXPOSURE_RANGE_LABEL = "Gemessene Belastung (Spanne)"
        private const val METHOD_STATISTICS_LABEL = "Statistische Methode"
        private const val METHOD_CONFOUNDERS_LABEL = "Störfaktoren Methode"
        private const val RESULT_EFFECT_ESTIMATE_LABEL = "Ergebnisse"
        private const val CONCLUSION_LABEL = "Schlussfolgerung"
        private const val COMMENT_LABEL = "Bemerkungen"
        private const val HEADER_PART = "LUDOK-Zusammenfassung Nr."
        private const val HEADER = "$HEADER_PART $NUMBER"
        private const val BRAND = "LUDOK"
        private const val FILE_NAME_SINGLE = "paper_summary_short_no_$NUMBER.pdf"
        private const val FILE_NAME_SINGLE_FALLBACK = "paper_summary_short.pdf"
        private const val FILE_NAME_MULTIPLE = "paper_summaries_short.pdf"
    }
}
