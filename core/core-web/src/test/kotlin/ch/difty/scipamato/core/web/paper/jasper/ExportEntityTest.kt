package ch.difty.scipamato.core.web.paper.jasper

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.Paper
import org.junit.jupiter.api.BeforeEach

@Suppress("UnnecessaryAbstractClass")
abstract class ExportEntityTest {

    protected val p = Paper()

    @BeforeEach
    fun setUp() {
        p.id = ID
        p.number = NUMBER
        p.authors = AUTHORS
        p.firstAuthor = FIRST_AUTHOR
        p.publicationYear = PUBLICATION_YEAR
        p.title = TITLE
        p.location = LOCATION
        p.goals = GOALS
        p.doi = DOI
        p.population = POPULATION
        p.methods = METHODS
        p.result = RESULT
        p.comment = COMMENT
        p.createdByName = CREATED_BY
        p.populationPlace = POPULATION_PLACE
        p.populationDuration = POPULATION_DURATION
        p.populationParticipants = POPULATION_PARTICIPANTS
        p.methodOutcome = METHOD_OUTCOME
        p.methodStudyDesign = METHOD_STUDY_DESIGN
        p.methodStatistics = METHOD_STATISTICS
        p.methodConfounders = METHOD_CONFOUNDERS
        p.exposurePollutant = EXPOSURE_POLLUTANT
        p.exposureAssessment = EXPOSURE_ASSESSMENT
        p.resultExposureRange = RESULT_EXPOSURE_RANGE
        p.resultEffectEstimate = RESULT_EFFECT_ESTIMATE
        p.resultMeasuredOutcome = RESULT_MEASURED_OUTCOME
        p.conclusion = CONCLUSION
        p.pmId = PM_ID
        p.originalAbstract = ABSTRACT
        p.addCode(Code("1F", "Code1F", "", false, CodeClassId.CC1.id, "CC1", "CC1D", 1))
        p.addCode(Code("4A", "Code4A", "", false, CodeClassId.CC4.id, "CC4", "CC4D", 1))
        p.addCode(Code("4C", "Code4C", "", false, CodeClassId.CC4.id, "CC4", "CC4D", 3))
        p.addCode(Code("7B", "Code7B", "", false, CodeClassId.CC7.id, "CC7", "CC7D", 2))
    }

    companion object {
        private const val ID = 1L
        const val NUMBER = 100L
        const val ABSTRACT = "abstract"
        const val AUTHORS = "authors"
        const val TITLE = "title"
        const val LOCATION = "location"
        const val DOI = "doi"
        const val GOALS = "goals"
        const val POPULATION = "population"
        const val METHODS = "methods"
        const val RESULT = "results"
        const val COMMENT = "comment"
        const val GOALS_LABEL = "goalsLabel"
        const val POPULATION_LABEL = "populationLabel"
        const val METHODS_LABEL = "methodsLabel"
        const val RESULT_LABEL = "resultLabel"
        const val CONCLUSION_LABEL = "conclusionLabel"
        const val COMMENT_LABEL = "commentLabel"
        const val INTERN_LABEL = "internLabel"
        const val HEADER_PART = "headerPart"
        const val BRAND = "brand"
        const val CREATED_BY = "creatingUser"
        const val POPULATION_PLACE = "populationPlace"
        const val POPULATION_DURATION = "populationDuration"
        const val POPULATION_PARTICIPANTS = "populationParticipants"
        const val METHOD_OUTCOME = "methodOutcome"
        const val METHOD_STUDY_DESIGN = "methodStudyDesign"
        const val METHOD_STATISTICS = "methodStatistics"
        const val METHOD_CONFOUNDERS = "methodConfounders"
        const val EXPOSURE_POLLUTANT = "exposurePollutant"
        const val EXPOSURE_ASSESSMENT = "exposureAssessment"
        const val RESULT_MEASURED_OUTCOME = "resultMeasuredOutcome"
        const val RESULT_EXPOSURE_RANGE = "resultExposureRange"
        const val RESULT_EFFECT_ESTIMATE = "resultEffectEstimate"
        const val METHOD_OUTCOME_LABEL = "methodOutcomeLabel"
        const val RESULT_MEASURED_OUTCOME_LABEL = "resultMeasuredOutcomeLabel"
        const val METHOD_STUDY_DESIGN_LABEL = "methodStudyDesignLabel"
        const val POPULATION_PLACE_LABEL = "populationPlaceLabel"
        const val POPULATION_PARTICIPANTS_LABEL = "populationParticipantsLabel"
        const val POPULATION_DURATION_LABEL = "populationDurationLabel"
        const val EXPOSURE_POLLUTANT_LABEL = "exposurePollutantLabel"
        const val EXPOSURE_ASSESSMENT_LABEL = "exposureAssessmentLabel"
        const val RESULT_EXPOSURE_RANGE_LABEL = "resultExposureRangeLabel"
        const val METHOD_STATISTICS_LABEL = "methodStatisticsLabel"
        const val METHOD_CONFOUNDERS_LABEL = "methodConfoundersLabel"
        const val RESULT_EFFECT_ESTIMATE_LABEL = "resultEffectEstimateLabel"
        const val CONCLUSION = "conclusion"
        const val NUMBER_LABEL = "numberLabel"
        const val CAPTION = "caption"
        const val AUTHOR_YEAR_LABEL = "authorYearLabel"
        const val PM_ID = 1234
        const val FIRST_AUTHOR = "firstAuthor"
        const val PUBLICATION_YEAR = 2017
    }
}
