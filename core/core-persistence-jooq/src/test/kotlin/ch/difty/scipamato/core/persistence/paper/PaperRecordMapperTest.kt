@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.RecordMapperTest
import io.mockk.every
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.RecordMapper

class PaperRecordMapperTest : RecordMapperTest<PaperRecord, Paper>() {

    override val mapper: RecordMapper<PaperRecord, Paper> = PaperRecordMapper()

    override fun makeRecord(): PaperRecord {
        val record = PaperRecord()
        record.id = ID
        record.number = NUMBER
        record.pmId = PM_ID
        record.doi = DOI
        record.authors = AUTHORS
        record.firstAuthor = FIRST_AUTHOR
        record.firstAuthorOverridden = FIRST_AUTHOR_OVERRIDDEN
        record.title = TITLE
        record.location = LOCATION
        record.publicationYear = PUBLICATION_YEAR

        record.goals = GOALS
        record.population = POPULATION
        record.methods = METHODS

        record.populationPlace = POPULATION_PLACE
        record.populationParticipants = POPULATION_PARTICIPANTS
        record.populationDuration = POPULATION_DURATION
        record.exposurePollutant = EXPOSURE_POLLUTANT
        record.exposureAssessment = EXPOSURE_ASSESSMENT
        record.methodStudyDesign = METHOD_STUDY_DESIGN
        record.methodOutcome = METHOD_OUTCOME
        record.methodStatistics = METHOD_STATISTICS
        record.methodConfounders = METHOD_CONFOUNDERS

        record.result = RESULT
        record.comment = COMMENT
        record.intern = INTERN

        record.resultExposureRange = RESULT_EXPOSURE_RANGE
        record.resultEffectEstimate = RESULT_EFFECT_ESTIMATE
        record.resultMeasuredOutcome = RESULT_MEASURED_OUTCOME
        record.conclusion = CONCLUSION

        record.originalAbstract = ORIGINAL_ABSTRACT

        record.mainCodeOfCodeclass1 = MAIN_CODE_OF_CODECLASS1

        return record
    }

    override fun setAuditFieldsIn(record: PaperRecord) {
        record.created = CREATED
        record.createdBy = CREATED_BY
        record.lastModified = LAST_MOD
        record.lastModifiedBy = LAST_MOD_BY
        record.version = VERSION
    }

    override fun assertEntity(entity: Paper) {
        entity.id shouldBeEqualTo ID
        entity.number shouldBeEqualTo NUMBER
        entity.pmId shouldBeEqualTo PM_ID
        entity.doi shouldBeEqualTo DOI
        entity.authors shouldBeEqualTo AUTHORS
        entity.firstAuthor shouldBeEqualTo FIRST_AUTHOR
        entity.isFirstAuthorOverridden shouldBeEqualTo FIRST_AUTHOR_OVERRIDDEN
        entity.title shouldBeEqualTo TITLE
        entity.location shouldBeEqualTo LOCATION
        entity.publicationYear shouldBeEqualTo PUBLICATION_YEAR

        entity.goals shouldBeEqualTo GOALS
        entity.population shouldBeEqualTo POPULATION
        entity.methods shouldBeEqualTo METHODS

        entity.populationPlace shouldBeEqualTo POPULATION_PLACE
        entity.populationParticipants shouldBeEqualTo POPULATION_PARTICIPANTS
        entity.populationDuration shouldBeEqualTo POPULATION_DURATION
        entity.exposurePollutant shouldBeEqualTo EXPOSURE_POLLUTANT
        entity.exposureAssessment shouldBeEqualTo EXPOSURE_ASSESSMENT
        entity.methodStudyDesign shouldBeEqualTo METHOD_STUDY_DESIGN
        entity.methodOutcome shouldBeEqualTo METHOD_OUTCOME
        entity.methodStatistics shouldBeEqualTo METHOD_STATISTICS
        entity.methodConfounders shouldBeEqualTo METHOD_CONFOUNDERS

        entity.result shouldBeEqualTo RESULT
        entity.comment shouldBeEqualTo COMMENT
        entity.intern shouldBeEqualTo INTERN

        entity.resultExposureRange shouldBeEqualTo RESULT_EXPOSURE_RANGE
        entity.resultEffectEstimate shouldBeEqualTo RESULT_EFFECT_ESTIMATE
        entity.resultMeasuredOutcome shouldBeEqualTo RESULT_MEASURED_OUTCOME
        entity.conclusion shouldBeEqualTo CONCLUSION

        entity.originalAbstract shouldBeEqualTo ORIGINAL_ABSTRACT

        entity.mainCodeOfCodeclass1 shouldBeEqualTo MAIN_CODE_OF_CODECLASS1

        entity.codes.shouldBeEmpty()
    }

