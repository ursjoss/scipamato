package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.RecordMapperTest
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(entity.id).isEqualTo(ID)
        assertThat(entity.number).isEqualTo(NUMBER)
        assertThat(entity.pmId).isEqualTo(PM_ID)
        assertThat(entity.doi).isEqualTo(DOI)
        assertThat(entity.authors).isEqualTo(AUTHORS)
        assertThat(entity.firstAuthor).isEqualTo(FIRST_AUTHOR)
        assertThat(entity.isFirstAuthorOverridden).isEqualTo(FIRST_AUTHOR_OVERRIDDEN)
        assertThat(entity.title).isEqualTo(TITLE)
        assertThat(entity.location).isEqualTo(LOCATION)
        assertThat(entity.publicationYear).isEqualTo(PUBLICATION_YEAR)

        assertThat(entity.goals).isEqualTo(GOALS)
        assertThat(entity.population).isEqualTo(POPULATION)
        assertThat(entity.methods).isEqualTo(METHODS)

        assertThat(entity.populationPlace).isEqualTo(POPULATION_PLACE)
        assertThat(entity.populationParticipants).isEqualTo(POPULATION_PARTICIPANTS)
        assertThat(entity.populationDuration).isEqualTo(POPULATION_DURATION)
        assertThat(entity.exposurePollutant).isEqualTo(EXPOSURE_POLLUTANT)
        assertThat(entity.exposureAssessment).isEqualTo(EXPOSURE_ASSESSMENT)
        assertThat(entity.methodStudyDesign).isEqualTo(METHOD_STUDY_DESIGN)
        assertThat(entity.methodOutcome).isEqualTo(METHOD_OUTCOME)
        assertThat(entity.methodStatistics).isEqualTo(METHOD_STATISTICS)
        assertThat(entity.methodConfounders).isEqualTo(METHOD_CONFOUNDERS)

        assertThat(entity.result).isEqualTo(RESULT)
        assertThat(entity.comment).isEqualTo(COMMENT)
        assertThat(entity.intern).isEqualTo(INTERN)

        assertThat(entity.resultExposureRange).isEqualTo(RESULT_EXPOSURE_RANGE)
        assertThat(entity.resultEffectEstimate).isEqualTo(RESULT_EFFECT_ESTIMATE)
        assertThat(entity.resultMeasuredOutcome).isEqualTo(RESULT_MEASURED_OUTCOME)
        assertThat(entity.conclusion).isEqualTo(CONCLUSION)

        assertThat(entity.originalAbstract).isEqualTo(ORIGINAL_ABSTRACT)

        assertThat(entity.mainCodeOfCodeclass1).isEqualTo(MAIN_CODE_OF_CODECLASS1)

        assertThat(entity.codes).isEmpty()
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
            whenever(entityMock.number).thenReturn(NUMBER)
            whenever(entityMock.pmId).thenReturn(PM_ID)
            whenever(entityMock.doi).thenReturn(DOI)
            whenever(entityMock.authors).thenReturn(AUTHORS)
            whenever(entityMock.firstAuthor).thenReturn(FIRST_AUTHOR)
            whenever(entityMock.isFirstAuthorOverridden).thenReturn(FIRST_AUTHOR_OVERRIDDEN)
            whenever(entityMock.title).thenReturn(TITLE)
            whenever(entityMock.location).thenReturn(LOCATION)
            whenever(entityMock.publicationYear).thenReturn(PUBLICATION_YEAR)

            whenever(entityMock.goals).thenReturn(GOALS)
            whenever(entityMock.population).thenReturn(POPULATION)
            whenever(entityMock.methods).thenReturn(METHODS)

            whenever(entityMock.populationPlace).thenReturn(POPULATION_PLACE)
            whenever(entityMock.populationParticipants).thenReturn(POPULATION_PARTICIPANTS)
            whenever(entityMock.populationDuration).thenReturn(POPULATION_DURATION)
            whenever(entityMock.exposurePollutant).thenReturn(EXPOSURE_POLLUTANT)
            whenever(entityMock.exposureAssessment).thenReturn(EXPOSURE_ASSESSMENT)
            whenever(entityMock.methodStudyDesign).thenReturn(METHOD_STUDY_DESIGN)
            whenever(entityMock.methodOutcome).thenReturn(METHOD_OUTCOME)
            whenever(entityMock.methodStatistics).thenReturn(METHOD_STATISTICS)
            whenever(entityMock.methodConfounders).thenReturn(METHOD_CONFOUNDERS)

            whenever(entityMock.result).thenReturn(RESULT)
            whenever(entityMock.comment).thenReturn(COMMENT)
            whenever(entityMock.intern).thenReturn(INTERN)

            whenever(entityMock.resultExposureRange).thenReturn(RESULT_EXPOSURE_RANGE)
            whenever(entityMock.resultEffectEstimate).thenReturn(RESULT_EFFECT_ESTIMATE)
            whenever(entityMock.resultMeasuredOutcome).thenReturn(RESULT_MEASURED_OUTCOME)
            whenever(entityMock.conclusion).thenReturn(CONCLUSION)

            whenever(entityMock.originalAbstract).thenReturn(ORIGINAL_ABSTRACT)

            whenever(entityMock.mainCodeOfCodeclass1).thenReturn(MAIN_CODE_OF_CODECLASS1)

            auditFixtureFor(entityMock)
        }
    }

}
