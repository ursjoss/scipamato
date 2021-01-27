package ch.difty.scipamato.core.web.paper.jasper

import org.apache.wicket.util.io.IClusterable

/**
 * Context holder for localized jasper report captions. Uses builder pattern in
 * order to avoid having constructor with numerous String arguments.
 *
 * This class can be used to serve various jasper reports with different needs
 * for localized labels. Consequently hardly any validation occurs in this
 * class. It is up to the DTOs that are passed into the reports to validate the
 * required labels are non-null.
 */
data class ReportHeaderFields @JvmOverloads constructor(
    val headerPart: String,
    val brand: String,
    val goalsLabel: String? = null,
    val methodsLabel: String? = null,
    val methodOutcomeLabel: String? = null,
    val resultMeasuredOutcomeLabel: String? = null,
    val methodStudyDesignLabel: String? = null,
    val populationPlaceLabel: String? = null,
    val populationParticipantsLabel: String? = null,
    val populationDurationLabel: String? = null,
    val exposurePollutantLabel: String? = null,
    val exposureAssessmentLabel: String? = null,
    val resultExposureRangeLabel: String? = null,
    val methodStatisticsLabel: String? = null,
    val methodConfoundersLabel: String? = null,
    val resultEffectEstimateLabel: String? = null,
    val conclusionLabel: String? = null,
    val commentLabel: String? = null,
    val populationLabel: String? = null,
    val resultLabel: String? = null,
    val captionLabel: String? = null,
    val numberLabel: String? = null,
    val authorYearLabel: String? = null,
    val pubmedBaseUrl: String? = null,
    val internLabel: String? = null,
) : IClusterable {

    companion object {
        private const val serialVersionUID = 1L
    }
}