    companion object {

        const val ID = 1L
        const val NUMBER = 10L
        const val PM_ID = 2
        const val DOI = "101000/1234"
        const val AUTHORS = "authors"
        const val FIRST_AUTHOR = "first author"
        const val FIRST_AUTHOR_OVERRIDDEN = false
        const val TITLE = "title"
        const val LOCATION = "location"
        const val PUBLICATION_YEAR = 3
        const val GOALS = "goals"
        const val POPULATION = "population"
        const val METHODS = "methods"
        const val POPULATION_PLACE = "population place"
        const val POPULATION_PARTICIPANTS = "population  participants"
        const val POPULATION_DURATION = "population duration"
        const val EXPOSURE_POLLUTANT = "exposure pollutant"
        const val EXPOSURE_ASSESSMENT = "exposure assessment"
        const val METHOD_STUDY_DESIGN = "method study design"
        const val METHOD_OUTCOME = "method outcome"
        const val METHOD_STATISTICS = "method statistics"
        const val METHOD_CONFOUNDERS = "method confounders"
        const val RESULT = "result"
        const val COMMENT = "comment"
        const val INTERN = "intern"
        const val RESULT_EXPOSURE_RANGE = "result exposure range"
        const val RESULT_EFFECT_ESTIMATE = "result effect estimate"
        const val RESULT_MEASURED_OUTCOME = "result measured outcome"
        const val CONCLUSION = "conclusion"
        const val ORIGINAL_ABSTRACT = "oa"
        const val MAIN_CODE_OF_CODECLASS1 = "1F"

        fun entityFixtureWithoutIdFields(entityMock: Paper) {
            every { entityMock.number } returns NUMBER
            every { entityMock.pmId } returns PM_ID
            every { entityMock.doi } returns DOI
            every { entityMock.authors } returns AUTHORS
            every { entityMock.firstAuthor } returns FIRST_AUTHOR
            every { entityMock.isFirstAuthorOverridden } returns FIRST_AUTHOR_OVERRIDDEN
            every { entityMock.title } returns TITLE
            every { entityMock.location } returns LOCATION
            every { entityMock.publicationYear } returns PUBLICATION_YEAR

            every { entityMock.goals } returns GOALS
            every { entityMock.population } returns POPULATION
            every { entityMock.methods } returns METHODS

            every { entityMock.populationPlace } returns POPULATION_PLACE
            every { entityMock.populationParticipants } returns POPULATION_PARTICIPANTS
            every { entityMock.populationDuration } returns POPULATION_DURATION
            every { entityMock.exposurePollutant } returns EXPOSURE_POLLUTANT
            every { entityMock.exposureAssessment } returns EXPOSURE_ASSESSMENT
            every { entityMock.methodStudyDesign } returns METHOD_STUDY_DESIGN
            every { entityMock.methodOutcome } returns METHOD_OUTCOME
            every { entityMock.methodStatistics } returns METHOD_STATISTICS
            every { entityMock.methodConfounders } returns METHOD_CONFOUNDERS

            every { entityMock.result } returns RESULT
            every { entityMock.comment } returns COMMENT
            every { entityMock.intern } returns INTERN

            every { entityMock.resultExposureRange } returns RESULT_EXPOSURE_RANGE
            every { entityMock.resultEffectEstimate } returns RESULT_EFFECT_ESTIMATE
            every { entityMock.resultMeasuredOutcome } returns RESULT_MEASURED_OUTCOME
            every { entityMock.conclusion } returns CONCLUSION

            every { entityMock.originalAbstract } returns ORIGINAL_ABSTRACT

            every { entityMock.mainCodeOfCodeclass1 } returns MAIN_CODE_OF_CODECLASS1

            auditFixtureFor(entityMock)
        }
    }
}
