package ch.difty.scipamato.core.web.paper.jasper.review

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.ExportEntity
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.na
import ch.difty.scipamato.core.web.paper.jasper.na2

/**
 * DTO to feed the PaperReviewDataSource
 */
class PaperReview(p: Paper, rhf: ReportHeaderFields) : ExportEntity {

    val number: String = p.number?.toString() ?: ""
    val authorYear: String = makeAuthorYearFrom(p)
    val populationPlace: String = na(p.populationPlace)
    val methodOutcome: String = na(p.methodOutcome)
    val exposurePollutant: String = na(p.exposurePollutant)
    val methodStudyDesign: String = na(p.methodStudyDesign)
    val populationDuration: String = na(p.populationDuration)
    val populationParticipants: String = na(p.populationParticipants)
    val exposureAssessment: String = na(p.exposureAssessment)
    val resultExposureRange: String = na(p.resultExposureRange)
    val methodStatistics: String = na(p.methodStatistics)
    val methodConfounders: String = na(p.methodConfounders)
    val resultEffectEstimate: String = na(p.resultEffectEstimate)
    val conclusion: String = na(p.conclusion)
    val comment: String = na(p.comment)
    val intern: String = na(p.intern)
    val goals: String = na(p.goals)
    val population: String = na(p.population)
    val methods: String = na(p.methods)
    val result: String = na(p.result)
    val numberLabel: String = na2(rhf.numberLabel!!, p.number?.toString())
    val authorYearLabel: String = na(rhf.authorYearLabel)
    val populationPlaceLabel: String = na(rhf.populationPlaceLabel)
    val methodOutcomeLabel: String = na(rhf.methodOutcomeLabel)
    val exposurePollutantLabel: String = na(rhf.exposurePollutantLabel)
    val methodStudyDesignLabel: String = na(rhf.methodStudyDesignLabel)
    val populationDurationLabel: String = na(rhf.populationDurationLabel)
    val populationParticipantsLabel: String = na(rhf.populationParticipantsLabel)
    val exposureAssessmentLabel: String = na(rhf.exposureAssessmentLabel)
    val resultExposureRangeLabel: String = na(rhf.resultExposureRangeLabel)
    val methodStatisticsLabel: String = na(rhf.methodStatisticsLabel)
    val methodConfoundersLabel: String = na(rhf.methodConfoundersLabel)
    val resultEffectEstimateLabel: String = na(rhf.resultEffectEstimateLabel)
    val conclusionLabel: String = na(rhf.conclusionLabel)
    val commentLabel: String = na(rhf.commentLabel)
    val internLabel: String = na(rhf.internLabel)
    val goalsLabel: String = na(rhf.goalsLabel)
    val populationLabel: String = na(rhf.populationLabel)
    val methodsLabel: String = na(rhf.methodsLabel)
    val resultLabel: String = na(rhf.resultLabel)
    val brand: String = na(rhf.brand)
    val createdBy: String = na(p.createdByName)

    @Suppress("NestedBlockDepth")
    private fun makeAuthorYearFrom(p: Paper): String {
        val fa = p.firstAuthor
        val py = p.publicationYear
        return if (fa == null && isPubYearMissingOrPseudoMissing(py)) ""
        else {
            StringBuilder().apply {
                if (fa != null) {
                    append(fa)
                    if (!isPubYearMissingOrPseudoMissing(py)) append(" ")
                }
                if (!isPubYearMissingOrPseudoMissing(py)) append(py)

            }.run { toString() }
        }
    }

    private fun isPubYearMissingOrPseudoMissing(pubYear: Int?): Boolean = pubYear == null || pubYear == 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PaperReview
        return number == other.number
    }

    override fun hashCode(): Int = number.hashCode()

    companion object {
        private const val serialVersionUID = 1L
    }
}
