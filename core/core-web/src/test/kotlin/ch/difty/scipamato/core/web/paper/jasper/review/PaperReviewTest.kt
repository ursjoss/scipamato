package ch.difty.scipamato.core.web.paper.jasper.review

import ch.difty.scipamato.core.web.paper.jasper.ExportEntityTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class PaperReviewTest : ExportEntityTest() {

    private val rhf = newReportHeaderFields()

    private lateinit var pr: PaperReview

    private fun newReportHeaderFields() = ReportHeaderFields(
        headerPart = "",
        brand = BRAND,
        numberLabel = NUMBER_LABEL,
        authorYearLabel = AUTHOR_YEAR_LABEL,
        populationPlaceLabel = POPULATION_PLACE_LABEL,
        populationParticipantsLabel = POPULATION_PARTICIPANTS_LABEL,
        methodOutcomeLabel = METHOD_OUTCOME_LABEL,
        exposurePollutantLabel = EXPOSURE_POLLUTANT_LABEL,
        methodStudyDesignLabel = METHOD_STUDY_DESIGN_LABEL,
        populationDurationLabel = POPULATION_DURATION_LABEL,
        exposureAssessmentLabel = EXPOSURE_ASSESSMENT_LABEL,
        resultExposureRangeLabel = RESULT_EXPOSURE_RANGE_LABEL,
        methodStatisticsLabel = METHOD_STATISTICS_LABEL,
        methodConfoundersLabel = METHOD_CONFOUNDERS_LABEL,
        resultEffectEstimateLabel = RESULT_EFFECT_ESTIMATE_LABEL,
        conclusionLabel = CONCLUSION_LABEL,
        commentLabel = COMMENT_LABEL,
        internLabel = INTERN_LABEL,
    )

    @Test
    fun instantiatingWithValidFieldsAndValidLabels() {
        pr = PaperReview(p, rhf)
        assertFieldValues()
        pr.numberLabel shouldBeEqualTo NUMBER_LABEL
        pr.authorYearLabel shouldBeEqualTo AUTHOR_YEAR_LABEL
        pr.populationPlaceLabel shouldBeEqualTo POPULATION_PLACE_LABEL
        pr.methodOutcomeLabel shouldBeEqualTo METHOD_OUTCOME_LABEL
        pr.exposurePollutantLabel shouldBeEqualTo EXPOSURE_POLLUTANT_LABEL
        pr.methodStudyDesignLabel shouldBeEqualTo METHOD_STUDY_DESIGN_LABEL
        pr.populationDurationLabel shouldBeEqualTo POPULATION_DURATION_LABEL
        pr.populationParticipantsLabel shouldBeEqualTo POPULATION_PARTICIPANTS_LABEL
        pr.exposureAssessmentLabel shouldBeEqualTo EXPOSURE_ASSESSMENT_LABEL
        pr.resultExposureRangeLabel shouldBeEqualTo RESULT_EXPOSURE_RANGE_LABEL
        pr.methodStatisticsLabel shouldBeEqualTo METHOD_STATISTICS_LABEL
        pr.methodConfoundersLabel shouldBeEqualTo METHOD_CONFOUNDERS_LABEL
        pr.resultEffectEstimateLabel shouldBeEqualTo RESULT_EFFECT_ESTIMATE_LABEL
        pr.conclusionLabel shouldBeEqualTo CONCLUSION_LABEL
        pr.internLabel shouldBeEqualTo INTERN_LABEL
        pr.brand shouldBeEqualTo BRAND
        pr.createdBy shouldBeEqualTo CREATED_BY
    }

    @Test
    fun instantiatingWithNullFirstAuthorAndPubYear_returnsBlank() {
        p.firstAuthor = null
        p.publicationYear = null
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo ""
    }

    @Test
    fun instantiatingWithNullFirstAuthorAndPubYear0_returnsBlank() {
        p.firstAuthor = null
        p.publicationYear = 0
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo ""
    }

    @Test
    fun instantiatingWithNonNullFirstAuthorButNullPubYear_returnsFirstAuthorOnly() {
        p.firstAuthor.shouldNotBeNull()
        p.publicationYear = null
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo "firstAuthor"
    }

    @Test
    fun instantiatingWithNonNullFirstAuthorButPubYear0_returnsFirstAuthorOnly() {
        p.firstAuthor.shouldNotBeNull()
        p.publicationYear = 0
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo "firstAuthor"
    }

    private fun assertFieldValues() {
        pr.number shouldBeEqualTo NUMBER.toString()
        pr.authorYear shouldBeEqualTo "$FIRST_AUTHOR $PUBLICATION_YEAR"
        pr.populationPlace shouldBeEqualTo POPULATION_PLACE
        pr.methodOutcome shouldBeEqualTo METHOD_OUTCOME
        pr.exposurePollutant shouldBeEqualTo EXPOSURE_POLLUTANT
        pr.methodStudyDesign shouldBeEqualTo METHOD_STUDY_DESIGN
        pr.populationDuration shouldBeEqualTo POPULATION_DURATION
        pr.populationParticipants shouldBeEqualTo POPULATION_PARTICIPANTS
        pr.exposureAssessment shouldBeEqualTo EXPOSURE_ASSESSMENT
        pr.resultExposureRange shouldBeEqualTo RESULT_EXPOSURE_RANGE
        pr.methodStatistics shouldBeEqualTo METHOD_STATISTICS
        pr.methodConfounders shouldBeEqualTo METHOD_CONFOUNDERS
        pr.resultEffectEstimate shouldBeEqualTo RESULT_EFFECT_ESTIMATE
        pr.conclusion shouldBeEqualTo CONCLUSION
    }

    @Test
    fun authorYear_withNullFirstAuthorAndYear() {
        p.firstAuthor = null
        p.publicationYear = null
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo ""
    }

    @Test
    fun authorYear_withOnlyFirstAuthor() {
        p.firstAuthor.shouldNotBeNull()
        p.publicationYear = null
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo FIRST_AUTHOR
    }

    @Test
    fun authorYear_withOnlyPubYear() {
        p.firstAuthor = null
        p.publicationYear.shouldNotBeNull()
        pr = PaperReview(p, rhf)
        pr.authorYear shouldBeEqualTo PUBLICATION_YEAR.toString()
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(PaperReview::class.java)
            .withRedefinedSuperclass()
            .withOnlyTheseFields("number")
            .verify()
    }
}
