package ch.difty.scipamato.core.web.paper.jasper.summaryshort

import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PaperSummaryShortTest : JasperEntityTest() {

    private val rhf = newReportHeaderFields()
    private lateinit var ps: PaperSummaryShort

    @Test
    fun instantiating() {
        ps = PaperSummaryShort(p, rhf)
        assertPaperSummaryShort()
    }

    private fun newReportHeaderFields(): ReportHeaderFields = ReportHeaderFields
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

    private fun assertPaperSummaryShort() {
        ps.number shouldBeEqualTo NUMBER.toString()
        ps.authors shouldBeEqualTo AUTHORS
        ps.title shouldBeEqualTo TITLE
        ps.location shouldBeEqualTo LOCATION
        ps.goals shouldBeEqualTo GOALS
        ps.methods shouldBeEqualTo METHODS
        ps.methodOutcome shouldBeEqualTo METHOD_OUTCOME
        ps.resultMeasuredOutcome shouldBeEqualTo RESULT_MEASURED_OUTCOME
        ps.methodStudyDesign shouldBeEqualTo METHOD_STUDY_DESIGN
        ps.populationPlace shouldBeEqualTo POPULATION_PLACE
        ps.populationParticipants shouldBeEqualTo POPULATION_PARTICIPANTS
        ps.populationDuration shouldBeEqualTo POPULATION_DURATION
        ps.exposurePollutant shouldBeEqualTo EXPOSURE_POLLUTANT
        ps.exposureAssessment shouldBeEqualTo EXPOSURE_ASSESSMENT
        ps.resultExposureRange shouldBeEqualTo RESULT_EXPOSURE_RANGE
        ps.methodStatistics shouldBeEqualTo METHOD_STATISTICS
        ps.methodConfounders shouldBeEqualTo METHOD_CONFOUNDERS
        ps.resultEffectEstimate shouldBeEqualTo RESULT_EFFECT_ESTIMATE
        ps.conclusion shouldBeEqualTo CONCLUSION
        ps.comment shouldBeEqualTo COMMENT
        ps.goalsLabel shouldBeEqualTo GOALS_LABEL
        ps.methodsLabel shouldBeEqualTo METHODS_LABEL
        ps.methodOutcomeLabel shouldBeEqualTo METHOD_OUTCOME_LABEL
        ps.resultMeasuredOutcomeLabel shouldBeEqualTo RESULT_MEASURED_OUTCOME_LABEL
        ps.methodStudyDesignLabel shouldBeEqualTo METHOD_STUDY_DESIGN_LABEL
        ps.populationPlaceLabel shouldBeEqualTo POPULATION_PLACE_LABEL
        ps.populationParticipantsLabel shouldBeEqualTo POPULATION_PARTICIPANTS_LABEL
        ps.populationDurationLabel shouldBeEqualTo POPULATION_DURATION_LABEL
        ps.exposurePollutantLabel shouldBeEqualTo EXPOSURE_POLLUTANT_LABEL
        ps.exposureAssessmentLabel shouldBeEqualTo EXPOSURE_ASSESSMENT_LABEL
        ps.resultExposureRangeLabel shouldBeEqualTo RESULT_EXPOSURE_RANGE_LABEL
        ps.methodStatisticsLabel shouldBeEqualTo METHOD_STATISTICS_LABEL
        ps.methodConfoundersLabel shouldBeEqualTo METHOD_CONFOUNDERS_LABEL
        ps.resultEffectEstimateLabel shouldBeEqualTo RESULT_EFFECT_ESTIMATE_LABEL
        ps.conclusionLabel shouldBeEqualTo CONCLUSION_LABEL
        ps.commentLabel shouldBeEqualTo COMMENT_LABEL
        ps.header shouldBeEqualTo "$HEADER_PART $NUMBER"
        ps.brand shouldBeEqualTo BRAND
        ps.createdBy shouldBeEqualTo CREATED_BY
    }

    @Test
    fun goalsLabelIsBlankIfGoalsIsBlank() {
        p.goals = ""
        ps = PaperSummaryShort(p, rhf)
        ps.goals shouldBeEqualTo ""
        ps.goalsLabel shouldBeEqualTo ""
    }

    @Test
    fun methodsLabelIsBlankIfMethodsIsBlank() {
        p.methods = ""
        ps = PaperSummaryShort(p, rhf)
        ps.methods shouldBeEqualTo ""
        ps.methodsLabel shouldBeEqualTo ""
    }

    @Test
    fun methodOutcomeLabelIsBlankIfMethodOutcomeIsBlank() {
        p.methodOutcome = ""
        ps = PaperSummaryShort(p, rhf)
        ps.methodOutcome shouldBeEqualTo ""
        ps.methodOutcomeLabel shouldBeEqualTo ""
    }

    @Test
    fun resultMeasuredOutcomeLabelIsBlankIfResultMeasuredOutcomeIsBlank() {
        p.resultMeasuredOutcome = ""
        ps = PaperSummaryShort(p, rhf)
        ps.resultMeasuredOutcome shouldBeEqualTo ""
        ps.resultMeasuredOutcomeLabel shouldBeEqualTo ""
    }

    @Test
    fun methodStudyDesignLabelIsBlankIfMethodStudyDesignIsBlank() {
        p.methodStudyDesign = ""
        ps = PaperSummaryShort(p, rhf)
        ps.methodStudyDesign shouldBeEqualTo ""
        ps.methodStudyDesignLabel shouldBeEqualTo ""
    }

    @Test
    fun populationPlaceLabelIsBlankIfPopulationPlaceIsBlank() {
        p.populationPlace = ""
        ps = PaperSummaryShort(p, rhf)
        ps.populationPlace shouldBeEqualTo ""
        ps.populationPlaceLabel shouldBeEqualTo ""
    }

    @Test
    fun populationParticipantsLabelIsBlankIfPopulationParticipantsIsBlank() {
        p.populationParticipants = ""
        ps = PaperSummaryShort(p, rhf)
        ps.populationParticipants shouldBeEqualTo ""
        ps.populationParticipantsLabel shouldBeEqualTo ""
    }

    @Test
    fun populationDurationLabelIsBlankIfPopulationDurationIsBlank() {
        p.populationDuration = ""
        ps = PaperSummaryShort(p, rhf)
        ps.populationDuration shouldBeEqualTo ""
        ps.populationDurationLabel shouldBeEqualTo ""
    }

    @Test
    fun exposurePollutantLabelIsBlankIfExposurePollutantIsBlank() {
        p.exposurePollutant = ""
        ps = PaperSummaryShort(p, rhf)
        ps.exposurePollutant shouldBeEqualTo ""
        ps.exposurePollutantLabel shouldBeEqualTo ""
    }

    @Test
    fun exposureAssessmentLabelIsBlankIfExposureAssessmentIsBlank() {
        p.exposureAssessment = ""
        ps = PaperSummaryShort(p, rhf)
        ps.exposureAssessment shouldBeEqualTo ""
        ps.exposureAssessmentLabel shouldBeEqualTo ""
    }

    @Test
    fun resultExposureRangeLabelIsBlankIfResultExposureRangeIsBlank() {
        p.resultExposureRange = ""
        ps = PaperSummaryShort(p, rhf)
        ps.resultExposureRange shouldBeEqualTo ""
        ps.resultExposureRangeLabel shouldBeEqualTo ""
    }

    @Test
    fun methodStatisticsLabelIsBlankIfMethodStatisticsIsBlank() {
        p.methodStatistics = ""
        ps = PaperSummaryShort(p, rhf)
        ps.methodStatistics shouldBeEqualTo ""
        ps.methodStatisticsLabel shouldBeEqualTo ""
    }

    @Test
    fun methodConfoundersLabelIsBlankIfMethodConfoundersIsBlank() {
        p.methodConfounders = ""
        ps = PaperSummaryShort(p, rhf)
        ps.methodConfounders shouldBeEqualTo ""
        ps.methodConfoundersLabel shouldBeEqualTo ""
    }

    @Test
    fun resultEffectEstimateLabelIsBlankIfResultIsBlank() {
        p.resultEffectEstimate = ""
        ps = PaperSummaryShort(p, rhf)
        ps.resultEffectEstimate shouldBeEqualTo ""
        ps.resultEffectEstimateLabel shouldBeEqualTo ""
    }

    @Test
    fun conclusionLabelIsBlankIfConclusionIsBlank() {
        p.conclusion = ""
        ps = PaperSummaryShort(p, rhf)
        ps.conclusion shouldBeEqualTo ""
        ps.conclusionLabel shouldBeEqualTo ""
    }

    @Test
    fun commentLabelIsBlankIfCommentIsBlank() {
        p.comment = ""
        ps = PaperSummaryShort(p, rhf)
        ps.comment shouldBeEqualTo ""
        ps.commentLabel shouldBeEqualTo ""
    }

    @Test
    fun equalsVerify() {
        EqualsVerifier
            .forClass(PaperSummaryShort::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE)
            .verify()
    }
}
