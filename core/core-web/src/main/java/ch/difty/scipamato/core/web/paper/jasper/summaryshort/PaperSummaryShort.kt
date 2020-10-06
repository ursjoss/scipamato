package ch.difty.scipamato.core.web.paper.jasper.summaryshort

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.PaperSummaryCommon
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.na

/**
 * DTO to feed the PaperSummaryShortDataSource
 */
class PaperSummaryShort(p: Paper, rhf: ReportHeaderFields) : PaperSummaryCommon(p, null, rhf) {

    val methodOutcome: String = na(p.methodOutcome)
    val methodOutcomeLabel: String = na(rhf.methodOutcomeLabel, methodOutcome)
    val resultMeasuredOutcome: String = na(p.resultMeasuredOutcome)
    val resultMeasuredOutcomeLabel: String = na(rhf.resultMeasuredOutcomeLabel, resultMeasuredOutcome)
    val methodStudyDesign: String = na(p.methodStudyDesign)
    val methodStudyDesignLabel: String = na(rhf.methodStudyDesignLabel, methodStudyDesign)
    val populationPlace: String = na(p.populationPlace)
    val populationPlaceLabel: String = na(rhf.populationPlaceLabel, populationPlace)
    val populationParticipants: String = na(p.populationParticipants)
    val populationParticipantsLabel: String = na(rhf.populationParticipantsLabel, populationParticipants)
    val populationDuration: String = na(p.populationDuration)
    val populationDurationLabel: String = na(rhf.populationDurationLabel, populationDuration)
    val exposurePollutant: String = na(p.exposurePollutant)
    val exposurePollutantLabel: String = na(rhf.exposurePollutantLabel, exposurePollutant)
    val exposureAssessment: String = na(p.exposureAssessment)
    val exposureAssessmentLabel: String = na(rhf.exposureAssessmentLabel, exposureAssessment)
    val resultExposureRange: String = na(p.resultExposureRange)
    val resultExposureRangeLabel: String = na(rhf.resultExposureRangeLabel, resultExposureRange)
    val methodStatistics: String = na(p.methodStatistics)
    val methodStatisticsLabel: String = na(rhf.methodStatisticsLabel, methodStatistics)
    val methodConfounders: String = na(p.methodConfounders)
    val methodConfoundersLabel: String = na(rhf.methodConfoundersLabel, methodConfounders)
    val resultEffectEstimate: String = na(p.resultEffectEstimate)
    val resultEffectEstimateLabel: String = na(rhf.resultEffectEstimateLabel, resultEffectEstimate)
    val conclusion: String = na(p.conclusion)
    val conclusionLabel: String = na(rhf.conclusionLabel, conclusion)

    companion object {
        private const val serialVersionUID = 1L
    }
}
