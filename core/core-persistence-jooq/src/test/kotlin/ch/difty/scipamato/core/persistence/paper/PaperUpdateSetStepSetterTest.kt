package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.RecordMapperTest
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class PaperUpdateSetStepSetterTest : UpdateSetStepSetterTest<PaperRecord, Paper>() {

    override val setter: UpdateSetStepSetter<PaperRecord, Paper> = PaperUpdateSetStepSetter()

    override val entity = mockk<Paper>()

    override fun specificTearDown() {
        confirmVerified(entity)
    }

    override fun entityFixture() {
        PaperRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        every { step.set(PAPER.NUMBER, PaperRecordMapperTest.NUMBER) } returns moreStep
        every { moreStep.set(PAPER.PM_ID, PaperRecordMapperTest.PM_ID) } returns moreStep

        every { moreStep.set(PAPER.DOI, PaperRecordMapperTest.DOI) } returns moreStep
        every { moreStep.set(PAPER.AUTHORS, PaperRecordMapperTest.AUTHORS) } returns moreStep
        every { moreStep.set(PAPER.FIRST_AUTHOR, PaperRecordMapperTest.FIRST_AUTHOR) } returns moreStep
        every {
            moreStep.set(PAPER.FIRST_AUTHOR_OVERRIDDEN, PaperRecordMapperTest.FIRST_AUTHOR_OVERRIDDEN)
        } returns moreStep
        every { moreStep.set(PAPER.TITLE, PaperRecordMapperTest.TITLE) } returns moreStep
        every { moreStep.set(PAPER.LOCATION, PaperRecordMapperTest.LOCATION) } returns moreStep
        every { moreStep.set(PAPER.PUBLICATION_YEAR, PaperRecordMapperTest.PUBLICATION_YEAR) } returns moreStep

        every { moreStep.set(PAPER.GOALS, PaperRecordMapperTest.GOALS) } returns moreStep
        every { moreStep.set(PAPER.POPULATION, PaperRecordMapperTest.POPULATION) } returns moreStep
        every { moreStep.set(PAPER.METHODS, PaperRecordMapperTest.METHODS) } returns moreStep
        every { moreStep.set(PAPER.POPULATION_PLACE, PaperRecordMapperTest.POPULATION_PLACE) } returns moreStep
        every {
            moreStep.set(PAPER.POPULATION_PARTICIPANTS, PaperRecordMapperTest.POPULATION_PARTICIPANTS)
        } returns moreStep
        every { moreStep.set(PAPER.POPULATION_DURATION, PaperRecordMapperTest.POPULATION_DURATION) } returns moreStep
        every { moreStep.set(PAPER.EXPOSURE_POLLUTANT, PaperRecordMapperTest.EXPOSURE_POLLUTANT) } returns moreStep
        every { moreStep.set(PAPER.EXPOSURE_ASSESSMENT, PaperRecordMapperTest.EXPOSURE_ASSESSMENT) } returns moreStep
        every { moreStep.set(PAPER.METHOD_STUDY_DESIGN, PaperRecordMapperTest.METHOD_STUDY_DESIGN) } returns moreStep
        every { moreStep.set(PAPER.METHOD_OUTCOME, PaperRecordMapperTest.METHOD_OUTCOME) } returns moreStep
        every { moreStep.set(PAPER.METHOD_STATISTICS, PaperRecordMapperTest.METHOD_STATISTICS) } returns moreStep
        every { moreStep.set(PAPER.METHOD_CONFOUNDERS, PaperRecordMapperTest.METHOD_CONFOUNDERS) } returns moreStep

        every { moreStep.set(PAPER.RESULT, PaperRecordMapperTest.RESULT) } returns moreStep
        every { moreStep.set(PAPER.COMMENT, PaperRecordMapperTest.COMMENT) } returns moreStep
        every { moreStep.set(PAPER.INTERN, PaperRecordMapperTest.INTERN) } returns moreStep

        every {
            moreStep.set(PAPER.RESULT_EXPOSURE_RANGE, PaperRecordMapperTest.RESULT_EXPOSURE_RANGE)
        } returns moreStep
        every {
            moreStep.set(PAPER.RESULT_EFFECT_ESTIMATE, PaperRecordMapperTest.RESULT_EFFECT_ESTIMATE)
        } returns moreStep
        every {
            moreStep.set(PAPER.RESULT_MEASURED_OUTCOME, PaperRecordMapperTest.RESULT_MEASURED_OUTCOME)
        } returns moreStep
        every { moreStep.set(PAPER.CONCLUSION, PaperRecordMapperTest.CONCLUSION) } returns moreStep

        every { moreStep.set(PAPER.ORIGINAL_ABSTRACT, PaperRecordMapperTest.ORIGINAL_ABSTRACT) } returns moreStep

        every {
            moreStep.set(PAPER.MAIN_CODE_OF_CODECLASS1, PaperRecordMapperTest.MAIN_CODE_OF_CODECLASS1)
        } returns moreStep
    }

    override fun stepSetFixtureAudit() {
        every { moreStep.set(PAPER.CREATED, RecordMapperTest.CREATED) } returns moreStep
        every { moreStep.set(PAPER.CREATED_BY, RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(PAPER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) } returns moreStep
        every { moreStep.set(PAPER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) } returns moreStep
        every { moreStep.set(PAPER.VERSION, RecordMapperTest.VERSION + 1) } returns moreStep
    }

    override fun verifyCallToAllFieldsExceptAudit() {
        verify { entity.number }
        verify { entity.pmId }
        verify { entity.doi }
        verify { entity.authors }
        verify { entity.firstAuthor }
        verify { entity.isFirstAuthorOverridden }
        verify { entity.title }
        verify { entity.location }
        verify { entity.publicationYear }

        verify { entity.goals }
        verify { entity.population }
        verify { entity.methods }

        verify { entity.populationPlace }
        verify { entity.populationParticipants }
        verify { entity.populationDuration }
        verify { entity.exposurePollutant }
        verify { entity.exposureAssessment }
        verify { entity.methodStudyDesign }
        verify { entity.methodOutcome }
        verify { entity.methodStatistics }
        verify { entity.methodConfounders }

        verify { entity.result }
        verify { entity.comment }
        verify { entity.intern }

        verify { entity.resultExposureRange }
        verify { entity.resultEffectEstimate }
        verify { entity.resultMeasuredOutcome }
        verify { entity.conclusion }

        verify { entity.originalAbstract }

        verify { entity.mainCodeOfCodeclass1 }
    }

    override fun verifyStepSettingExceptAudit() {
        verify { step.set(PAPER.NUMBER, PaperRecordMapperTest.NUMBER) }
        verify { moreStep.set(PAPER.PM_ID, PaperRecordMapperTest.PM_ID) }
        verify { moreStep.set(PAPER.DOI, PaperRecordMapperTest.DOI) }
        verify { moreStep.set(PAPER.AUTHORS, PaperRecordMapperTest.AUTHORS) }
        verify { moreStep.set(PAPER.FIRST_AUTHOR, PaperRecordMapperTest.FIRST_AUTHOR) }
        verify { moreStep.set(PAPER.FIRST_AUTHOR_OVERRIDDEN, PaperRecordMapperTest.FIRST_AUTHOR_OVERRIDDEN) }
        verify { moreStep.set(PAPER.TITLE, PaperRecordMapperTest.TITLE) }
        verify { moreStep.set(PAPER.LOCATION, PaperRecordMapperTest.LOCATION) }
        verify { moreStep.set(PAPER.PUBLICATION_YEAR, PaperRecordMapperTest.PUBLICATION_YEAR) }

        verify { moreStep.set(PAPER.GOALS, PaperRecordMapperTest.GOALS) }
        verify { moreStep.set(PAPER.POPULATION, PaperRecordMapperTest.POPULATION) }
        verify { moreStep.set(PAPER.METHODS, PaperRecordMapperTest.METHODS) }

        verify { moreStep.set(PAPER.POPULATION_PLACE, PaperRecordMapperTest.POPULATION_PLACE) }
        verify { moreStep.set(PAPER.POPULATION_PARTICIPANTS, PaperRecordMapperTest.POPULATION_PARTICIPANTS) }
        verify { moreStep.set(PAPER.POPULATION_DURATION, PaperRecordMapperTest.POPULATION_DURATION) }
        verify { moreStep.set(PAPER.EXPOSURE_POLLUTANT, PaperRecordMapperTest.EXPOSURE_POLLUTANT) }
        verify { moreStep.set(PAPER.EXPOSURE_ASSESSMENT, PaperRecordMapperTest.EXPOSURE_ASSESSMENT) }
        verify { moreStep.set(PAPER.METHOD_STUDY_DESIGN, PaperRecordMapperTest.METHOD_STUDY_DESIGN) }
        verify { moreStep.set(PAPER.METHOD_OUTCOME, PaperRecordMapperTest.METHOD_OUTCOME) }
        verify { moreStep.set(PAPER.METHOD_STATISTICS, PaperRecordMapperTest.METHOD_STATISTICS) }
        verify { moreStep.set(PAPER.METHOD_CONFOUNDERS, PaperRecordMapperTest.METHOD_CONFOUNDERS) }

        verify { moreStep.set(PAPER.RESULT, PaperRecordMapperTest.RESULT) }
        verify { moreStep.set(PAPER.COMMENT, PaperRecordMapperTest.COMMENT) }
        verify { moreStep.set(PAPER.INTERN, PaperRecordMapperTest.INTERN) }

        verify { moreStep.set(PAPER.RESULT_EXPOSURE_RANGE, PaperRecordMapperTest.RESULT_EXPOSURE_RANGE) }
        verify { moreStep.set(PAPER.RESULT_EFFECT_ESTIMATE, PaperRecordMapperTest.RESULT_EFFECT_ESTIMATE) }
        verify { moreStep.set(PAPER.RESULT_MEASURED_OUTCOME, PaperRecordMapperTest.RESULT_MEASURED_OUTCOME) }
        verify { moreStep.set(PAPER.CONCLUSION, PaperRecordMapperTest.CONCLUSION) }

        verify { moreStep.set(PAPER.ORIGINAL_ABSTRACT, PaperRecordMapperTest.ORIGINAL_ABSTRACT) }

        verify { moreStep.set(PAPER.MAIN_CODE_OF_CODECLASS1, PaperRecordMapperTest.MAIN_CODE_OF_CODECLASS1) }
    }

    override fun verifyStepSettingAudit() {
        verify { moreStep.set(PAPER.CREATED, RecordMapperTest.CREATED) }
        verify { moreStep.set(PAPER.CREATED_BY, RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(PAPER.LAST_MODIFIED, RecordMapperTest.LAST_MOD) }
        verify { moreStep.set(PAPER.LAST_MODIFIED_BY, RecordMapperTest.LAST_MOD_BY) }
        verify { moreStep.set(PAPER.VERSION, RecordMapperTest.VERSION + 1) }
    }
}
