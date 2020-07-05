package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.InsertSetStepSetter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class PaperInsertSetStepSetterTest : InsertSetStepSetterTest<PaperRecord, Paper>() {

    override val setter: InsertSetStepSetter<PaperRecord, Paper> = PaperInsertSetStepSetter()

    override val entity = mockk<Paper>(relaxed = true)
    private val recordMock = mockk<PaperRecord>()

    override fun specificTearDown() {
        confirmVerified(entity, recordMock)
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
        every { moreStep.set(PAPER.FIRST_AUTHOR_OVERRIDDEN, PaperRecordMapperTest.FIRST_AUTHOR_OVERRIDDEN) } returns moreStep
        every { moreStep.set(PAPER.TITLE, PaperRecordMapperTest.TITLE) } returns moreStep
        every { moreStep.set(PAPER.LOCATION, PaperRecordMapperTest.LOCATION) } returns moreStep
        every { moreStep.set(PAPER.PUBLICATION_YEAR, PaperRecordMapperTest.PUBLICATION_YEAR) } returns moreStep

        every { moreStep.set(PAPER.GOALS, PaperRecordMapperTest.GOALS) } returns moreStep
        every { moreStep.set(PAPER.POPULATION, PaperRecordMapperTest.POPULATION) } returns moreStep
        every { moreStep.set(PAPER.METHODS, PaperRecordMapperTest.METHODS) } returns moreStep

        every { moreStep.set(PAPER.POPULATION_PLACE, PaperRecordMapperTest.POPULATION_PLACE) } returns moreStep
        every { moreStep.set(PAPER.POPULATION_PARTICIPANTS, PaperRecordMapperTest.POPULATION_PARTICIPANTS) } returns moreStep
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

        every { moreStep.set(PAPER.RESULT_EXPOSURE_RANGE, PaperRecordMapperTest.RESULT_EXPOSURE_RANGE) } returns moreStep
        every { moreStep.set(PAPER.RESULT_EFFECT_ESTIMATE, PaperRecordMapperTest.RESULT_EFFECT_ESTIMATE) } returns moreStep
        every { moreStep.set(PAPER.RESULT_MEASURED_OUTCOME, PaperRecordMapperTest.RESULT_MEASURED_OUTCOME) } returns moreStep
        every { moreStep.set(PAPER.CONCLUSION, PaperRecordMapperTest.CONCLUSION) } returns moreStep

        every { moreStep.set(PAPER.ORIGINAL_ABSTRACT, PaperRecordMapperTest.ORIGINAL_ABSTRACT) } returns moreStep

        every { moreStep.set(PAPER.MAIN_CODE_OF_CODECLASS1, PaperRecordMapperTest.MAIN_CODE_OF_CODECLASS1) } returns moreStep
    }

    override fun setStepFixtureAudit() {
        every { moreStep.set(PAPER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY) } returns moreStep
        every { moreStep.set(PAPER.LAST_MODIFIED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY) } returns moreStep
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
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

    override fun verifySettingFieldsExceptKeyAndAudit() {
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

    override fun verifySettingAuditFields() {
        verify { moreStep.set(PAPER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY) }
        verify { moreStep.set(PAPER.LAST_MODIFIED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY) }
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        every { entity.id } returns null
        setter.considerSettingKeyOf(moreStep, entity)
        verify { entity.id }
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        every { entity.id } returns PaperRecordMapperTest.ID
        every { moreStep.set(PAPER.ID, PaperRecordMapperTest.ID) } returns moreStep

        setter.considerSettingKeyOf(moreStep, entity)

        verify { entity.id }
        verify { moreStep.set(PAPER.ID, PaperRecordMapperTest.ID) }
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entity, null)
        verify(exactly = 0) { entity.id = any() }
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        every { recordMock.id } returns 3L
        every { moreStep.set(PAPER.ID, PaperRecordMapperTest.ID) } returns moreStep
        setter.resetIdToEntity(entity, recordMock)
        verify { recordMock.id }
        verify { entity.id = any() }
    }
}
